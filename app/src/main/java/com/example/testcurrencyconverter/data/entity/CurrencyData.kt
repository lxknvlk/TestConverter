package com.example.testcurrencyconverter.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.testcurrencyconverter.domain.entity.CurrencyType

@Entity(tableName = "rates",
        primaryKeys = ["base", "other"])
data class CurrencyData(
    @ColumnInfo(name = "base")
    val base: CurrencyType,
    @ColumnInfo(name = "other")
    val other: CurrencyType,
    @ColumnInfo(name = "value")
    val value: Double
)