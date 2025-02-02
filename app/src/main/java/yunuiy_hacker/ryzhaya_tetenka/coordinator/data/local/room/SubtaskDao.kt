package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Subtask

@Dao
interface SubtaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(subtask: Subtask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun allUpsert(subtasks: List<Subtask>)

    @Delete
    suspend fun delete(subtask: Subtask)

    @Delete
    suspend fun allDelete(subtasks: List<Subtask>)

    @Update(entity = Subtask::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(subtask: Subtask)

    @Update(entity = Subtask::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun allUpdate(subtasks: List<Subtask>)

    @Query("SELECT * FROM subtasks")
    suspend fun getSubtasks(): List<Subtask>

    @Query("SELECT * FROM subtasks WHERE task_id=:taskId")
    suspend fun getSubtasksByTaskId(taskId: Int): List<Subtask>
}