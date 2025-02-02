package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples

data class PeoplesUseCase(
    val deletePeopleOperator: DeletePeopleOperator,
    val getPeoplesOperator: GetPeoplesOperator,
    val getPeopleByIdOperator: GetPeopleByIdOperator,
    val insertPeopleOperator: InsertPeopleOperator,
    val insertPeoplesOperator: InsertPeoplesOperator,
    val updatePeopleOperator: UpdatePeopleOperator
)
