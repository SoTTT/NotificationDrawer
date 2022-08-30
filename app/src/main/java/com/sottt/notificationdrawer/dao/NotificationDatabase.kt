package com.sottt.notificationdrawer.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sottt.notificationdrawer.data.defined.NotificationInfo

@Database(version = 1, entities = [NotificationInfo::class], exportSchema = false)
abstract class NotificationDatabase : RoomDatabase() {

    abstract fun NotificationDao(): NotificationDao

    companion object {

        private var instance: NotificationDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): NotificationDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                NotificationDatabase::class.java,
                "app_database"
            ).build().apply {
                instance = this
            }
        }
    }

}