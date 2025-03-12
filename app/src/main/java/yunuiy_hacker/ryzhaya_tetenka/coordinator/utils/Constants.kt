package yunuiy_hacker.ryzhaya_tetenka.coordinator.utils

import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType

object Constants {
    val timeTypes: List<TimeType> = listOf(
        TimeType(id = 0, resId = R.string.day),
        TimeType(id = 1, resId = R.string.week),
        TimeType(id = 2, resId = R.string.month),
        TimeType(id = 3, resId = R.string.year),
        TimeType(id = 4, resId = R.string.life)
    )
    const val TASKS_DATABASE_NAME = "tasks_db"

    //Package uri's of maps
    const val YANDEX_MAPS_PACKAGE = "ru.yandex.yandexmaps"
    const val GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps"

    //Notifification info
    const val NOTIFICATION_CHANNEL_ID: String = "yunuiy_hacker.ryzhaya_tetenka.coordinator"
    const val NOTIFICATION_CHANNEL_DESCRIPTION: String = "channel description"
    const val NOTIFICATION_ID: String = 17.toString()

}