package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao

class GetAllNotCompletedTasksCount(private val taskDao: TaskDao) {
    operator fun invoke(): Int {
        return taskDao.getAllNotCompletedTasksCount()
    }
}