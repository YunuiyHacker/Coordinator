package yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R

val caros: FontFamily =
    FontFamily(
        Font(resId = R.font.caros_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
        Font(resId = R.font.caros_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
        Font(resId = R.font.caros_regular, weight = FontWeight.Normal, style = FontStyle.Normal)
    )

val circularstd: FontFamily =
    FontFamily(
        Font(resId = R.font.circularstd_book, weight = FontWeight.Normal, style = FontStyle.Normal),
        Font(
            resId = R.font.circularstd_mediumitalic,
            weight = FontWeight.Medium,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.circularstd_medium,
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        )
    )

val roboto: FontFamily = FontFamily(
    Font(resId = R.font.roboto_bold, weight = FontWeight.Bold),
    Font(resId = R.font.roboto_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(resId = R.font.roboto_black, weight = FontWeight.Black),
    Font(resId = R.font.roboto_black_italic, weight = FontWeight.Black, style = FontStyle.Italic),
    Font(resId = R.font.roboto_regular, weight = FontWeight.Normal),
    Font(resId = R.font.roboto_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(resId = R.font.roboto_light, weight = FontWeight.Light),
    Font(resId = R.font.roboto_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(resId = R.font.roboto_medium, weight = FontWeight.Medium),
    Font(resId = R.font.roboto_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(resId = R.font.roboto_thin, weight = FontWeight.Thin),
    Font(resId = R.font.roboto_thin_italic, weight = FontWeight.Thin, style = FontStyle.Italic)
)