package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places_in_tasks")
data class PlaceInTask(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "place_id") val placeId: Int = 0,
    @ColumnInfo(name = "task_id") val taskId: Int = 0
)