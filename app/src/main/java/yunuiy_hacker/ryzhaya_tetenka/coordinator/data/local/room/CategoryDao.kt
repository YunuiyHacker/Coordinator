package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun allUpsert(categories: List<Category>): List<Long>

    @Delete
    suspend fun delete(category: Category)

    @Update(entity = Category::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(category: Category)

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<Category>

    @Query("SELECT * FROM categories WHERE id=:id")
    suspend fun getCategoryById(id: Int): Category

}