package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Notification

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun upsert(notification: Notification)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun allUpsert(notifications: List<Notification>)

    @Delete
    suspend fun delete(notification: Notification)

    @Update(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun update(notification: Notification)

    @Query("SELECT * FROM notifications")
    suspend fun getNotifications(): List<Notification>

    @Query("SELECT * FROM notifications WHERE id=:id")
    suspend fun getNotificationById(id: Int): Notification

    @Query("SELECT * FROM notifications WHERE tag=:tag")
    suspend fun getNotificationByTag(tag: String): Notification

    @Query("SELECT * FROM notifications WHERE task_id=:taskId")
    suspend fun getNotificationByTaskId(taskId: Int): Notification
}