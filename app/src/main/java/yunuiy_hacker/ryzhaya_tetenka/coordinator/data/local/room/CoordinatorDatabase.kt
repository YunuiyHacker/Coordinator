package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Notification
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PeopleInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PlaceInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task

@Database(
    entities = [Task::class, Category::class, Subtask::class, Place::class, PlaceInTask::class, People::class, PeopleInTask::class, Notification::class],
    version = 7
)
abstract class CoordinatorDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val categoryDao: CategoryDao
    abstract val subtaskDao: SubtaskDao
    abstract val placeDao: PlaceDao
    abstract val placeInTaskDao: PlaceInTaskDao
    abstract val peopleDao: PeopleDao
    abstract val peopleInTaskDao: PeopleInTaskDao
    abstract val notificationDao: NotificationDao
}