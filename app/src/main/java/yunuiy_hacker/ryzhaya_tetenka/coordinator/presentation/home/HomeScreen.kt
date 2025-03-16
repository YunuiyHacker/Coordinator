package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.AddCategoryButton
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.AddEditCategoryDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.CategoryCard
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.DatePickerDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.LoadingIndicator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.MenuItem
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.QuestionDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.TaskItem
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.SearchBar
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.SwipeLazyColumn
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.TasksStatisticsColumn
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.TimeTypeSelectRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.TopTimeOfDayText
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.DateFormats
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.toTimeTypeEvent
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val datePickerState = rememberDatePickerState()
    val selectedCategoryOffset = remember { mutableStateOf(Offset(0f, 0f)) }

    val currentLocale by remember { mutableStateOf(Locale.getDefault()) }

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeEvent.LoadDataEvent)
    }

    var urgentAndImportantVisibility by remember { mutableStateOf(true) }
    var notUrgentAndImportantVisibility by remember { mutableStateOf(true) }
    var urgentAndUnimportantVisibility by remember { mutableStateOf(true) }
    var notUrgentAndUnimportantVisibility by remember { mutableStateOf(true) }
    var notPriority by remember { mutableStateOf(true) }

    viewModel.state.let { state ->
        val state = remember { state }

        Scaffold(floatingActionButton = {
            AnimatedVisibility(!state.isDeletionMode, exit = fadeOut(), enter = fadeIn()) {
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = {
                        navHostController.navigate("${Route.CreateUpdateTaskScreen.route}/${state.timeType.id}/${state.selectedDate.time}/${state.selectedWeekStart.time}/${state.selectedWeekEnd.time}/${state.selectedCategory.id}")
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Rounded.Add,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
            AnimatedVisibility(state.isDeletionMode) {
                Button(
                    onClick = {
                        viewModel.onEvent(
                            HomeEvent.ShowQuestionDialogEvent(
                                context.getString(R.string.deletion),
                                context.getString(R.string.really_want_to_delete_all_selected_tasks)
                            )
                        )
                    }, shape = RoundedCornerShape(10.dp), enabled = state.deletionTasks.isNotEmpty()
                ) {
                    Text(
                        text = stringResource(R.string.delete_all_selected),
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
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
            Box(modifier = Modifier.animateContentSize(tween(20))) {
                AnimatedVisibility(!state.isDeletionMode, exit = fadeOut(), enter = fadeIn()) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.statusBarsPadding())
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                TopTimeOfDayText(
                                    Modifier.padding(top = 8.dp),
                                    timeOfDay = state.timeOfDay,
                                    userName = state.userName
                                )
                                Spacer(Modifier.height(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (state.showLazySwipeColumn) {
                                        if (state.timeType.toTimeTypeEvent() != TimeTypeEnum.LIFE) {
                                            SwipeLazyColumn(modifier = Modifier
                                                .clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null
                                                ) {
                                                    viewModel.onEvent(HomeEvent.ShowDatePickerDialogEvent)
                                                }
                                                .wrapContentWidth(),
                                                selectedIndex = if (state.showLazySwipeColumn) when (state.timeType.toTimeTypeEvent()) {
                                                    TimeTypeEnum.DAY -> state.daysList.indexOfLast {
                                                        val c: GregorianCalendar =
                                                            GregorianCalendar()
                                                        c.time = it
                                                        val year: Int = c.get(Calendar.YEAR)
                                                        val month: Int = c.get(Calendar.MONTH)
                                                        val day: Int = c.get(Calendar.DAY_OF_MONTH)
                                                        val tc: GregorianCalendar =
                                                            GregorianCalendar()
                                                        tc.time = state.selectedDate
                                                        val tyear: Int = tc.get(Calendar.YEAR)
                                                        val tmonth: Int = tc.get(Calendar.MONTH)
                                                        val tday: Int =
                                                            tc.get(Calendar.DAY_OF_MONTH)
                                                        year == tyear && month == tmonth && day == tday
                                                    }

                                                    TimeTypeEnum.WEEK -> state.weeksList.indexOfFirst {
                                                        val c_first: GregorianCalendar =
                                                            GregorianCalendar()
                                                        c_first.time = it.first
                                                        val c_second: GregorianCalendar =
                                                            GregorianCalendar()
                                                        c_second.time = it.second
                                                        val c_first_year: Int =
                                                            c_first.get(Calendar.YEAR)
                                                        val c_first_month: Int =
                                                            c_first.get(Calendar.MONTH)
                                                        val c_first_day: Int =
                                                            c_first.get(Calendar.DAY_OF_MONTH)
                                                        val c_second_year: Int =
                                                            c_second.get(Calendar.YEAR)
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
                                                        val tc_first_year: Int =
                                                            tc_first.get(Calendar.YEAR)
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
                                                        val c: GregorianCalendar =
                                                            GregorianCalendar()
                                                        c.time = it
                                                        val year: Int = c.get(Calendar.YEAR)
                                                        val month: Int = c.get(Calendar.MONTH)
                                                        val tc: GregorianCalendar =
                                                            GregorianCalendar()
                                                        tc.time = state.selectedMonth
                                                        val tyear: Int = tc.get(Calendar.YEAR)
                                                        val tmonth: Int = tc.get(Calendar.MONTH)
                                                        year == tyear && month == tmonth
                                                    }

                                                    TimeTypeEnum.YEAR -> state.yearsList.indexOfLast {
                                                        val c: GregorianCalendar =
                                                            GregorianCalendar()
                                                        c.time = it
                                                        val year: Int = c.get(Calendar.YEAR)
                                                        val tc: GregorianCalendar =
                                                            GregorianCalendar()
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
                                                        TimeTypeEnum.DAY -> DateFormats.toDayTimeTypeOutputFormat(currentLocale).format(
                                                            date
                                                        ).lowercase()

                                                        TimeTypeEnum.WEEK -> "${
                                                            DateFormats.toWeekTimeTypeOutputFormatFirstPart(currentLocale).format(
                                                                (date as Pair<*, *>).first
                                                            ).lowercase()
                                                        } - ${
                                                            DateFormats.toWeekTimeTypeOutputFormatSecondPart(currentLocale).format(
                                                                (date).second
                                                            ).lowercase()
                                                        }"

                                                        TimeTypeEnum.MONTH -> DateFormats.toMonthTimeTypeOutputFormat(currentLocale).format(
                                                            date
                                                        ).lowercase()

                                                        TimeTypeEnum.YEAR -> DateFormats.toYearTimeTypeOutputFormat(currentLocale).format(
                                                            date
                                                        ).lowercase()

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

                                                        else -> state.selectedDate =
                                                            state.daysList[it]
                                                    }

                                                    viewModel.onEvent(HomeEvent.SelectedDateChangeEvent)
                                                },
                                                height = 20.dp,
                                                onScrollingStopped = {})
                                        } else {
                                            Text(
                                                text = stringResource(R.string.life).toLowerCase(
                                                    Locale.ROOT
                                                ),
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
                                                TimeTypeEnum.DAY -> DateFormats.toDayTimeTypeOutputFormat(currentLocale).format(
                                                    state.selectedDate
                                                ).lowercase()

                                                TimeTypeEnum.WEEK -> "${
                                                    DateFormats.toWeekTimeTypeOutputFormatFirstPart(currentLocale).format(
                                                        state.selectedWeekStart
                                                    ).lowercase()
                                                } - ${
                                                    DateFormats.toWeekTimeTypeOutputFormatSecondPart(currentLocale).format(
                                                        state.selectedWeekEnd
                                                    ).lowercase()
                                                }"

                                                TimeTypeEnum.MONTH -> DateFormats.toMonthTimeTypeOutputFormat(currentLocale).format(
                                                    state.selectedMonth
                                                ).lowercase()

                                                TimeTypeEnum.YEAR -> DateFormats.toYearTimeTypeOutputFormat(currentLocale).format(
                                                    state.selectedYear
                                                ).lowercase()

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
                            }
                            Box(modifier = Modifier
                                .clip(CircleShape)
                                .border(
                                    width = 0.5.dp,
                                    color = Color.DarkGray,
                                    shape = CircleShape
                                )
                                .clickable {
                                    navHostController.navigate(Route.SettingsScreen.route)
                                }) {
                                Icon(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .rotate(30f),
                                    painter = painterResource(R.drawable.ic_settings),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        AnimatedVisibility(
                            state.timeTypeRowVisible
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                TimeTypeSelectRow(modifier = Modifier.padding(horizontal = 24.dp),
                                    selectedTimeType = state.timeType,
                                    onChangeSelectedTimeType = {
                                        viewModel.onEvent(HomeEvent.TimeTypeChangeEvent(it))
                                    })
                                Spacer(modifier = Modifier.height(4.dp))
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
                                    interactionSource = interactionSource, indication = null
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
            }
        }, bottomBar = {

        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                AnimatedVisibility(!state.isDeletionMode, exit = fadeOut(), enter = fadeIn()) {
                    Column {
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
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .animateContentSize()
                        ) {
                            Spacer(modifier = Modifier.width(24.dp))
                            AddCategoryButton(onClick = {
                                viewModel.onEvent(HomeEvent.ShowAddCategoryDialogEvent)
                            })
                            Spacer(modifier = Modifier.width(12.dp))
                            CategoryCard(modifier = Modifier.clip(CircleShape),
                                category = state.defaultAllCategoriesValue,
                                selected = state.selectedCategory == state.defaultAllCategoriesValue,
                                onSelectChange = {
                                    viewModel.onEvent(HomeEvent.SelectCategoryEvent(state.defaultAllCategoriesValue))
                                },
                                onLongClick = {

                                })
                            Spacer(modifier = Modifier.width(12.dp))
                            state.categories.forEach { category ->
                                key(category.id) {
                                    CategoryCard(modifier = Modifier
                                        .clip(CircleShape)
                                        .onGloballyPositioned {
                                            selectedCategoryOffset.value = it.positionInRoot()
                                        },
                                        category = category,
                                        selected = state.selectedCategory == category,
                                        onSelectChange = {
                                            viewModel.onEvent(HomeEvent.SelectCategoryEvent(category))
                                        },
                                        onLongClick = {
                                            viewModel.onEvent(HomeEvent.ShowCategoryMenuEvent)
                                            viewModel.onEvent(
                                                HomeEvent.SetCategoryEvent(
                                                    category
                                                )
                                            )
                                        })
                                    if (state.categories.last() == category) Spacer(
                                        modifier = Modifier.width(
                                            24.dp
                                        )
                                    )
                                    else Spacer(modifier = Modifier.width(12.dp))
                                }
                            }
                            MaterialTheme(
                                colorScheme = MaterialTheme.colorScheme.copy(surface = MaterialTheme.colorScheme.surfaceVariant),
                                shapes = MaterialTheme.shapes.copy(extraSmall = ShapeDefaults.Medium)
                            ) {
                                DropdownMenu(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            width = 0.3.dp,
                                            color = Color.DarkGray,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    expanded = state.showCategoryMenu,
                                    onDismissRequest = {
                                        viewModel.onEvent(HomeEvent.HideCategoryMenuEvent)
                                    },
                                    offset = DpOffset(
                                        x = with(LocalDensity.current) { selectedCategoryOffset.value.x.toDp() },
                                        y = 0.dp
                                    )
                                ) {
                                    DropdownMenuItem(text = {
                                        MenuItem(
                                            icon = Icons.Rounded.Edit, title = R.string.edit
                                        )
                                    }, onClick = {
                                        viewModel.onEvent(HomeEvent.HideCategoryMenuEvent)
                                        viewModel.onEvent(HomeEvent.EditCategoryEvent)
                                    })
                                    DropdownMenuItem(text = {
                                        MenuItem(
                                            icon = Icons.Rounded.Delete,
                                            title = R.string.delete,
                                            iconColor = MaterialTheme.colorScheme.primary,
                                            titleColor = MaterialTheme.colorScheme.primary
                                        )
                                    }, onClick = {
                                        viewModel.onEvent(HomeEvent.HideCategoryMenuEvent)
                                        viewModel.onEvent(
                                            HomeEvent.ShowQuestionDialogEvent(
                                                title = context.getString(
                                                    R.string.deletion
                                                ),
                                                text = context.getString(R.string.really_want_to_delete_category)
                                            )
                                        )
                                    })
                                }
                            }
                        }
                    }
                }
                if (state.showTasksStatistics) {
                    AnimatedVisibility(!state.isDeletionMode, exit = fadeOut(), enter = fadeIn()) {
                        Column {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                modifier = Modifier.padding(horizontal = 24.dp),
                                text = stringResource(R.string.tasks_statistics),
                                fontFamily = caros,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TasksStatisticsColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                allCompletedTasks = state.allCompletedTasks,
                                allNotCompletedTasks = state.allNotCompletedTasks,
                                completedTasks = state.completedTasks,
                                notCompletedTasks = state.notCompletedTasks
                            )
                        }
                    }
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
                Spacer(modifier = Modifier.height(if (state.showTaskPriority) 4.dp else 16.dp))
                if (!state.contentState.isLoading.value) AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(), visible = !state.contentState.isLoading.value
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .animateContentSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (state.tasks.isNotEmpty()) {
                            if (!state.showTaskPriority) {
                                state.tasks.forEach { task ->
                                    key(task.id) {
                                        TaskItem(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 24.dp),
                                            task = task,
                                            onCheckedChange = {
                                                viewModel.onEvent(
                                                    HomeEvent.TaskItemCheckboxToggleEvent(
                                                        task
                                                    )
                                                )
                                            },
                                            onClick = {
                                                navHostController.navigate("${Route.TaskScreen.route}/${task.id}")
                                            },
                                            onLongClick = {
                                                viewModel.onEvent(HomeEvent.OnDeletionModeEvent)
                                                if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                    HomeEvent.RemoveSelectedTaskEvent(task)
                                                )
                                                else viewModel.onEvent(
                                                    HomeEvent.AddSelectedTaskEvent(
                                                        task
                                                    )
                                                )
                                            },
                                            isDeletionMode = state.isDeletionMode,
                                            onDeleteCheckedChange = {
                                                if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                    HomeEvent.RemoveSelectedTaskEvent(task)
                                                )
                                                else viewModel.onEvent(
                                                    HomeEvent.AddSelectedTaskEvent(
                                                        task
                                                    )
                                                )
                                            },
                                            isDeleteChecked = state.deletionTasks.contains(task),
                                            onCheckedSubtask = {
                                                viewModel.onEvent(
                                                    HomeEvent.SubtaskItemCheckboxToggleEvent(
                                                        it
                                                    )
                                                )
                                            }
                                        )
                                        if (task.id != state.tasks.last().id) {
                                            Spacer(modifier = Modifier.height(16.dp))
                                        }
                                    }
                                }
                            } else {
                                //urgent_and_important
                                if (state.urgentAndImportantTasks.isNotEmpty()) {
                                    Column(modifier = Modifier.animateContentSize()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    horizontal = 24.dp,
                                                    vertical = 8.dp
                                                ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                modifier = Modifier.clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null
                                                ) {
                                                    urgentAndImportantVisibility =
                                                        !urgentAndImportantVisibility
                                                },
                                                text = stringResource(R.string.urgent_and_important),
                                                fontFamily = caros,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(modifier = Modifier.clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                urgentAndImportantVisibility =
                                                    !urgentAndImportantVisibility
                                            }) {
                                                Icon(
                                                    imageVector = if (urgentAndImportantVisibility) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.5f
                                                    )
                                                )
                                            }
                                        }
                                        if (urgentAndImportantVisibility) {
                                            state.urgentAndImportantTasks.forEach { task ->
                                                key(task.id) {
                                                    TaskItem(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 24.dp),
                                                        task = task,
                                                        onCheckedChange = {
                                                            viewModel.onEvent(
                                                                HomeEvent.TaskItemCheckboxToggleEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        onClick = {
                                                            navHostController.navigate("${Route.TaskScreen.route}/${task.id}")
                                                        },
                                                        onLongClick = {
                                                            viewModel.onEvent(HomeEvent.OnDeletionModeEvent)
                                                            if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                                HomeEvent.RemoveSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                            else viewModel.onEvent(
                                                                HomeEvent.AddSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        isDeletionMode = state.isDeletionMode,
                                                        onDeleteCheckedChange = {
                                                            if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                                HomeEvent.RemoveSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                            else viewModel.onEvent(
                                                                HomeEvent.AddSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        isDeleteChecked = state.deletionTasks.contains(
                                                            task
                                                        ),
                                                        onCheckedSubtask = {
                                                            viewModel.onEvent(
                                                                HomeEvent.SubtaskItemCheckboxToggleEvent(
                                                                    it
                                                                )
                                                            )
                                                        }
                                                    )
                                                    if (task.id != state.urgentAndImportantTasks.last().id) {
                                                        Spacer(modifier = Modifier.height(16.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                //not_urgent_and_important
                                if (state.notUrgentAndImportantTasks.isNotEmpty()) {
                                    Column(modifier = Modifier.animateContentSize()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    horizontal = 24.dp,
                                                    vertical = 8.dp
                                                ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                modifier = Modifier.clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null
                                                ) {
                                                    notUrgentAndImportantVisibility =
                                                        !notUrgentAndImportantVisibility
                                                },
                                                text = stringResource(R.string.not_urgent_and_important),
                                                fontFamily = caros,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(modifier = Modifier.clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                notUrgentAndImportantVisibility =
                                                    !notUrgentAndImportantVisibility
                                            }) {
                                                Icon(
                                                    imageVector = if (notUrgentAndImportantVisibility) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.5f
                                                    )
                                                )
                                            }
                                        }
                                        if (notUrgentAndImportantVisibility) {
                                            state.notUrgentAndImportantTasks.forEach { task ->
                                                key(task.id) {
                                                    TaskItem(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 24.dp),
                                                        task = task,
                                                        onCheckedChange = {
                                                            viewModel.onEvent(
                                                                HomeEvent.TaskItemCheckboxToggleEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        onClick = {
                                                            navHostController.navigate("${Route.TaskScreen.route}/${task.id}")
                                                        },
                                                        onLongClick = {
                                                            viewModel.onEvent(HomeEvent.OnDeletionModeEvent)
                                                            if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                                HomeEvent.RemoveSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                            else viewModel.onEvent(
                                                                HomeEvent.AddSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        isDeletionMode = state.isDeletionMode,
                                                        onDeleteCheckedChange = {
                                                            if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                                HomeEvent.RemoveSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                            else viewModel.onEvent(
                                                                HomeEvent.AddSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        isDeleteChecked = state.deletionTasks.contains(
                                                            task
                                                        ),
                                                        onCheckedSubtask = {
                                                            viewModel.onEvent(
                                                                HomeEvent.SubtaskItemCheckboxToggleEvent(
                                                                    it
                                                                )
                                                            )
                                                        }
                                                    )
                                                    if (task.id != state.notUrgentAndImportantTasks.last().id) {
                                                        Spacer(modifier = Modifier.height(16.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                //urgent_and_unimportant
                                if (state.urgentAndUnimportantTasks.isNotEmpty()) {
                                    Column(modifier = Modifier.animateContentSize()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    horizontal = 24.dp,
                                                    vertical = 8.dp
                                                ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                modifier = Modifier.clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null
                                                ) {
                                                    urgentAndUnimportantVisibility =
                                                        !urgentAndUnimportantVisibility
                                                },
                                                text = stringResource(R.string.urgent_and_unimportant),
                                                fontFamily = caros,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(modifier = Modifier.clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                urgentAndUnimportantVisibility =
                                                    !urgentAndUnimportantVisibility
                                            }) {
                                                Icon(
                                                    imageVector = if (urgentAndUnimportantVisibility) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.5f
                                                    )
                                                )
                                            }
                                        }
                                        if (urgentAndUnimportantVisibility) {
                                            state.urgentAndUnimportantTasks.forEach { task ->
                                                key(task.id) {
                                                    TaskItem(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 24.dp),
                                                        task = task,
                                                        onCheckedChange = {
                                                            viewModel.onEvent(
                                                                HomeEvent.TaskItemCheckboxToggleEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        onClick = {
                                                            navHostController.navigate("${Route.TaskScreen.route}/${task.id}")
                                                        },
                                                        onLongClick = {
                                                            viewModel.onEvent(HomeEvent.OnDeletionModeEvent)
                                                            if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                                HomeEvent.RemoveSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                            else viewModel.onEvent(
                                                                HomeEvent.AddSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        isDeletionMode = state.isDeletionMode,
                                                        onDeleteCheckedChange = {
                                                            if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                                HomeEvent.RemoveSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                            else viewModel.onEvent(
                                                                HomeEvent.AddSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        isDeleteChecked = state.deletionTasks.contains(
                                                            task
                                                        ),
                                                        onCheckedSubtask = {
                                                            viewModel.onEvent(
                                                                HomeEvent.SubtaskItemCheckboxToggleEvent(
                                                                    it
                                                                )
                                                            )
                                                        }
                                                    )
                                                    if (task.id != state.urgentAndUnimportantTasks.last().id) {
                                                        Spacer(modifier = Modifier.height(16.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                //not_urgent_and_unimportant
                                if (state.notUrgentAndUnimportantTasks.isNotEmpty()) {
                                    Column(modifier = Modifier.animateContentSize()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    horizontal = 24.dp,
                                                    vertical = 8.dp
                                                ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                modifier = Modifier.clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null
                                                ) {
                                                    notUrgentAndUnimportantVisibility =
                                                        !notUrgentAndUnimportantVisibility
                                                },
                                                text = stringResource(R.string.not_urgent_and_unimportant),
                                                fontFamily = caros,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(modifier = Modifier.clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                notUrgentAndUnimportantVisibility =
                                                    !notUrgentAndUnimportantVisibility
                                            }) {
                                                Icon(
                                                    imageVector = if (notUrgentAndUnimportantVisibility) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.5f
                                                    )
                                                )
                                            }
                                        }
                                        if (notUrgentAndUnimportantVisibility) {
                                            state.notUrgentAndUnimportantTasks.forEach { task ->
                                                key(task.id) {
                                                    TaskItem(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 24.dp),
                                                        task = task,
                                                        onCheckedChange = {
                                                            viewModel.onEvent(
                                                                HomeEvent.TaskItemCheckboxToggleEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        onClick = {
                                                            navHostController.navigate("${Route.TaskScreen.route}/${task.id}")
                                                        },
                                                        onLongClick = {
                                                            viewModel.onEvent(HomeEvent.OnDeletionModeEvent)
                                                            if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                                HomeEvent.RemoveSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                            else viewModel.onEvent(
                                                                HomeEvent.AddSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        isDeletionMode = state.isDeletionMode,
                                                        onDeleteCheckedChange = {
                                                            if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                                HomeEvent.RemoveSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                            else viewModel.onEvent(
                                                                HomeEvent.AddSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        isDeleteChecked = state.deletionTasks.contains(
                                                            task
                                                        ),
                                                        onCheckedSubtask = {
                                                            viewModel.onEvent(
                                                                HomeEvent.SubtaskItemCheckboxToggleEvent(
                                                                    it
                                                                )
                                                            )
                                                        }
                                                    )
                                                    if (task.id != state.notUrgentAndUnimportantTasks.last().id) {
                                                        Spacer(modifier = Modifier.height(16.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                //not_priority
                                if (state.notPriorityTasks.isNotEmpty()) {
                                    Column(modifier = Modifier.animateContentSize()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    horizontal = 24.dp,
                                                    vertical = 8.dp
                                                ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                modifier = Modifier.clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null
                                                ) {
                                                    notPriority =
                                                        !notPriority
                                                },
                                                text = stringResource(R.string.not_priority),
                                                fontFamily = caros,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(modifier = Modifier.clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                notPriority =
                                                    !notPriority
                                            }) {
                                                Icon(
                                                    imageVector = if (notPriority) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.5f
                                                    )
                                                )
                                            }
                                        }
                                        if (notPriority) {
                                            state.notPriorityTasks.forEach { task ->
                                                key(task.id) {
                                                    TaskItem(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 24.dp),
                                                        task = task,
                                                        onCheckedChange = {
                                                            viewModel.onEvent(
                                                                HomeEvent.TaskItemCheckboxToggleEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        onClick = {
                                                            navHostController.navigate("${Route.TaskScreen.route}/${task.id}")
                                                        },
                                                        onLongClick = {
                                                            viewModel.onEvent(HomeEvent.OnDeletionModeEvent)
                                                            if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                                HomeEvent.RemoveSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                            else viewModel.onEvent(
                                                                HomeEvent.AddSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        isDeletionMode = state.isDeletionMode,
                                                        onDeleteCheckedChange = {
                                                            if (state.deletionTasks.contains(task)) viewModel.onEvent(
                                                                HomeEvent.RemoveSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                            else viewModel.onEvent(
                                                                HomeEvent.AddSelectedTaskEvent(
                                                                    task
                                                                )
                                                            )
                                                        },
                                                        isDeleteChecked = state.deletionTasks.contains(
                                                            task
                                                        ),
                                                        onCheckedSubtask = {
                                                            viewModel.onEvent(
                                                                HomeEvent.SubtaskItemCheckboxToggleEvent(
                                                                    it
                                                                )
                                                            )
                                                        }
                                                    )
                                                    if (task.id != state.notPriorityTasks.last().id) {
                                                        Spacer(modifier = Modifier.height(16.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .align(Alignment.CenterHorizontally),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(48.dp))
                                Text(
                                    text = stringResource(R.string.empty_date),
                                    fontFamily = caros,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(84.dp))
                    }
                } else {
                    AnimatedVisibility(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        visible = state.contentState.isLoading.value
                    ) {
                        LoadingIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
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
                        }, shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                    ) {
                        Text(
                            text = stringResource(R.string.select),
                            color = Color.White,
                            fontFamily = caros,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        if (state.showQuestionDialog) {
            QuestionDialog(title = state.questionTitle,
                text = state.questionText,
                onDismissRequest = {
                    viewModel.onEvent(HomeEvent.HideQuestionDialogEvent)
                },
                onConfirmRequest = {
                    if (state.isDeletionMode) viewModel.onEvent(HomeEvent.DeleteAllSelectedTasksEvent)
                    else viewModel.onEvent(HomeEvent.DeleteCategoryEvent)
                })
        }

        if (state.showAddEditCategoryDialog) {
            AddEditCategoryDialog(
                onDismissRequest = {
                    viewModel.onEvent(HomeEvent.HideAddCategoryDialogEvent)
                },
                onAddClick = {
                    if (state.isEditMode) viewModel.onEvent(
                        HomeEvent.SaveEditedCategoryEvent(
                            state.editionDeletionCategory.copy(
                                title = it
                            )
                        )
                    )
                    else viewModel.onEvent(HomeEvent.CreateCategoryEvent(it))
                },
                category = if (state.isEditMode) state.editionDeletionCategory else null,
                isEditMode = state.isEditMode
            )
        }

        BackHandler {
            if (state.isDeletionMode) {
                viewModel.onEvent(HomeEvent.OffDeletionModeEvent)
            }
        }

        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME || event == Lifecycle.Event.ON_START) {
                    state.userName = viewModel.sharedPrefsHelper.name ?: ""
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}