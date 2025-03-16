package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.lang

import android.app.Activity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.BeyondBoundsLayout
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.layoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.MessageWithButtonDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.getLanguages
import java.lang.Thread.sleep
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    navHostController: NavHostController, viewModel: LanguageViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    val languages = getLanguages(context)

    LaunchedEffect(Unit) {
        viewModel.onEvent(LanguageEvent.LoadDataEvent)
    }

    var verticalScrollState = rememberScrollState()

    var text by remember { mutableStateOf(R.string.language_change) }

    viewModel.state.let { state ->
        if (!state.contentState.isLoading.value) {
            Scaffold(topBar = {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    title = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(x = -24.dp),
                            text = stringResource(text),
                            fontFamily = caros,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        Row {
                            Spacer(modifier = Modifier.width(24.dp))
                            Box(modifier = Modifier.clickable(
                                interactionSource = interactionSource, indication = null
                            ) {
                                navHostController.popBackStack()
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = null
                                )
                            }
                        }
                    })
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .verticalScroll(verticalScrollState)
                ) {
                    languages.forEachIndexed { index, lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    viewModel.onEvent(LanguageEvent.ChangeLanguageEvent(lang))
                                }, verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .animateContentSize()
                                ) {
                                    Text(
                                        text = lang.title,
                                        fontFamily = caros,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (state.language == lang) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(
                                            modifier = Modifier
                                                .height(2.dp)
                                                .width(100.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary)
                                        ) {}
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    lang.icons.forEachIndexed { index, icon ->
                                        Surface(
                                            modifier = Modifier
                                                .width(40.dp)
                                                .height(30.dp),
                                            contentColor = Color(0xFFFFFFFF),
                                            shape = RoundedCornerShape(6.dp),
                                            shadowElevation = 6.dp,
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Image(
                                                    modifier = Modifier
                                                        .width(40.dp)
                                                        .height(30.dp)
                                                        .clip(RoundedCornerShape(6.dp)),
                                                    painter = painterResource(icon),
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }
                                        if (lang.icons.size > 1 && index > -1 && index < lang.icons.size - 1) {
                                            Spacer(modifier = Modifier.width(12.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.showMessageWithButtonDialog) {
            MessageWithButtonDialog(message = state.message, onDismissRequest = {

            }, onConfirmRequest = {
                viewModel.onEvent(LanguageEvent.HideMessageWithButtonDialog)
                sleep(10)
                (context as? Activity)?.recreate()
            })
        }
    }
}