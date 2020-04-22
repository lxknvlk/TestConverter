package com.example.testcurrencyconverter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testcurrencyconverter.data.entity.CurrencyData

@Database(entities = [
    CurrencyData::class],
    version = 1,
    exportSchema = false)
@TypeConverters(com.example.testcurrencyconverter.data.database.TypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}