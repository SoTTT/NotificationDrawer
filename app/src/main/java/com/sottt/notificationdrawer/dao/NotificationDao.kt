package com.sottt.notificationdrawer.dao

import androidx.room.*
import com.sottt.notificationdrawer.data.defined.NotificationInfo


@Dao
interface NotificationDao {

    @Insert
    fun insertNotification(notification: NotificationInfo): Long

    @Update
    fun updateNotification(newNotification: NotificationInfo)

    @Query("select * from NotificationInfo")
    fun selectAllNotificationRecord(): List<NotificationInfo>

    @Delete
    fun deleteNotification(notification: NotificationInfo)

    @Query("select count(*) from NotificationInfo")
    fun count(): Int

    @Query("select * from NotificationInfo where packageName = :packageName")
    fun selectWithPackageName(packageName: String): List<NotificationInfo>

    @Query("select distinct packageName from NotificationInfo")
    fun selectAllPackageName(): List<String>

    @Query("select packageName as name,count(*) as count from NotificationInfo group by packageName")
    fun selectAllPackageNameAndCount(): List<Repository.PackageNameAndCount>

    @Query("select `key` from NotificationInfo")
    fun selectAllKey(): List<String>

}