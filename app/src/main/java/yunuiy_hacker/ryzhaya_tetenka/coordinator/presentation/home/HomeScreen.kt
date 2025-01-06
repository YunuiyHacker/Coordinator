package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.DatePickerDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.QuestionDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.TaskItem
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.SearchBar
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.SwipeLazyColumn
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.TimeTypeSelectRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.TopTimeOfDayText
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.DateFormats
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toTimeTypeEvent
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeEvent.LoadDataEvent)
    }

    viewModel.state.let { state ->

        Scaffold(floatingActionButton = {
            AnimatedVisibility(!state.isDeletionMode, exit = fadeOut(), enter = fadeIn()) {
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = {
                        navHostController.navigate("${Route.CreateUpdateTaskScreen.route}/${state.timeType.id}/${state.selectedDate.time}/${state.selectedWeekStart.time}/${state.selectedWeekEnd.time}")
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Rounded.Add,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                }
            }
            AnimatedVisibility(state.isDeletionMode) {
                Button(
                    onClick = {
                        viewModel.onEvent(HomeEvent.ShowQuestionDialogEvent)
                    },
                    shape = RoundedCornerShape(10.dp),
                    enabled = state.deletionTasks.isNotEmpty()
                ) {
                    Text(
                        text = stringResource(R.string.delete_all_selected),
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }, floatingActionButtonPosition = FabPosition.Center, snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Column {
                    Snackbar(modifier = Modifier.padding(horizontal = 24.dp), action = {
                        TextButton(onClick = {
                            data.dismiss()
                        }) {
                            Text(
                                text = stringResource(R.string.yes),
                                fontFamily = caros,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }, containerColor = MaterialTheme.colorScheme.primary) {
                        Text(
                            text = stringResource(R.string.cancel_deletion),
                            fontFamily = caros,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }, topBar = {
            AnimatedVisibility(!state.isDeletionMode, exit = fadeOut(), enter = fadeIn()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.statusBarsPadding())
                    TopTimeOfDayText(
                        Modifier
                            .padding(horizontal = 24.dp)
                            .padding(top = 8.dp),
                        timeOfDay = state.timeOfDay,
                        userName = state.userName
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (state.showLazySwipeColumn) {
                            if (state.timeType.toTimeTypeEvent() != TimeTypeEnum.LIFE) {
                                SwipeLazyColumn(modifier = Modifier
                                    .padding(start = 1.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        viewModel.onEvent(HomeEvent.ShowDatePickerDialogEvent)
                                    }
                                    .wrapContentWidth()
                                    .animateContentSize(),
                                    selectedIndex = if (state.showLazySwipeColumn) when (state.timeType.toTimeTypeEvent()) {
                                        TimeTypeEnum.DAY -> state.daysList.indexOfLast {
                                            val c: GregorianCalendar = GregorianCalendar()
                                            c.time = it
                                            val year: Int = c.get(Calendar.YEAR)
                                            val month: Int = c.get(Calendar.MONTH)
                                            val day: Int = c.get(Calendar.DAY_OF_MONTH)
                                            val tc: GregorianCalendar = GregorianCalendar()
                                            tc.time = state.selectedDate
                                            val tyear: Int = tc.get(Calendar.YEAR)
                                            val tmonth: Int = tc.get(Calendar.MONTH)
                                            val tday: Int = tc.get(Calendar.DAY_OF_MONTH)
                                            year == tyear && month == tmonth && day == tday
                                        }

                                        TimeTypeEnum.WEEK -> state.weeksList.indexOfFirst {
                                            val c_first: GregorianCalendar = GregorianCalendar()
                                            c_first.time = it.first
                                            val c_second: GregorianCalendar =
                                                GregorianCalendar()
                                            c_second.time = it.second
                                            val c_first_year: Int = c_first.get(Calendar.YEAR)
                                            val c_first_month: Int = c_first.get(Calendar.MONTH)
                                            val c_first_day: Int =
                                                c_first.get(Calendar.DAY_OF_MONTH)
                                            val c_second_year: Int = c_second.get(Calendar.YEAR)
                                            val c_second_month: Int =
                                                c_second.get(Calendar.MONTH)
                                            val c_second_day: Int =
                                                c_second.get(Calendar.DAY_OF_MONTH)

                                            val tc_first: GregorianCalendar =
                                                GregorianCalendar()
                                            tc_first.time = state.selectedWeekStart
                                            val tc_second: GregorianCalendar =
                                                GregorianCalendar()
                                            tc_second.time = state.selectedWeekEnd
                                            val tc_first_year: Int = tc_first.get(Calendar.YEAR)
                                            val tc_first_month: Int =
                                                tc_first.get(Calendar.MONTH)
                                            val tc_first_day: Int =
                                                tc_first.get(Calendar.DAY_OF_MONTH)
                                            val tc_second_year: Int =
                                                tc_second.get(Calendar.YEAR)
                                            val tc_second_month: Int =
                                                tc_second.get(Calendar.MONTH)
                                            val tc_second_day: Int =
                                                tc_second.get(Calendar.DAY_OF_MONTH)

                                            (c_first_day == tc_first_day && c_first_month == tc_first_month && c_first_year == tc_first_year) && (c_second_day == tc_second_day && c_second_month == tc_second_month && c_second_year == tc_second_year)
                                        }

                                        TimeTypeEnum.MONTH -> state.monthsList.indexOfLast {
                                            val c: GregorianCalendar = GregorianCalendar()
                                            c.time = it
                                            val year: Int = c.get(Calendar.YEAR)
                                            val month: Int = c.get(Calendar.MONTH)
                                            val tc: GregorianCalendar = GregorianCalendar()
                                            tc.time = state.selectedMonth
                                            val tyear: Int = tc.get(Calendar.YEAR)
                                            val tmonth: Int = tc.get(Calendar.MONTH)
                                            year == tyear && month == tmonth
                                        }

                                        TimeTypeEnum.YEAR -> state.yearsList.indexOfLast {
                                            val c: GregorianCalendar = GregorianCalendar()
                                            c.time = it
                                            val year: Int = c.get(Calendar.YEAR)
                                            val tc: GregorianCalendar = GregorianCalendar()
                                            tc.time = state.selectedYear
                                            val tyear: Int = tc.get(Calendar.YEAR)
                                            year == tyear
                                        }

                                        else -> -100
                                    } else -1,
                                    items = when (state.timeType.toTimeTypeEvent()) {
                                        TimeTypeEnum.DAY -> state.daysList
                                        TimeTypeEnum.WEEK -> state.weeksList
                                        TimeTypeEnum.MONTH -> state.monthsList
                                        TimeTypeEnum.YEAR -> state.yearsList
                                        else -> mutableListOf()
                                    }.map { date ->
                                        when (state.timeType.toTimeTypeEvent()) {
                                            TimeTypeEnum.DAY -> DateFormats.DayTimeTypeOutputFormat.format(
                                                date
                                            )

                                            TimeTypeEnum.WEEK -> "${
                                                DateFormats.WeekTimeTypeOutputFormatFirstPart.format(
                                                    (date as Pair<*, *>).first
                                                )
                                            } - ${
                                                DateFormats.WeekTimeTypeOutputFormatSecondPart.format(
                                                    (date).second
                                                )
                                            }"

                                            TimeTypeEnum.MONTH -> DateFormats.MonthTimeTypeOutputFormat.format(
                                                date
                                            )

                                            TimeTypeEnum.YEAR -> DateFormats.YearTimeTypeOutputFormat.format(
                                                date
                                            )

                                            else -> ""
                                        }
                                    }.toMutableList(),
                                    onSelectedIndexChange = {
                                        when (state.timeType.toTimeTypeEvent()) {
                                            TimeTypeEnum.DAY -> state.selectedDate =
                                                state.daysList[it]

                                            TimeTypeEnum.WEEK -> {
                                                state.selectedWeekStart =
                                                    state.weeksList[it].first
                                                state.selectedWeekEnd =
                                                    state.weeksList[it].second
                                            }

                                            TimeTypeEnum.MONTH -> state.selectedMonth =
                                                state.monthsList[it]

                                            TimeTypeEnum.YEAR -> state.selectedYear =
                                                state.yearsList[it]

                                            else -> state.selectedDate = state.daysList[it]
                                        }

                                        viewModel.onEvent(HomeEvent.SelectedDateChangeEvent)
                                    },
                                    height = 20.dp,
                                    onScrollingStopped = {})
                            } else {
                                Text(
                                    text = stringResource(R.string.life).toLowerCase(Locale.ROOT),
                                    modifier = Modifier,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = caros,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 20.sp
                                )
                            }
                        } else {
                            Text(
                                text = when (state.timeType.toTimeTypeEvent()) {
                                    TimeTypeEnum.DAY -> DateFormats.DayTimeTypeOutputFormat.format(
                                        state.selectedDate
                                    )

                                    TimeTypeEnum.WEEK -> "${
                                        DateFormats.WeekTimeTypeOutputFormatFirstPart.format(
                                            state.selectedWeekStart
                                        )
                                    } - ${
                                        DateFormats.WeekTimeTypeOutputFormatSecondPart.format(
                                            state.selectedWeekEnd
                                        )
                                    }"

                                    TimeTypeEnum.MONTH -> DateFormats.MonthTimeTypeOutputFormat.format(
                                        state.selectedMonth
                                    )

                                    TimeTypeEnum.YEAR -> DateFormats.YearTimeTypeOutputFormat.format(
                                        state.selectedYear
                                    )

                                    else -> ""
                                },
                                modifier = Modifier,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = caros,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                        Box(modifier = Modifier.clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            state.timeTypeRowVisible = !state.timeTypeRowVisible
                        }) {
                            Icon(
                                imageVector = if (state.timeTypeRowVisible) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                    AnimatedVisibility(
                        state.timeTypeRowVisible
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            TimeTypeSelectRow(modifier = Modifier.padding(horizontal = 24.dp),
                                selectedTimeType = state.timeType,
                                onChangeSelectedTimeType = {
                                    viewModel.onEvent(HomeEvent.TimeTypeChangeEvent(it))
                                })
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
            AnimatedVisibility(state.isDeletionMode, exit = fadeOut(), enter = fadeIn()) {
                TopAppBar(
                    navigationIcon = {
                        Row {
                            Spacer(modifier = Modifier.width(24.dp))
                            Icon(
                                modifier = Modifier.clickable(
                                    interactionSource = interactionSource, indication = null
                                ) {
                                    viewModel.onEvent(HomeEvent.OffDeletionModeEvent)
                                },
                                imageVector = Icons.Rounded.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.deletion_mode),
                            fontFamily = caros,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    },
                    actions = {
                        Icon(
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                if (state.selectedAll) viewModel.onEvent(HomeEvent.UnselectAllEvent)
                                else viewModel.onEvent(HomeEvent.SelectAllEvent)
                            },
                            imageVector = if (state.selectedAll) Icons.Rounded.CheckBox else Icons.Rounded.CheckBoxOutlineBlank,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(24.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            }
        }, bottomBar = {

        }) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                AnimatedVisibility(!state.isDeletionMode, exit = fadeOut(), enter = fadeIn()) {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        query = state.query,
                        onQueryChange = {
                            viewModel.onEvent(HomeEvent.SearchQueryChangeEvent(it))
                        },
                        onSearch = {
                            viewModel.onEvent(HomeEvent.OnClickSearchEvent)
                        },
                        placeholder = stringResource(R.string.try_to_find_task)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = stringResource(R.string.tasks),
                    fontFamily = caros,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(!state.contentState.isLoading.value) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                    ) {
                        state.tasks.forEach { task ->
                            TaskItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                task = task,
                                onCheckedChange = {
                                    viewModel.onEvent(HomeEvent.TaskItemCheckboxToggleEvent(task))
                                },
                                onClick = {
                                    navHostController.navigate("${Route.TaskScreen.route}/${task.id}")
                                },
                                onLongClick = {
                                    viewModel.onEvent(HomeEvent.OnDeletionModeEvent)
                                    if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                        HomeEvent.RemoveSelectedTaskEvent(task)
                                    )
                                    else viewModel.onEvent(HomeEvent.AddSelectedTaskEvent(task))
                                },
                                isDeletionMode = state.isDeletionMode,
                                onDeleteCheckedChange = {
                                    if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                        HomeEvent.RemoveSelectedTaskEvent(task)
                                    )
                                    else viewModel.onEvent(HomeEvent.AddSelectedTaskEvent(task))
                                },
                                isDeleteChecked = state.deletionTasks.contains(task)
                            )
                            if (task.id != state.tasks.last().id) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(84.dp))
                    }
                }
            }
        }

        if (state.showDatePickerDialog) {
            DatePickerDialog(
                modifier = Modifier.heightIn(max = 700.dp),
                onDismissRequest = {
                    viewModel.onEvent(HomeEvent.HideDatePickerDialogEvent)
                },
                confirmButton = {},
                colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    DatePicker(state = datePickerState)
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp), onClick = {
                            viewModel.onEvent(
                                HomeEvent.SelectDatePickerDialogEvent(
                                    datePickerState.selectedDateMillis ?: 0
                                )
                            )
                        }, shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.select),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = caros,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        if (state.showQuestionDialog) {
            QuestionDialog(title = R.string.deletion,
                text = R.string.really_want_to_delete_all_selected_tasks,
                onDismissRequest = {
                    viewModel.onEvent(HomeEvent.HideQuestionDialogEvent)
                },
                onConfirmRequest = {
                    viewModel.onEvent(HomeEvent.DeleteAllSelectedTasksEvent)
                })
        }

        BackHandler {
            if (state.isDeletionMode) {
                viewModel.onEvent(HomeEvent.OffDeletionModeEvent)
            }
        }
    }
}