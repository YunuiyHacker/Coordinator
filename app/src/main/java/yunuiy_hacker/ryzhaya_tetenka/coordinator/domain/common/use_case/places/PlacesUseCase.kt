package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places

data class PlacesUseCase(
    val deletePlaceOperator: DeletePlaceOperator,
    val getPlaceByIdOperator: GetPlaceByIdOperator,
    val getPlacesOperator: GetPlacesOperator,
    val insertPlaceOperator: InsertPlaceOperator,
    val insertPlacesOperator: InsertPlacesOperator,
    val updatePlaceOperator: UpdatePlaceOperator
)
