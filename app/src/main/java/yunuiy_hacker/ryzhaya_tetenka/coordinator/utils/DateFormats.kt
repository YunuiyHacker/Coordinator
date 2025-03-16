package yunuiy_hacker.ryzhaya_tetenka.coordinator.utils

import android.icu.text.SimpleDateFormat
import java.util.Locale

object DateFormats {
    fun toDayTimeTypeOutputFormat(locale: Locale): SimpleDateFormat =
        SimpleDateFormat("dd MMMM yyyy", locale)

    fun toWeekTimeTypeOutputFormatFirstPart(locale: Locale): SimpleDateFormat =
        SimpleDateFormat("dd MMMM", locale)

    fun toWeekTimeTypeOutputFormatSecondPart(locale: Locale): SimpleDateFormat =
        SimpleDateFormat("dd MMMM yyyy", locale)

    fun toMonthTimeTypeOutputFormat(locale: Locale): SimpleDateFormat =
        SimpleDateFormat("LLLL yyyy", locale)

    fun toYearTimeTypeOutputFormat(locale: Locale): SimpleDateFormat =
        SimpleDateFormat("yyyy", locale)
}