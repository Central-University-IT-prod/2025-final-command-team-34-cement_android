package com.itblaze.mentor.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.itblaze.mentor.data.db.dao.RequestsDAO
import com.itblaze.mentor.data.models.db.RequestItem

@Database(
    entities = [RequestItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MainDB : RoomDatabase() {
    abstract fun requestsDAO(): RequestsDAO

    companion object {
        @Volatile
        private var INSTANCE: MainDB? = null

        fun getInstance(context: Context): MainDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDB::class.java,
                    "main_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}