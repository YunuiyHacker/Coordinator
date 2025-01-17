package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories

data class CategoriesUseCase(
    val deleteCategoryOperator: DeleteCategoryOperator,
    val getCategoriesOperator: GetCategoriesOperator,
    val getCategoryByIdOperator: GetCategoryByIdOperator,
    val insertCategoryOperator: InsertCategoryOperator,
    val insertCategoriesOperator: InsertCategoriesOperator,
    val updateCategoryOperator: UpdateCategoryOperator
)
