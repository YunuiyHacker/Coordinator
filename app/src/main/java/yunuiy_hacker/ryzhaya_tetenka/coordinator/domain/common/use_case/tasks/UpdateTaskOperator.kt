package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao

class UpdateTaskOperator(private val taskDao: TaskDao) {
    suspend operator fun invoke(task: Task) {
        taskDao.update(task)
    }
}