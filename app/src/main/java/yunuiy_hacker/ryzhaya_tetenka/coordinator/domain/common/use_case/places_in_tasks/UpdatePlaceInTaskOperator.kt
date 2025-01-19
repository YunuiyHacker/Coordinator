package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PlaceInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.PlaceInTaskDao

class UpdatePlaceInTaskOperator(private val placeInTaskDao: PlaceInTaskDao) {
    suspend operator fun invoke(placeInTask: PlaceInTask) {
        placeInTaskDao.update(placeInTask)
    }
}