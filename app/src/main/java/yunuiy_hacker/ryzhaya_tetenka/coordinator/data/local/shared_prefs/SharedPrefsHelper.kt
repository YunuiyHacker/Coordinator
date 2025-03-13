package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs

import android.content.Context
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum

class SharedPrefsHelper(context: Context) {

    private val tasker = context.getSharedPreferences(TASKER_TITLE, Context.MODE_PRIVATE)
    private val personal_data =
        context.getSharedPreferences(PERSONAL_DATA_TITLE, Context.MODE_PRIVATE)
    private val settings =
        context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    //tasker
    var timeTypeEnum
        set(value) {
            with(tasker.edit()) {
                putInt(
                    TIME_TYPE_ENUM, when (value.name) {
                        TimeTypeEnum.DAY.name -> 0
                        TimeTypeEnum.WEEK.name -> 1
                        TimeTypeEnum.MONTH.name -> 2
                        TimeTypeEnum.YEAR.name -> 3
                        TimeTypeEnum.LIFE.name -> 4
                        else -> 0
                    }
                )
                apply()
            }
        }
        get() = when (tasker.getInt(TIME_TYPE_ENUM, 0)) {
            0 -> TimeTypeEnum.DAY
            1 -> TimeTypeEnum.WEEK
            2 -> TimeTypeEnum.MONTH
            3 -> TimeTypeEnum.YEAR
            4 -> TimeTypeEnum.LIFE
            else -> TimeTypeEnum.DAY
        }

    var categoryId
        set(value) {
            with(tasker.edit()) {
                if (value < 0) remove(CATEGORY_ID)
                else putInt(CATEGORY_ID, value)
                apply()
            }
        }
        get() = tasker.getInt(CATEGORY_ID, 0)

    var unsavedTitle
        set(value) {
            with(tasker.edit()) {
                if (value.isNullOrEmpty()) remove(UNSAVED_TITLE)
                else putString(UNSAVED_TITLE, value)
                apply()
            }
        }
        get() = tasker.getString(UNSAVED_TITLE, "")

    var unsavedContent
        set(value) {
            with(tasker.edit()) {
                if (value.isNullOrEmpty()) remove(UNSAVED_CONTENT)
                else putString(UNSAVED_CONTENT, value)
                apply()
            }
        }
        get() = tasker.getString(UNSAVED_CONTENT, "")

    //personal data
    var name
        set(value) {
            with(personal_data.edit()) {
                if (value.isNullOrEmpty()) remove(NAME)
                else putString(NAME, value)
                apply()
            }
        }
        get() = personal_data.getString(NAME, "")

    //settings
    var color
        set(value) {
            with(settings.edit()) {
                if (value.isNullOrEmpty()) remove(COLOR)
                else putString(COLOR, value)
                apply()
            }
        }
        get() = settings.getString(COLOR, "")

    var isDarkTheme
        set(value) {
            with(settings.edit()) {
                putBoolean(IS_DARK_THEME, value)
                apply()
            }
        }
        get() = settings.getBoolean(IS_DARK_THEME, false)

    var language
        set(value) {
            with(settings.edit()) {
                if (value.isNullOrEmpty()) remove(LANGUAGE)
                else putString(LANGUAGE, value)
                apply()
            }
        }
        get() = settings.getString(LANGUAGE, "")

    companion object {
        private const val TASKER_TITLE = "tasker"
        private const val PERSONAL_DATA_TITLE = "personal_data"
        private const val SETTINGS = "settings"

        //tasker
        private const val TIME_TYPE_ENUM = "timeTypeEnum"
        private const val CATEGORY_ID = "categoryId"
        private const val UNSAVED_TITLE = "unsavedTitle"
        private const val UNSAVED_CONTENT = "unsavedContent"

        //personal data
        private const val NAME = "name"

        //settings
        private const val COLOR = "color"
        private const val IS_DARK_THEME = "theme"
        private const val LANGUAGE = "language"
    }
}