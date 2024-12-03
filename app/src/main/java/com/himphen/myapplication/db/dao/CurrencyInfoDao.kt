package com.himphen.myapplication.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.himphen.myapplication.db.entity.CurrencyInfoEntity
import kotlinx.coroutines.flow.Flow

const val tableName = "table_name"

@Dao
interface CurrencyInfoDao {
    @Transaction
    @Query("SELECT * FROM $tableName")
    fun getAll(): Flow<List<CurrencyInfoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(entity: List<CurrencyInfoEntity>)

    @Query("DELETE FROM $tableName")
    suspend fun clearAll()
}
