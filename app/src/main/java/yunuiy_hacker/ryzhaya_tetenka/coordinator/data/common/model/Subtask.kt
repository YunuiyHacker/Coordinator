package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtasks")
data class Subtask(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "task_id") val taskId: Int? = 0,
    @ColumnInfo(name = "title") val title: String? = "",
    @ColumnInfo(name = "checked") val checked: Boolean? = false
)
