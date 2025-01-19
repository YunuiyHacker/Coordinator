package yunuiy_hacker.ryzhaya_tetenka.coordinator.util

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
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

fun getFileDataFromUri(application: Application, uri: Uri): String {
    val inputStream: InputStream = application.contentResolver.openInputStream(uri)!!
    val data = inputStream.bufferedReader().use { it.readText() }

    inputStream.close()
    return data
}

fun getLaAndLtFromString(coordinates: String): List<Double>? {
    val split = coordinates.split(",")
    if (split.size == 4) {
        return listOf(
            (split[0].trim() + "." + split[1].trim()).toDouble(),
            (split[2].trim() + "." + split[3].trim()).toDouble()
        )
    } else if (split.size == 2) {
        return listOf(split[0].trim().toDouble(), split[1].trim().toDouble())
    }
    return null
}

fun appIsInstalled(context: Context, stringUri: String): Boolean {
    val packageManager: PackageManager = context.packageManager
    try {
        packageManager.getPackageInfo(stringUri, PackageManager.GET_ACTIVITIES)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
    }
    return false
}

fun getMapUri(la: Double, lt: Double): Uri {
    return Uri.parse("geo:0,0?q=$la, $lt")
}