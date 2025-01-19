package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks

data class SubtasksUseCase(
    val deleteSubtaskOperator: DeleteSubtaskOperator,
    val getSubtasksByTaskIdOperator: GetSubtasksByTaskIdOperator,
    val getSubtasksOperator: GetSubtasksOperator,
    val insertSubtaskOperator: InsertSubtaskOperator,
    val insertSubtasksOperator: InsertSubtasksOperator,
    val updateSubtaskOperator: UpdateSubtaskOperator
)
