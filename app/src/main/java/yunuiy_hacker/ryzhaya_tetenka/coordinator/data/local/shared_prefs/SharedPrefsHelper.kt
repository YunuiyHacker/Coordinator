package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs

import android.content.Context
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum

class SharedPrefsHelper(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_TITLE, Context.MODE_PRIVATE)

    var name
        set(value) {
            with(prefs.edit()) {
                if (value.isNullOrEmpty()) remove(NAME)
                else putString(NAME, value)
                apply()
            }
        }
        get() = prefs.getString(NAME, "")

    var timeTypeEnum
        set(value) {
            with(prefs.edit()) {
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
        get() = when (prefs.getInt(TIME_TYPE_ENUM, 0)) {
            0 -> TimeTypeEnum.DAY
            1 -> TimeTypeEnum.WEEK
            2 -> TimeTypeEnum.MONTH
            3 -> TimeTypeEnum.YEAR
            4 -> TimeTypeEnum.LIFE
            else -> TimeTypeEnum.DAY
        }

    companion object {
        private const val PREFS_TITLE = "storage"

        private const val NAME = "name"
        private const val TIME_TYPE_ENUM = "timeTypeEnum"
    }
}