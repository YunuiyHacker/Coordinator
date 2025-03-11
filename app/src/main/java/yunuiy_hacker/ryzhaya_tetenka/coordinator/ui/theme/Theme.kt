package yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    background = Color.Black,
    primary = Color(0xFF6b00b8),
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surfaceVariant = Color(0xFF212121),
    primaryContainer = Color(0xFF6b00b8).copy(alpha = 0.8f)
)

private val LightColorScheme = lightColorScheme(
    background = Color.White,
    primary = Color(0xFF6b00b8),
    secondary = PurpleGrey40,
    tertiary = Pink40,
    surfaceVariant = Color(0xFFEEEEEE),
    primaryContainer = Color(0xFF6b00b8).copy(alpha = 0.8f)
)

@Composable
fun CoordinatorTheme(
    colorScheme: ColorScheme,
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme.copy(
            primary = colorScheme.primary,
            primaryContainer = colorScheme.primary.copy(alpha = 0.8f),
            surfaceContainer = Color.DarkGray,
            onPrimary = Color.White,
            surfaceTint = colorScheme.primary
        )

        else -> LightColorScheme.copy(
            primary = colorScheme.primary,
            primaryContainer = colorScheme.primary.copy(alpha = 0.8f),
            surfaceContainer = Color.LightGray,
            onPrimary = Color.White,
            surfaceTint = colorScheme.primary
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if (darkTheme) 0xFF000000.toInt() else 0xFFFFFFF
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}