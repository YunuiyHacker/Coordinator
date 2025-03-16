package yunuiy_hacker.ryzhaya_tetenka.coordinator.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import yunuiy_hacker.ryzhaya_tetenka.coordinator.CoordinatorApplication
import java.util.Locale

object LocaleHelper {
    fun setLocale(context: Context, language: String): Context? {
        CoordinatorApplication.locale = language
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }
        return updateResourcesLegacy(context, language);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}