package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks

data class PlacesInTasksUseCase(
    val deletePlaceInTaskOperator: DeletePlaceInTaskOperator,
    val getPlacesInTaskByTaskId: GetPlaceInTaskByTaskIdOperator,
    val getPlacesInTasksOperator: GetPlacesInTasksOperator,
    val insertPlaceInTaskOperator: InsertPlaceInTaskOperator,
    val insertPlacesInTasksOperator: InsertPlacesInTasksOperator,
    val updatePlaceInTaskOperator: UpdatePlaceInTaskOperator
)