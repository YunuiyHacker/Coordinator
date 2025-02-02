package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.People

@Dao
interface PeopleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(people: People)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun allUpsert(peoples: List<People>)

    @Delete
    suspend fun delete(people: People)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(people: People)

    @Query("SELECT * FROM peoples")
    suspend fun getPeoples(): List<People>

    @Query("SELECT * FROM peoples WHERE id=:id")
    suspend fun getPeopleById(id: Int): People
}