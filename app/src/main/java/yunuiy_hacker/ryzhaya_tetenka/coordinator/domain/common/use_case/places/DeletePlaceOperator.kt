package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.PlaceDao

class DeletePlaceOperator(private val placeDao: PlaceDao) {
    suspend operator fun invoke(place: Place) {
        placeDao.delete(place)
    }
}