package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples_in_tasks

data class PeoplesInTasksUseCase(
    val deletePeopleInTaskOperator: DeletePeopleInTaskOperator,
    val deletePeoplesInTasksOperator: DeletePeoplesInTasksOperator,
    val getPeoplesInTasksByTaskIdOperator: GetPeoplesInTasksByTaskIdOperator,
    val getPeoplesInTasksOperator: GetPeoplesInTasksOperator,
    val insertPeopleInTaskOperator: InsertPeopleInTaskOperator,
    val insertPeoplesInTasksOperator: InsertPeoplesInTasksOperator,
    val updatePeopleInTaskOperator: UpdatePeopleInTaskOperator
)
