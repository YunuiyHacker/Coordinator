package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.TaskItem
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding.pages.OnboardingFirstScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding.pages.OnboardingSecondScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding.pages.OnboardingThirdScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@Composable
fun OnboardingScreen(
    navHostController: NavHostController, viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.onEvent(OnboardingEvent.LoadDataEvent)
    }

    viewModel.state.let { state ->
        Scaffold(bottomBar = {
            Column {
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
                                    else
                                        navHostController.navigate(Route.FillNameScreen.route)
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
                HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState) {
                    when (pagerState.currentPage) {
                        0 -> OnboardingFirstScreen(
                            isLoading = state.contentState.isLoading.value, tasks = state.tasks
                        )

                        1 -> OnboardingSecondScreen(
                            isLoading = state.contentState.isLoading.value, place = state.place
                        )

                        2 -> OnboardingThirdScreen(
                            isLoading = state.contentState.isLoading.value,
                            peoples = state.peoples
                        )
                    }
                }
            }
        }
    }
}