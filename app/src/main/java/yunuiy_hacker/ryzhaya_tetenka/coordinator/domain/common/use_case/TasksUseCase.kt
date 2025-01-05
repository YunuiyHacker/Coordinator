package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case

data class TasksUseCase(
    val deleteTaskOperator: DeleteTaskOperator,
    val getTaskByIdOperator: GetTaskByIdOperator,
    val getTasksByLikeQueryOperator: GetTasksByLikeQueryOperator,
    val getTasksByTimeTypeIdAndDateOperator: GetTasksByTimeTypeIdAndDateOperator,
    val getTasksOperator: GetTasksOperator,
    val insertTaskOperator: InsertTaskOperator,
    val updateTaskOperator: UpdateTaskOperator
)
