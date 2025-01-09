package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.CategoryDao

class GetCategoryByIdOperator(private val categoryDao: CategoryDao) {
    suspend operator fun invoke(id: Int): Category {
        return categoryDao.getCategoryById(id)
    }
}