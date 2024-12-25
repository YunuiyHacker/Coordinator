package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@Composable
fun SwipeLazyColumn(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    items: List<String>,
    textAlign: TextAlign = TextAlign.End,
    alignment: Alignment = Alignment.CenterStart,
    isScrollingToSelectedItemEnabled: Boolean = false,
    height: Dp,
    onScrollingStopped: () -> Unit
) {
    var isAutoScrolling by remember { mutableStateOf(false) }
    val listState = rememberLazyListState(selectedIndex)
    SwipeLazyColumn(modifier = modifier,
        selectedIndex = selectedIndex,
        onSelectedIndexChange = onSelectedIndexChange,
        isAutoScrolling = isAutoScrolling,
        height = height,
        isScrollingToSelectedItemEnabled = isScrollingToSelectedItemEnabled,
        listState = listState,
        onScrollingStopped = {
            isAutoScrolling = false
            onScrollingStopped()
        }) {
        // we add some empty rows at the beginning and end of list to make it feel that it is a center focused list
        val count = items.size - 1
        items(count) {
            SliderItem(value = it,
                selectedIndex = selectedIndex,
                items = items,
                alignment = alignment,
                textAlign = textAlign,
                height = height,
                onItemClick = { index ->

                })
        }
    }
}

@SuppressLint("FrequentlyChangedStateReadInComposition")
@Composable
private fun SwipeLazyColumn(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    height: Dp,
    isAutoScrolling: Boolean,
    isScrollingToSelectedItemEnabled: Boolean = false,
    listState: LazyListState,
    onScrollingStopped: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    var isManualScrolling by remember { mutableStateOf(true) }
    var isInitialWaitOver by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        delay(250)
        isInitialWaitOver =
            true // because not two animateScrollToItem should be called at once and this delay protect that from happening.
    }

    if (isScrollingToSelectedItemEnabled) {
        LaunchedEffect(key1 = selectedIndex) {
            if (isInitialWaitOver) {
                isManualScrolling = false
                if (!listState.isScrollInProgress) {
                    listState.animateScrollToItem(selectedIndex)
                }
                delay(10)
                isManualScrolling = true
            }
        }
    }

    // Update selected item index
    LaunchedEffect(key1 = listState.firstVisibleItemScrollOffset) {
        if (!isAutoScrolling && isManualScrolling && isInitialWaitOver) {
            val index =
                listState.firstVisibleItemIndex + if (listState.firstVisibleItemScrollOffset > height.value / 5) 1 else 0
            onSelectedIndexChange(index)
        }
    }

    // For smooth scrolling to center item
    var isAnimateScrollToItemTriggered by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = listState.isScrollInProgress) {
        if (!isAnimateScrollToItemTriggered) {
            listState.animateScrollToItem(selectedIndex)
            isAnimateScrollToItemTriggered = true
            onScrollingStopped()
        }
    }
    LaunchedEffect(key1 = listState.interactionSource.interactions) {
        listState.interactionSource.interactions.collect {
            if (it is DragInteraction.Start) {
                isAnimateScrollToItemTriggered = false
            }
        }
    }

    LazyColumn(
        modifier = modifier.height(height), state = listState
    ) {
        content()
    }
}

@Composable
private fun SliderItem(
    value: Int,
    selectedIndex: Int,
    items: List<String>,
    onItemClick: (Int) -> Unit,
    alignment: Alignment,
    height: Dp,
    textAlign: TextAlign,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    // this gap variable helps in maintaining list as center focused list
    val gap = (5 - 1) / 2
    val isSelected = value == selectedIndex + gap
    val scale by animateFloatAsState(targetValue = if (isSelected) 0f else 1f)
    if (value >= gap && value < items.size + gap) {
        Box(
            modifier = Modifier
                .height(height)
                .padding(
                    start = if (alignment == Alignment.CenterStart) 1.dp else 0.dp,
                    end = if (alignment == Alignment.CenterEnd) 1.dp else 0.dp
                )
        ) {
            Box(modifier = Modifier
                .wrapContentWidth()
                .clickable(interactionSource = interactionSource, indication = null) {
                    onItemClick(value)
                }) {
                Text(
                    text = items[value],
                    modifier = Modifier
                        .align(alignment)
                        .scale(scale),
                    textAlign = textAlign,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = caros,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
        }
    }
}