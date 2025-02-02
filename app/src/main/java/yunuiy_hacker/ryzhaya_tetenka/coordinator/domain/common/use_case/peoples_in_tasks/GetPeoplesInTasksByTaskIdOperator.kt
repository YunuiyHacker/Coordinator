package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples_in_tasks

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PeopleInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.PeopleInTaskDao

class GetPeoplesInTasksByTaskIdOperator(private val peopleInTaskDao: PeopleInTaskDao) {
    suspend operator fun invoke(taskId: Int): List<PeopleInTask> {
        return peopleInTaskDao.getPeoplesInTasksById(taskId)
    }
}