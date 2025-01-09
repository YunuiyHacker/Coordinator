package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "time_type_id") val timeTypeId: Int? = 0,
    @ColumnInfo(name = "category_id") val categoryId: Int? = 0,
    @ColumnInfo(name = "dateInMilliseconds") val dateInMilliseconds: Long? = 0L,
    @ColumnInfo(name = "hour") val hour: Int? = 0,
    @ColumnInfo(name = "minute") val minute: Int? = 0,
    @ColumnInfo(name = "withEndTime") val withEndTime: Boolean? = false,
    @ColumnInfo(name = "endHour") val endHour: Int? = 0,
    @ColumnInfo(name = "endMinute") val endMinute: Int? = 0,
    @ColumnInfo(name = "title") val title: String? = "",
    @ColumnInfo(name = "content") val content: String? = "",
    @ColumnInfo(name = "checked") val checked: Boolean? = false
)