package com.sottt.notificationdrawer.DAO

import androidx.room.*
import com.sottt.notificationdrawer.data.defined.NotificationInfo


@Dao
interface NotificationDao {

    @Insert
    fun insertNotification(notification: NotificationInfo): Long

    @Update
    fun updateNotification(newNotification: NotificationInfo)

    @Query("select * from NotificationInfo")
    fun loadAllNotificationRecord(): List<NotificationInfo>

    @Delete
    fun deleteNotification(notification: NotificationInfo)


}