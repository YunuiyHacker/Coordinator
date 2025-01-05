package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update(entity = Task::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(task: Task)

    @Query("SELECT * FROM task")
    suspend fun getTasks(): List<Task>

    @Query("SELECT * FROM task WHERE id=:id")
    suspend fun getTaskById(id: Int): Task

    @Query("SELECT * FROM task WHERE time_type_id=0 AND strftime('%d.%m.%Y', DATE(dateInMilliseconds / 1000, 'unixepoch')) = strftime('%d.%m.%Y', DATE(:dateInMilliseconds / 1000, 'unixepoch')) ORDER BY hour, minute ASC")
    fun getTasksByDate(dateInMilliseconds: Long): List<Task>

    @Query("SELECT * FROM task WHERE time_type_id=1 AND (DATE(dateInMilliseconds / 1000, 'unixepoch') BETWEEN DATE(:weekStartInMilliseconds / 1000, 'unixepoch') AND DATE(:weekEndInMilliseconds / 1000, 'unixepoch'))")
    fun getTasksByWeek(
        weekStartInMilliseconds: Long, weekEndInMilliseconds: Long
    ): List<Task>

    @Query("SELECT * FROM task WHERE time_type_id=2 AND strftime('%m.%Y', DATE(dateInMilliseconds / 1000, 'unixepoch')) = strftime('%m.%Y', DATE(:dateInMilliseconds / 1000, 'unixepoch'))")
    fun getTasksByMonth(dateInMilliseconds: Long): List<Task>

    @Query("SELECT * FROM task WHERE time_type_id=3 AND strftime('%Y', DATE(dateInMilliseconds / 1000, 'unixepoch')) = strftime('%Y', DATE(:dateInMilliseconds / 1000, 'unixepoch'))")
    fun getTasksByYear(dateInMilliseconds: Long): List<Task>

    @Query("SELECT * FROM task WHERE time_type_id=4")
    fun getTasksByLife(): List<Task>

    @Query("SELECT * FROM task WHERE (LOWER(title) LIKE LOWER(:query) || '%' OR + LOWER(content) LIKE LOWER(:query) || '%') OR (LOWER(title) LIKE '%' || LOWER(:query) OR + LOWER(content) LIKE '%' || LOWER(:query)) OR (LOWER(title) LIKE '%' || LOWER(:query) || '%' OR + LOWER(content) LIKE '%' || LOWER(:query) || '%')")
    fun getTasksByLikeQuery(query: String): List<Task>
}