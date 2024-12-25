package yunuiy_hacker.ryzhaya_tetenka.coordinator.util

import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

fun TimeType.toTimeTypeEvent(): TimeTypeEnum {
    return when (this.id) {
        0 -> TimeTypeEnum.DAY
        1 -> TimeTypeEnum.WEEK
        2 -> TimeTypeEnum.MONTH
        3 -> TimeTypeEnum.YEAR
        4 -> TimeTypeEnum.LIFE
        else -> TimeTypeEnum.DAY
    }
}

fun TimeTypeEnum.toTimeType(): TimeType {
    return when (this.name) {
        TimeTypeEnum.DAY.name -> TimeType(id = 0, resId = R.string.day)
        TimeTypeEnum.WEEK.name -> TimeType(id = 1, resId = R.string.week)
        TimeTypeEnum.MONTH.name -> TimeType(id = 2, resId = R.string.month)
        TimeTypeEnum.YEAR.name -> TimeType(id = 3, resId = R.string.year)
        TimeTypeEnum.LIFE.name -> TimeType(id = 4, resId = R.string.life)
        else -> TimeType(id = 0, resId = R.string.day)
    }
}

fun startAndEndThisWeek(calendar: GregorianCalendar): List<Date> {
    val firstDay = calendar.firstDayOfWeek
    calendar.set(Calendar.DAY_OF_WEEK, firstDay)
    val startDate = Date(calendar.timeInMillis)
    calendar.add(Calendar.DAY_OF_YEAR, +6)
    val endDate = Date(calendar.timeInMillis)
    return listOf(startDate, endDate)
}