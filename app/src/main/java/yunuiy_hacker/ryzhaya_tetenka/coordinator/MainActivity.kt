package yunuiy_hacker.ryzhaya_tetenka.coordinator

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.NavGraph
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.CoordinatorTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val content: () -> Unit = {
            setContent {
                val navHostController = rememberNavController()
                val userNameExists: Boolean = !sharedPrefsHelper.name.isNullOrEmpty()
                var primary by remember {
                    mutableStateOf(
                        if (!sharedPrefsHelper.color.isNullOrEmpty()) Color(
                            sharedPrefsHelper.color?.toInt()!!
                        ) else Color(0xFF6b00b8)
                    )
                }
                var isDarkTheme by remember {
                    mutableStateOf(sharedPrefsHelper.isDarkTheme)
                }

                AppCompatDelegate.setDefaultNightMode(if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)

                CoordinatorTheme(
                    colorScheme = MaterialTheme.colorScheme.copy(primary = primary),
                    darkTheme = isDarkTheme
                ) {
                    NavGraph(navHostController = navHostController,
                        startDestination = if (userNameExists) Route.HomeScreen.route else Route.OnboardingScreen.route,
                        onChangeTheme = {
                            primary = it
                            sharedPrefsHelper.color = it.toArgb().toString()
                        },
                        onChangeDarkTheme = {
                            isDarkTheme = it
                            sharedPrefsHelper.isDarkTheme = it
                        })
                }
            }
        }
        installSplashScreen()
        content()
    }
}