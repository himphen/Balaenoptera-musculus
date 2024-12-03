package com.himphen.myapplication.db.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.himphen.myapplication.db.dao.tableName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = tableName)
data class CurrencyInfoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "code")
    val code: String? = null,
) : Parcelable
