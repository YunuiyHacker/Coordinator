package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model

data class People(
    val id: Int = 0,
    val surname: String = "",
    val name: String = "",
    val lastname: String = "",
    val sex: Boolean = true,
    val dateOfBirthInMilliseconds: Long? = null,
    val displayName: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val avatarPath: String = ""
)
