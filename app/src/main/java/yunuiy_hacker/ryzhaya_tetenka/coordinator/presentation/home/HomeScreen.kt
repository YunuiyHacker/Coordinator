package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.SwipeLazyColumn
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.TimeTypeSelectRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable.TopTimeOfDayText
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.DateFormats
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toTimeTypeEvent
import java.sql.Time
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.stream.Collectors.toList

@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    viewModel.state.let { state ->
        Scaffold {
            Column(modifier = Modifier.padding(it)) {
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
                    if (state.timeType.toTimeTypeEvent() != TimeTypeEnum.LIFE) {
                        SwipeLazyColumn(modifier = Modifier.wrapContentWidth(),
                            selectedIndex = when (state.timeType.toTimeTypeEvent()) {
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
                                    val c_second: GregorianCalendar = GregorianCalendar()
                                    c_second.time = it.second
                                    val c_first_year: Int = c_first.get(Calendar.YEAR)
                                    val c_first_month: Int = c_first.get(Calendar.MONTH)
                                    val c_first_day: Int = c_first.get(Calendar.DAY_OF_MONTH)
                                    val c_second_year: Int = c_second.get(Calendar.YEAR)
                                    val c_second_month: Int = c_second.get(Calendar.MONTH)
                                    val c_second_day: Int = c_second.get(Calendar.DAY_OF_MONTH)

                                    val tc_first: GregorianCalendar = GregorianCalendar()
                                    tc_first.time = state.selectedWeekStart
                                    val tc_second: GregorianCalendar = GregorianCalendar()
                                    tc_second.time = state.selectedWeekEnd
                                    val tc_first_year: Int = tc_first.get(Calendar.YEAR)
                                    val tc_first_month: Int = tc_first.get(Calendar.MONTH)
                                    val tc_first_day: Int = tc_first.get(Calendar.DAY_OF_MONTH)
                                    val tc_second_year: Int = tc_second.get(Calendar.YEAR)
                                    val tc_second_month: Int = tc_second.get(Calendar.MONTH)
                                    val tc_second_day: Int = tc_second.get(Calendar.DAY_OF_MONTH)

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
                            },
                            items = when (state.timeType.toTimeTypeEvent()) {
                                TimeTypeEnum.DAY -> state.daysList.toList()
                                TimeTypeEnum.WEEK -> state.weeksList.toList()
                                TimeTypeEnum.MONTH -> state.monthsList.toList()
                                TimeTypeEnum.YEAR -> state.yearsList.toList()
                                else -> listOf()
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
                            },
                            onSelectedIndexChange = {
                                when (state.timeType.toTimeTypeEvent()) {
                                    TimeTypeEnum.DAY -> state.selectedDate = state.daysList[it]
                                    TimeTypeEnum.WEEK -> {
                                        state.selectedWeekStart = state.weeksList[it].first
                                        state.selectedWeekEnd = state.weeksList[it].second
                                    }

                                    TimeTypeEnum.MONTH -> state.selectedMonth = state.monthsList[it]
                                    TimeTypeEnum.YEAR -> state.selectedYear = state.yearsList[it]
                                    else -> state.selectedDate = state.daysList[it]
                                }
                            },
                            height = 20.dp
                        ) {}
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
                AnimatedVisibility(state.timeTypeRowVisible) {
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
        }
    }
}