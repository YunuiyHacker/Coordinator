package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PlaceInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.PlaceInTaskDao

class DeletePlaceInTaskOperator(private val placeInTaskDao: PlaceInTaskDao) {
    suspend operator fun invoke(placeInTask: PlaceInTask) {
        placeInTaskDao.delete(placeInTask)
    }
}