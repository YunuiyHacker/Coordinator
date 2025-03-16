package yunuiy_hacker.ryzhaya_tetenka.coordinator.utils

import android.app.Application
import android.app.LocaleManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.LocaleList
import android.provider.OpenableColumns
import android.util.DisplayMetrics
import android.util.Log.v
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Priority
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.model.Language
import java.io.InputStream
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import java.util.Formatter
import java.util.Locale
import kotlin.random.Random

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

fun Date.toStringFormat(): String {
    return SimpleDateFormat(
        "dd.MM.yyyy"
    ).format(
        this
    )
}

fun hashString(type: String, input: String): String {
    val HEX_CHARS = "0123456789ABCDEF"
    val bytes = MessageDigest.getInstance(type).digest(input.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
        val i = it.toInt()
        result.append(HEX_CHARS[i shr 4 and 0x0f])
        result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString()
}

fun getHashedRandomString(): String {
    return hashString("SHA-256", (Date().time * Random(1000).nextInt()).toString())
}

fun displayName(displayName: String, surname: String, name: String, lastname: String): String {
    var returnValue = ""
    if (displayName.isNotEmpty()) return displayName
    else {
        if (surname.isNotEmpty()) returnValue += surname
        if (name.isNotEmpty()) returnValue += if (returnValue.isNotEmpty()) " " else {
            ""
        } + name
        if (lastname.isNotEmpty()) returnValue += if (returnValue.isNotEmpty()) " " else {
            ""
        } + lastname
    }
    return returnValue
}

fun getAppInfo(context: Context): String {
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val appName = context.getString(R.string.app_name)
    val version = packageInfo.versionName
    return "$appName ${context.getString(R.string.letter_for)} Android v$version ${
        context.getString(
            R.string.by
        )
    } ${context.getString(R.string.developer_nickname)}"
}

fun getFileName(context: Context, uri: Uri): String {

    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor.use {
            if (cursor?.moveToFirst()!!) {
                return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
    }

    return uri.path.toString()
}

fun getScreenSizeInPx(application: Application): Pair<Int, Int> {
    val displayMetrics: DisplayMetrics = DisplayMetrics()
    application.display.getMetrics(displayMetrics)
    return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

fun getScreenSizeInDp(context: Context): Pair<Dp, Dp> {
    val displayMetrics: DisplayMetrics = DisplayMetrics()
    context.display.getMetrics(displayMetrics)
    return Pair(
        (displayMetrics.widthPixels / displayMetrics.density).dp,
        (displayMetrics.heightPixels / displayMetrics.density).dp
    )
}

fun getLanguages(context: Context): List<Language> {
    return listOf(
        Language("RU", context.getString(R.string.russian), icons = listOf(R.drawable.ru)),
        Language("EN", context.getString(R.string.english), icons = listOf(R.drawable.gb)),
        Language("FR", context.getString(R.string.french), icons = listOf(R.drawable.fr)),
        Language("ES", context.getString(R.string.spanish), icons = listOf(R.drawable.es)),
        Language(
            "AR",
            context.getString(R.string.arabian),
            icons = listOf(R.drawable.sa, R.drawable.ae)
        ),
        Language("HI", context.getString(R.string.hindi), icons = listOf(R.drawable.`in`)),
        Language("ZH", context.getString(R.string.chinese), icons = listOf(R.drawable.cn)),
        Language("BN", context.getString(R.string.bengali), icons = listOf(R.drawable.bd)),
        Language("PT", context.getString(R.string.portuguese), icons = listOf(R.drawable.pt)),
        Language("DE", context.getString(R.string.german), icons = listOf(R.drawable.de)),
        Language("JA", context.getString(R.string.japanese), icons = listOf(R.drawable.jp)),
        Language("KO", context.getString(R.string.south_korean), icons = listOf(R.drawable.kr)),
        Language("TT", context.getString(R.string.tatar), icons = listOf(R.drawable.tt)),
        Language("KK", context.getString(R.string.kazakh), icons = listOf(R.drawable.kz)),
        Language("HY", context.getString(R.string.armenian), icons = listOf(R.drawable.am)),
        Language("KA", context.getString(R.string.georgian), icons = listOf(R.drawable.ge)),
        Language("SV", context.getString(R.string.swedish), icons = listOf(R.drawable.se)),
    )
}

fun setLocale(context: Context, locale: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        LocaleHelper.setLocale(context, locale)
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(locale)
        )
    }
}

fun getPriorityByCode(priorityCode: Int): Priority {
    return when (priorityCode) {
        1 -> Priority.URGENT_AND_IMPORTANT
        2 -> Priority.NOT_URGENT_AND_IMPORTANT
        3 -> Priority.URGENT_AND_UNIMPORTANT
        4 -> Priority.NOT_URGENT_AND_UNIMPORTANT
        else -> Priority.NOT_PRIORITY
    }
}