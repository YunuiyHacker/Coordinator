package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao

class GetTasksByLikeQueryOperator(private val taskDao: TaskDao) {
    suspend operator fun invoke(query: String): List<Task> {
        return taskDao.getTasksByLikeQuery(query)
    }
}