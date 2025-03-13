package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding.pages.OnboardingFirstScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding.pages.OnboardingSecondScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding.pages.OnboardingThirdScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@Composable
fun OnboardingScreen(
    navHostController: NavHostController, viewModel: OnboardingViewModel = hiltViewModel(),
    onChangeDarkTheme: (isDarkTheme: Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val scope = rememberCoroutineScope()

    val paddingValues = WindowInsets.navigationBars.asPaddingValues()

    LaunchedEffect(Unit) {
        viewModel.onEvent(OnboardingEvent.LoadDataEvent)
    }

    viewModel.state.let { state ->
        Scaffold(bottomBar = {
            Column(modifier = Modifier.padding(paddingValues)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                navHostController.navigate(Route.FillNameScreen.route)
                            }, horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 10.dp),
                            text = stringResource(R.string.skip),
                            fontFamily = caros,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                scope.launch {
                                    if (pagerState.currentPage != pagerState.pageCount - 1) pagerState.animateScrollToPage(
                                        pagerState.currentPage + 1
                                    )
                                    else navHostController.navigate(Route.FillNameScreen.route)
                                }
                            }, horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 10.dp),
                            text = stringResource(R.string.letter_continue),
                            fontFamily = caros,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.background
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .animateContentSize()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                                viewModel.onEvent(OnboardingEvent.ToggleThemeEvent)
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
                    Spacer(modifier = Modifier.width(24.dp))
                }
                HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState) {
                    when (pagerState.currentPage) {
                        0 -> OnboardingFirstScreen(
                            isLoading = state.contentState.isLoading.value, tasks = state.tasks
                        )

                        1 -> OnboardingSecondScreen(
                            isLoading = state.contentState.isLoading.value, place = state.place
                        )

                        2 -> OnboardingThirdScreen(
                            isLoading = state.contentState.isLoading.value, peoples = state.peoples
                        )
                    }
                }
            }
        }
    }
}