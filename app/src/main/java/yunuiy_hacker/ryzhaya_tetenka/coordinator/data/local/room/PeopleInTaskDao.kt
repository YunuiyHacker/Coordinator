package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PeopleInTask

@Dao
interface PeopleInTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(peopleInTask: PeopleInTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun allUpsert(peoplesInTask: List<PeopleInTask>)

    @Delete
    suspend fun delete(peopleInTask: PeopleInTask)

    @Delete
    suspend fun allDelete(peoplesInTasks: List<PeopleInTask>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(peopleInTask: PeopleInTask)

    @Query("SELECT * FROM peoples_in_tasks")
    suspend fun getPeoplesInTasks(): List<PeopleInTask>

    @Query("SELECT * FROM peoples_in_tasks WHERE task_id=:taskId")
    suspend fun getPeoplesInTasksById(taskId: Int): List<PeopleInTask>
}