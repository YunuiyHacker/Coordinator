package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Place

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(place: Place)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun allUpsert(places: List<Place>)

    @Delete
    suspend fun delete(place: Place)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(place: Place)

    @Query("SELECT * FROM places")
    suspend fun getPlaces(): List<Place>

    @Query("SELECT * FROM places WHERE id=:id")
    suspend fun getPlaceById(id: Int): Place
}