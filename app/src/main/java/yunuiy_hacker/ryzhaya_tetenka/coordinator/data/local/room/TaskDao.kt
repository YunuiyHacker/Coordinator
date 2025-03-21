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
    suspend fun upsert(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun allUpsert(tasks: List<Task>): List<Long>

    @Delete
    suspend fun delete(task: Task)

    @Update(entity = Task::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(task: Task)

    @Query("SELECT * FROM tasks")
    suspend fun getTasks(): List<Task>

    @Query("SELECT * FROM tasks WHERE id=:id")
    suspend fun getTaskById(id: Int): Task

    @Query("SELECT * FROM tasks WHERE time_type_id=0 AND strftime('%d.%m.%Y', DATE(date_in_milliseconds / 1000, 'unixepoch')) = strftime('%d.%m.%Y', DATE(:dateInMilliseconds / 1000, 'unixepoch')) ORDER BY hour, minute ASC")
    fun getTasksByDate(dateInMilliseconds: Long): List<Task>

    @Query("SELECT * FROM tasks WHERE time_type_id=0 AND (strftime('%d.%m.%Y', DATE(date_in_milliseconds / 1000, 'unixepoch')) = strftime('%d.%m.%Y', DATE(:dateInMilliseconds / 1000, 'unixepoch'))) AND category_id = :categoryId ORDER BY hour, minute ASC")
    fun getTasksByDateAndCategoryId(dateInMilliseconds: Long, categoryId: Int): List<Task>

    @Query("SELECT * FROM tasks WHERE time_type_id=1 AND (DATE(date_in_milliseconds / 1000, 'unixepoch') BETWEEN DATE(:weekStartInMilliseconds / 1000, 'unixepoch') AND DATE(:weekEndInMilliseconds / 1000, 'unixepoch'))")
    fun getTasksByWeek(
        weekStartInMilliseconds: Long, weekEndInMilliseconds: Long
    ): List<Task>

    @Query("SELECT * FROM tasks WHERE time_type_id=1 AND (DATE(date_in_milliseconds / 1000, 'unixepoch') BETWEEN DATE(:weekStartInMilliseconds / 1000, 'unixepoch') AND DATE(:weekEndInMilliseconds / 1000, 'unixepoch')) AND category_id = :categoryId")
    fun getTasksByWeekAndCategoryId(
        weekStartInMilliseconds: Long, weekEndInMilliseconds: Long, categoryId: Int
    ): List<Task>

    @Query("SELECT * FROM tasks WHERE time_type_id=2 AND strftime('%m.%Y', DATE(date_in_milliseconds / 1000, 'unixepoch')) = strftime('%m.%Y', DATE(:dateInMilliseconds / 1000, 'unixepoch'))")
    fun getTasksByMonth(dateInMilliseconds: Long): List<Task>

    @Query("SELECT * FROM tasks WHERE time_type_id=2 AND (strftime('%m.%Y', DATE(date_in_milliseconds / 1000, 'unixepoch')) = strftime('%m.%Y', DATE(:dateInMilliseconds / 1000, 'unixepoch'))) AND category_id = :categoryId")
    fun getTasksByMonthAndCategoryId(dateInMilliseconds: Long, categoryId: Int): List<Task>

    @Query("SELECT * FROM tasks WHERE time_type_id=3 AND strftime('%Y', DATE(date_in_milliseconds / 1000, 'unixepoch')) = strftime('%Y', DATE(:dateInMilliseconds / 1000, 'unixepoch'))")
    fun getTasksByYear(dateInMilliseconds: Long): List<Task>

    @Query("SELECT * FROM tasks WHERE time_type_id=3 AND (strftime('%Y', DATE(date_in_milliseconds / 1000, 'unixepoch')) = strftime('%Y', DATE(:dateInMilliseconds / 1000, 'unixepoch'))) AND category_id = :categoryId")
    fun getTasksByYearAndCategoryId(dateInMilliseconds: Long, categoryId: Int): List<Task>

    @Query("SELECT * FROM tasks WHERE time_type_id=4")
    fun getTasksByLife(): List<Task>

    @Query("SELECT * FROM tasks WHERE time_type_id=4 AND category_id = :categoryId")
    fun getTasksByLifeAndCategoryId(categoryId: Int): List<Task>

    @Query("SELECT * FROM tasks WHERE (LOWER(title) LIKE LOWER(:query) || '%' OR + LOWER(content) LIKE LOWER(:query) || '%') OR (LOWER(title) LIKE '%' || LOWER(:query) OR + LOWER(content) LIKE '%' || LOWER(:query)) OR (LOWER(title) LIKE '%' || LOWER(:query) || '%' OR + LOWER(content) LIKE '%' || LOWER(:query) || '%')")
    fun getTasksByLikeQuery(query: String): List<Task>

    @Query("SELECT COUNT(CASE WHEN checked THEN 1 END) AS count FROM tasks")
    fun getAllCompletedTasksCount(): Int

    @Query("SELECT COUNT(CASE WHEN NOT checked THEN 1 END) AS count FROM tasks")
    fun getAllNotCompletedTasksCount(): Int
}