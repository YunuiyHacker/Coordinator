package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao

class GetTasksOperator(private val taskDao: TaskDao) {
    suspend operator fun invoke(): List<Task> {
        return taskDao.getTasks()
    }
}