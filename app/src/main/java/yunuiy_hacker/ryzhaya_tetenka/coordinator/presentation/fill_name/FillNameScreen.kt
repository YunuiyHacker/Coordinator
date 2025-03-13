package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.fill_name

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding.OnboardingEvent
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@Composable
fun FillNameScreen(
    navHostController: NavHostController,
    viewModel: FillNameViewModel = hiltViewModel(),
    onChangeDarkTheme: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val paddingValues = WindowInsets.navigationBars.asPaddingValues()

    LaunchedEffect(Unit) {
        viewModel.onEvent(FillNameEvent.LoadDataEvent)
    }

    viewModel.state.let { state ->
        Scaffold(bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClick = {
                        viewModel.onEvent(FillNameEvent.OnClickButton)
                    },
                    shape = RoundedCornerShape(12.dp),
                    enabled = state.name.length >= 2
                ) {
                    Text(
                        text = stringResource(R.string.get_start),
                        color = Color.White,
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                navHostController.navigate(Route.LanguageScreen.route)
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = state.language.title,
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                state.language.icons.forEachIndexed { index, icon ->
                                    Surface(
                                        modifier = Modifier
                                            .width(30.dp)
                                            .height(22.5.dp),
                                        contentColor = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(4.5.dp),
                                        shadowElevation = 6.dp,
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                modifier = Modifier
                                                    .width(30.dp)
                                                    .height(22.5.dp)
                                                    .clip(RoundedCornerShape(4.5.dp)),
                                                painter = painterResource(icon),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                    if (state.language.icons.size > 1 && index > -1 && index < state.language.icons.size - 1) {
                                        Spacer(modifier = Modifier.width(12.dp))
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                viewModel.onEvent(FillNameEvent.ToggleThemeEvent)
                                onChangeDarkTheme(state.isDarkTheme)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.padding(12.dp)) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = if (state.isDarkTheme) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                                contentDescription = null
                            )
                        }
                    }
                }
                Spacer(Modifier.height(64.dp))
                Text(
                    text = stringResource(R.string.what_is_your_name_title),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = caros,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.what_is_your_name_description),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = caros,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(64.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = state.name,
                    onValueChange = {
                        viewModel.onEvent(FillNameEvent.ChangeNameEvent(it.take(23)))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background
                    ),
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.your_name),
                            style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                            textAlign = TextAlign.Center
                        )
                    },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    singleLine = true
                )
            }
        }

        LaunchedEffect(state.succes) {
            if (state.succes) {
                navHostController.navigate(Route.HomeScreen.route)
            }
        }

        BackHandler {

        }
    }
}