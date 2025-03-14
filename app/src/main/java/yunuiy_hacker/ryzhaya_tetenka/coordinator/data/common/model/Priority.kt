package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model

import androidx.annotation.StringRes
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R

sealed class Priority(val priority: String, val code: Int, @StringRes val resId: Int) {
    data object URGENT_AND_IMPORTANT : Priority("urgent_and_important", 1, R.string.urgent_and_important)
    data object NOT_URGENT_AND_IMPORTANT : Priority("not_urgent_and_important", 2, R.string.not_urgent_and_important)
    data object URGENT_AND_UNIMPORTANT : Priority("urgent_and_unimportant", 3, R.string.urgent_and_unimportant)
    data object NOT_URGENT_AND_UNIMPORTANT : Priority("not_urgent_and_unimportant", 4, R.string.not_urgent_and_unimportant)
    data object NOT_PRIORITY : Priority("not_priority", 5, R.string.not_priority)
}