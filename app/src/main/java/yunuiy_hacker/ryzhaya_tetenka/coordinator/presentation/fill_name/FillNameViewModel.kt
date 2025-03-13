package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.fill_name

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.getLanguages
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FillNameViewModel @Inject constructor(
    private val sharedPrefsHelper: SharedPrefsHelper,
    val application: Application
) :
    ViewModel() {
    val state by mutableStateOf(FillNameState())

    fun onEvent(event: FillNameEvent) {
        when (event) {
            is FillNameEvent.LoadDataEvent -> loadData()

            is FillNameEvent.ChangeNameEvent -> state.name = event.name

            is FillNameEvent.ChangeLanguageEvent -> state.language = event.language
            is FillNameEvent.ToggleThemeEvent -> toggleTheme()

            is FillNameEvent.OnClickButton -> saveName()
        }
    }

    private fun loadData() {
        state.isDarkTheme = sharedPrefsHelper.isDarkTheme

        val savedLanguage = sharedPrefsHelper.language
        state.language = getLanguages(application).find {
            it.ISOCode.toLowerCase(Locale.ROOT)
                .equals(if (!savedLanguage.isNullOrEmpty()) savedLanguage else "ru")
        }!!
    }

    private fun saveName() {
        sharedPrefsHelper.name = state.name

        state.succes = state.name.length >= 2
    }

    private fun toggleTheme() {
        state.isDarkTheme = !state.isDarkTheme
        AppCompatDelegate.setDefaultNightMode(if (state.isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }
}