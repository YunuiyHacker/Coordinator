package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    category: Category,
    selected: Boolean = true,
    onSelectChange: (Boolean) -> Unit,
    onLongClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val localIndication = LocalIndication.current

    val combinedClickableModifier = modifier
        .combinedClickable(
            interactionSource = interactionSource,
            indication = localIndication,
            onClick = {
                onSelectChange(!selected)
            },
            onLongClick = {
                onLongClick()
            }
        )
        .animateContentSize()

    Row(
        modifier = if (selected) combinedClickableModifier
            .background(
                MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) else combinedClickableModifier
            .border(
                width = 0.5.dp, color = Color.DarkGray, shape = CircleShape
            )
            .background(Color.Transparent, shape = CircleShape)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = category.title,
            fontFamily = caros,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AddCategoryButton(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Row(modifier = modifier
        .clip(CircleShape)
        .clickable {
            onClick()
        }
        .background(Color.Transparent)
        .dashedBorder(
            width = 1.dp, color = Color.DarkGray, shape = CircleShape, on = 8.dp, off = 4.dp
        )) {
        Icon(
            modifier = Modifier.padding(horizontal = 48.dp, vertical = 6.dp),
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

