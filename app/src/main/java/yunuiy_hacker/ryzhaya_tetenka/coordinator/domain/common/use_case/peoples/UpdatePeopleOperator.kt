package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.PeopleDao

class UpdatePeopleOperator(private val peopleDao: PeopleDao) {
    suspend operator fun invoke(people: People) {
        peopleDao.update(people)
    }
}