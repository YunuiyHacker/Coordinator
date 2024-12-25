package yunuiy_hacker.ryzhaya_tetenka.coordinator.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object DateFormats {
    @SuppressLint("SimpleDateFormat")
    val DayTimeTypeOutputFormat: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy")
    @SuppressLint("SimpleDateFormat")
    val WeekTimeTypeOutputFormatFirstPart: SimpleDateFormat = SimpleDateFormat("dd MMMM")
    @SuppressLint("SimpleDateFormat")
    val WeekTimeTypeOutputFormatSecondPart: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy")
    @SuppressLint("SimpleDateFormat")
    val MonthTimeTypeOutputFormat: SimpleDateFormat = SimpleDateFormat("LLLL yyyy")
    @SuppressLint("SimpleDateFormat")
    val YearTimeTypeOutputFormat: SimpleDateFormat = SimpleDateFormat("yyyy")
}