package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task

@Database(entities = [Task::class, Category::class, Subtask::class], version = 6)
abstract class CoordinatorDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val categoryDao: CategoryDao
    abstract val subtaskDao: SubtaskDao
}