package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks

data class TasksUseCase(
    val deleteTaskOperator: DeleteTaskOperator,
    val getTaskByIdOperator: GetTaskByIdOperator,
    val getTasksByLikeQueryOperator: GetTasksByLikeQueryOperator,
    val getTasksByTimeTypeIdDateAndCategoryIdOperator: GetTasksByTimeTypeIdDateAndCategoryIdOperator,
    val getTasksOperator: GetTasksOperator,
    val insertTaskOperator: InsertTaskOperator,
    val insertTasksOperator: InsertTasksOperator,
    val updateTaskOperator: UpdateTaskOperator,
    val getAllCompletedTasksCount: GetAllCompletedTasksCount,
    val getAllNotCompletedTasksCount: GetAllNotCompletedTasksCount
)
