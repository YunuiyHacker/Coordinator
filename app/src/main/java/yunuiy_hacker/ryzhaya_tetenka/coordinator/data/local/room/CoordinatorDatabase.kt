package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task

@Database(entities = [Task::class], version = 1)
abstract class CoordinatorDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}