package com.example.testcurrencyconverter.data.database

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.testcurrencyconverter.data.entity.CurrencyData

@Dao
abstract class CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(currencyData: CurrencyData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(currencyData: CurrencyData)

    @Delete
    abstract fun delete(currencyData: CurrencyData)

    fun upsert(currencyData: CurrencyData){
        try{
            insert(currencyData)
        } catch (e: SQLiteConstraintException){
            update(currencyData)
        }
    }

    @Query("select * from rates where base = :base_currency")
    abstract fun getRatesForBase(base_currency: String): LiveData<List<CurrencyData>>

    @Query("select * from rates where base = :base_currency")
    abstract fun getRatesForBaseSync(base_currency: String): List<CurrencyData>

    @Query("select * from rates")
    abstract fun get(): LiveData<List<CurrencyData>>

    @Query("select * from rates")
    abstract fun getSync(): List<CurrencyData>
}