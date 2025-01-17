package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao

class InsertTasksOperator(private val taskDao: TaskDao) {
    suspend operator fun invoke(tasks: List<Task>): List<Long> {
        return taskDao.allUpsert(tasks)
    }
}