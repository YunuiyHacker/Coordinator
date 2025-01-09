package yunuiy_hacker.ryzhaya_tetenka.coordinator.util

import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import java.util.Formatter

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

fun startAndEndThisWeek(calendar: Calendar): Pair<Date, Date> {
    val firstDay = calendar.firstDayOfWeek
    calendar.set(Calendar.DAY_OF_WEEK, firstDay)
    val startDate = Date(calendar.timeInMillis)
    calendar.add(Calendar.DAY_OF_YEAR, +6)
    val endDate = Date(calendar.timeInMillis)
    return Pair(startDate, endDate)
}

fun timeFormatter(hour: Int, minute: Int): String {
    return Formatter().format("%1\$02d:%2\$02d", hour, minute).toString()
}

fun setCalendarTime(calendar: Calendar): Calendar {
    calendar.set(Calendar.HOUR_OF_DAY, (zoneOffset.totalSeconds / (60 * 60)))
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar
}

fun setDateTime(date: Date): Date {
    date.hours = (zoneOffset.totalSeconds / (60 * 60))
    date.minutes = 0
    date.seconds = 0

    return date
}

val currentZoneDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault())
val zoneOffset: ZoneOffset = currentZoneDateTime.offset