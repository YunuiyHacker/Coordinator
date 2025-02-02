package yunuiy_hacker.ryzhaya_tetenka.coordinator

import android.os.Bundle
import android.os.Environment
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.NavGraph
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.CoordinatorTheme
import java.io.BufferedReader
import java.io.FileReader
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val content: () -> Unit = {
            setContent {
                val navHostController = rememberNavController()
                val userNameExists: Boolean = !sharedPrefsHelper.name.isNullOrEmpty()

                CoordinatorTheme {
                    NavGraph(
                        navHostController = navHostController,
                        startDestination = if (userNameExists) Route.HomeScreen.route else Route.OnboardingScreen.route
                    )
                }
            }
        }
        installSplashScreen()
        content()
    }
}