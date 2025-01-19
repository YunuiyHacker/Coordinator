package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PlaceInTask

@Dao
interface PlaceInTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(placeInTask: PlaceInTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun allUpsert(placesInTask: List<PlaceInTask>)

    @Delete
    suspend fun delete(placeInTask: PlaceInTask)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(placeInTask: PlaceInTask)

    @Query("SELECT * FROM places_in_tasks")
    suspend fun getPlacesInTasks(): List<PlaceInTask>

    @Query("SELECT * FROM places_in_tasks WHERE task_id=:taskId")
    suspend fun getPlaceInTaskByTaskId(taskId: Int): PlaceInTask?
}