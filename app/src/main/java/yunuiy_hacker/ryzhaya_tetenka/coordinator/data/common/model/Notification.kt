package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "task_id") val taskId: Int? = 0,
    @ColumnInfo(name = "tag") val tag: String? = "",
    @ColumnInfo(name = "is_done") val isDone: Boolean? = false
)
