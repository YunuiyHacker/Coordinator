package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.CategoryDao

class InsertCategoriesOperator(private val categoryDao: CategoryDao) {
    suspend operator fun invoke(categories: List<Category>) {
        categoryDao.allUpsert(categories)
    }
}