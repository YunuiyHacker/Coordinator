package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.lang

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.model.Language
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.getLanguages
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.setLocale
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val sharedPrefsHelper: SharedPrefsHelper, val application: Application
) : ViewModel() {
    val state by mutableStateOf(LanguageState())

    fun onEvent(event: LanguageEvent) {
        when (event) {
            is LanguageEvent.LoadDataEvent -> loadData()

            is LanguageEvent.ChangeLanguageEvent -> changeLanguage(event.language)
        }
    }

    private fun loadData() {
        val savedLanguage = sharedPrefsHelper.language
        state.language = getLanguages(application).find {
            it.ISOCode.toLowerCase(Locale.ROOT)
                .equals(if (!savedLanguage.isNullOrEmpty()) savedLanguage else "ru")
        }!!
    }


    private fun changeLanguage(language: Language) {
        sharedPrefsHelper.language = language.ISOCode.toLowerCase()
        state.language = language

        setLocale(application as Context, language.ISOCode)
    }
}