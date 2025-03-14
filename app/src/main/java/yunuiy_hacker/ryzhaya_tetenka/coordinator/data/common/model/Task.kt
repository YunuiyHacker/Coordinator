package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "time_type_id") val timeTypeId: Int? = 0,
    @ColumnInfo(name = "category_id") val categoryId: Int? = 0,
    @ColumnInfo(name = "date_in_milliseconds") val dateInMilliseconds: Long? = 0L,
    @ColumnInfo(name = "hour") val hour: Int? = 0,
    @ColumnInfo(name = "minute") val minute: Int? = 0,
    @ColumnInfo(name = "with_end_time") val withEndTime: Boolean? = false,
    @ColumnInfo(name = "end_hour") val endHour: Int? = 0,
    @ColumnInfo(name = "end_minute") val endMinute: Int? = 0,
    @ColumnInfo(name = "title") val title: String? = "",
    @ColumnInfo(name = "content") val content: String? = "",
    @ColumnInfo(name = "checked") val checked: Boolean? = false,
    @ColumnInfo(name = "notify") val notify: Boolean? = false,
    @ColumnInfo(name = "notify_date_in_milliseconds") val notifyDateInMilliseconds: Long? = 0L,
    @ColumnInfo(name = "notify_hour") val notifyHour: Int? = 0,
    @ColumnInfo(name = "notify_minute") val notifyMinute: Int? = 0,
    @ColumnInfo(name = "priority_code") val priorityCode: Int? = 0
)