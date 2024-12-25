package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model

import androidx.annotation.StringRes

data class TimeType(val id: Int = 0, @StringRes val resId: Int = 0)

enum class TimeTypeEnum {
    DAY,
    WEEK,
    MONTH,
    YEAR,
    LIFE
}
