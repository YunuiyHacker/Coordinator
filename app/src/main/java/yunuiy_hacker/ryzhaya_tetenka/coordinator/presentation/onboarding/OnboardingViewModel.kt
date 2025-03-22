package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.getLanguages
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.getLocaleStringResource
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val sharedPrefsHelper: SharedPrefsHelper,
    private val application: Application
) : ViewModel() {
    val state by mutableStateOf(OnboardingState())

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.LoadDataEvent -> loadData()

            is OnboardingEvent.ToggleThemeEvent -> toggleTheme()
        }
    }

    private fun loadData() {
        state.contentState.isLoading.value = true

        state.isDarkTheme = sharedPrefsHelper.isDarkTheme

        val savedLanguage = sharedPrefsHelper.language
        state.language = getLanguages(application).find {
            it.ISOCode.toLowerCase(Locale.ROOT)
                .equals(if (!savedLanguage.isNullOrEmpty()) savedLanguage else "ru")
        }!!

        state.tasks.clear()
        state.peoples.clear()

        state.tasks.add(
            Task(
                id = 1,
                hour = 19,
                minute = 11,
                title = getLocaleStringResource(
                    application.resources.configuration.locale,
                    R.string.onboarding_screen1_task1_title,
                    application as Context
                ),
                content = getLocaleStringResource(
                    application.resources.configuration.locale,
                    R.string.onboarding_screen1_task1_content,
                    application as Context
                )
            )
        )
        state.tasks.add(
            Task(
                id = 2,
                hour = 20,
                minute = 5,
                title = getLocaleStringResource(
                    application.resources.configuration.locale,
                    R.string.onboarding_screen1_task2_content,
                    application as Context
                ),
                content = getLocaleStringResource(
                    application.resources.configuration.locale,
                    R.string.onboarding_screen1_task2_title,
                    application as Context
                )
            )
        )

        state.place = Place(
            id = 1,
            title = getLocaleStringResource(
                application.resources.configuration.locale,
                R.string.home,
                application as Context
            ),
            la = 58.010455,
            lt = 56.229443
        )

        state.peoples.add(
            People(
                id = 1,
                surname = getLocaleStringResource(
                    application.resources.configuration.locale,
                    R.string.developer_surname,
                    application as Context
                ),
                name = getLocaleStringResource(
                    application.resources.configuration.locale,
                    R.string.developer_name,
                    application as Context
                ),
                lastname = getLocaleStringResource(
                    application.resources.configuration.locale,
                    R.string.developer_lastname,
                    application as Context
                ),
                displayName = application.getString(R.string.developer_nickname)
            )
        )

        state.contentState.isLoading.value = false
    }

    private fun toggleTheme() {
        state.isDarkTheme = !state.isDarkTheme
        AppCompatDelegate.setDefaultNightMode(if (state.isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }
}