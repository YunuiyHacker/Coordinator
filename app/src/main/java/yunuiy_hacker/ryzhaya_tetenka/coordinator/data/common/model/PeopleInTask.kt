package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "peoples_in_tasks")
data class PeopleInTask(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "people_id") val peopleId: Int? = 0,
    @ColumnInfo(name = "task_id") val taskId: Int? = 0
)
