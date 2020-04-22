package com.example.testcurrencyconverter.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.testcurrencyconverter.domain.entity.CurrencyType

@Entity(tableName = "currency",
        primaryKeys = ["currency"])
data class CurrencyData(
    @ColumnInfo(name = "rates")
    val rates: Map<String, Double>,

    @ColumnInfo(name = "currency")
    val currency: CurrencyType
)