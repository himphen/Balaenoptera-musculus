package com.himphen.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.himphen.myapplication.db.dao.CurrencyInfoDao
import com.himphen.myapplication.db.entity.CurrencyInfoEntity

private const val DATABASE_NAME = "database_name"

@Database(
    entities = [
        CurrencyInfoEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun currencyInfoDao(): CurrencyInfoDao

    companion object {

        fun getInstance(context: Context): LocalDatabase {
            val dbBuilder = Room.databaseBuilder(
                context,
                LocalDatabase::class.java,
                DATABASE_NAME
            )
            dbBuilder.fallbackToDestructiveMigration()
            return dbBuilder.build()
        }
    }
}