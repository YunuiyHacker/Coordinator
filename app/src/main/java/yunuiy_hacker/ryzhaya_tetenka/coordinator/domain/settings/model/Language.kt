package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.model

import androidx.annotation.DrawableRes

data class Language(
    val ISOCode: String = "",
    val title: String = "",
    @DrawableRes val icons: List<Int> = listOf()
)
