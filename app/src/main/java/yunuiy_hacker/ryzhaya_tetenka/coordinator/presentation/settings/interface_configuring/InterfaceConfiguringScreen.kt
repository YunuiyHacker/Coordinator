package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.interface_configuring

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterfaceConfiguringScreen(
    navHostController: NavHostController,
    viewModel: InterfaceConfiguringViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        viewModel.onEvent(InterfaceConfiguringEvent.LoadDataEvent)
    }

    viewModel.state.let { state ->
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = -24.dp),
                        text = stringResource(R.string.interface_configuring),
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
                                Route.HomeScreen.route, inclusive = false, saveState = false
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
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            viewModel.onEvent(InterfaceConfiguringEvent.ToggleTaskPriorityShowEvent)
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.show_task_priority),
                            fontFamily = caros,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Switch(
                            modifier = Modifier.height(20.dp),
                            checked = state.showTaskPriority,
                            onCheckedChange = {
                                viewModel.onEvent(InterfaceConfiguringEvent.ToggleTaskPriorityShowEvent)
                            },
                            colors = SwitchDefaults.colors(
                                checkedBorderColor = Color.Transparent,
                                uncheckedBorderColor = Color.Transparent,
                                uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedThumbColor = MaterialTheme.colorScheme.background
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            viewModel.onEvent(InterfaceConfiguringEvent.ToggleTasksStatisticsShowEvent)
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.show_tasks_statistics),
                            fontFamily = caros,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Switch(
                            modifier = Modifier.height(20.dp),
                            checked = state.showTasksStatistics,
                            onCheckedChange = {
                                viewModel.onEvent(InterfaceConfiguringEvent.ToggleTasksStatisticsShowEvent)
                            },
                            colors = SwitchDefaults.colors(
                                checkedBorderColor = Color.Transparent,
                                uncheckedBorderColor = Color.Transparent,
                                uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedThumbColor = MaterialTheme.colorScheme.background
                            )
                        )
                    }
                }
            }
        }
    }
}