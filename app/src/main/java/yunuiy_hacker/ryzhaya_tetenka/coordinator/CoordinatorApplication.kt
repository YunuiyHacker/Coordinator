package yunuiy_hacker.ryzhaya_tetenka.coordinator

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.LocaleHelper
import javax.inject.Inject

@HiltAndroidApp
class CoordinatorApplication : Application() {

    override fun attachBaseContext(base: Context) {
        locale = SharedPrefsHelper(base).language ?: "ru"
        super.attachBaseContext(LocaleHelper.setLocale(base, locale))
    }

    companion object {
        var locale = "ru"
    }
}