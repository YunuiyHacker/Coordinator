package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.dev

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.dev.composable.SupportDeveloper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.roboto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperScreen(
    navHostController: NavHostController, viewModel: DeveloperViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    var clickCount by remember {
        mutableStateOf(0)
    }
    viewModel.state.let { state ->
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = -24.dp),
                        text = stringResource(R.string.developer),
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
                            navHostController.popBackStack(
                                Route.SettingsScreen.route,
                                inclusive = false,
                                saveState = false
                            )
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
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = interactionSource, indication = null
                            ) {
                                clickCount++
                                if (clickCount == 10) Toast
                                    .makeText(
                                        context,
                                        context.getString(R.string.developer_reference),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            },
                        model = ImageRequest.Builder(context).data(R.drawable.developer_avatar)
                            .build(),
                        contentDescription = null
                    )
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .border(
                                width = 40.dp, brush = Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 1f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.47f),
                                        MaterialTheme.colorScheme.primary.copy(0.0f)
                                    )
                                ), shape = CircleShape
                            )
                    )

                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.developer_nickname),
                    fontFamily = roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.developer_full_name),
                    fontFamily = caros,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        viewModel.onEvent(DeveloperEvent.ShowDeveloperEvent)
                    },
                    border = BorderStroke(width = 0.3.dp, color = Color.DarkGray),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.support_developer),
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Color.Transparent, RoundedCornerShape(8.dp)
                            )
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(modifier = Modifier
                            .size(48.dp)
                            .clickable {
                                try {
                                    uriHandler.openUri("https://vk.com/yunuiy_hacker")
                                } catch (e: Exception) {

                                }
                            }
                            .padding(8.dp),
                            painter = painterResource(R.drawable.ic_vk),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                    Box(
                        modifier = Modifier
                            .background(
                                Color.Transparent, RoundedCornerShape(8.dp)
                            )
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(modifier = Modifier
                            .size(48.dp)
                            .clickable {
                                try {
                                    uriHandler.openUri("https://t.me/yunuiy_hacker")
                                } catch (e: Exception) {

                                }
                            }
                            .padding(8.dp),
                            painter = painterResource(R.drawable.ic_telegram),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                    Box(
                        modifier = Modifier
                            .background(
                                Color.Transparent, RoundedCornerShape(8.dp)
                            )
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(modifier = Modifier
                            .size(48.dp)
                            .clickable {
                                try {
                                    uriHandler.openUri("https://github.com/YunuiyHacker")
                                } catch (e: Exception) {

                                }
                            }
                            .padding(8.dp),
                            painter = painterResource(R.drawable.ic_github),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                    Box(
                        modifier = Modifier
                            .background(
                                Color.Transparent, RoundedCornerShape(8.dp)
                            )
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(modifier = Modifier
                            .size(48.dp)
                            .clickable {
                                try {
                                    uriHandler.openUri("https://www.instagram.com/yunuiy_hacker")
                                } catch (e: Exception) {

                                }
                            }
                            .padding(8.dp),
                            painter = painterResource(R.drawable.ic_instagram),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = buildAnnotatedString {
                        append(stringResource(R.string.developer_info_part_1))
                        append(" ")
                        withStyle(
                            style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                        ) {
                            append(stringResource(R.string.developer_name))
                        }
                        append(stringResource(R.string.developer_info_part_2))
                        append("\n\n")
                        append(stringResource(R.string.developer_info_part_3))
                        append(" ")
                        withStyle(
                            style = SpanStyle(textDecoration = TextDecoration.Underline)
                        ) {
                            append(stringResource(R.string.developer_info_part_4))
                        }
                        append(".\n\n")
                        append(stringResource(R.string.developer_info_part_5))
                        append(".")
                    },
                    fontFamily = caros,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Left
                )
            }
        }

        if (state.showDeveloperDialog) {
            Dialog(onDismissRequest = { viewModel.onEvent(DeveloperEvent.HideDeveloperEvent) }) {
                SupportDeveloper(onCopyClick = {
                    viewModel.onEvent(DeveloperEvent.CopyPhoneEvent)
                })
            }
        }
    }
}