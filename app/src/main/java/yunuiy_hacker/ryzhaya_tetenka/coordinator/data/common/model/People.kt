package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "peoples")
data class People(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "surname") val surname: String? = "",
    @ColumnInfo(name = "name") val name: String? = "",
    @ColumnInfo(name = "lastname") val lastname: String? = "",
    @ColumnInfo(name = "sex") val sex: Boolean? = true,
    @ColumnInfo(name = "date_of_birth_in_milliseconds") val dateOfBirthInMilliseconds: Long? = 0L,
    @ColumnInfo(name = "display_name") val displayName: String? = "",
    @ColumnInfo(name = "address") val address: String? = "",
    @ColumnInfo(name = "avatar_path") val avatarPath: String? = ""
)
