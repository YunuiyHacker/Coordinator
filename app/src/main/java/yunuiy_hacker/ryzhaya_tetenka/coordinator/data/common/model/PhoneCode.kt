package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phone_codes")
data class PhoneCode(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "iso_code") val ISOCode: String? = "",
    @ColumnInfo(name = "phone_code") val phoneCode: Int? = 0
)
