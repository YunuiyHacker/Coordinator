package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.SubtaskDao

class GetSubtasksOperator(private val subtaskDao: SubtaskDao) {
    suspend operator fun invoke(): List<Subtask> {
        return subtaskDao.getSubtasks()
    }
}