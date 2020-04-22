package com.example.testcurrencyconverter.data.dagger

import android.app.Application
import com.example.testcurrencyconverter.data.database.AppDatabase
import com.example.testcurrencyconverter.data.database.CurrencyDao
import com.example.testcurrencyconverter.data.mapper.ApiResponseEntityMapper
import com.example.testcurrencyconverter.data.mapper.CurrencyDataEntityMapper
import com.example.testcurrencyconverter.data.mapper.CurrencyEntityDataMapper
import com.example.testcurrencyconverter.data.repository.CurrencyRepositoryImpl
import com.example.testcurrencyconverter.data.servercall.ApiCallExecutorImpl
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(modules = [ProvidersModule::class, DatabaseModule::class])
interface ApplicationGraph {
    fun apiCallExecutor(): ApiCallExecutorImpl
    fun currencyRepository(): CurrencyRepositoryImpl
}

@Module
class ProvidersModule {
    @Provides @Singleton fun okHttpClient(): OkHttpClient = OkHttpClient()
    @Provides fun apiResponseEntityMapper(): ApiResponseEntityMapper = ApiResponseEntityMapper()
    @Provides fun currencyDataEntityMapper(): CurrencyDataEntityMapper = CurrencyDataEntityMapper()
    @Provides fun currencyEntityDataMapper(): CurrencyEntityDataMapper = CurrencyEntityDataMapper()
}

@Module
class DatabaseModule(private val application: Application) {
    @Provides @Singleton fun database(): AppDatabase {
        return AppDatabase.getDatabase(application)
    }

    @Provides @Singleton fun currencyDao(): CurrencyDao {
        return database().currencyDao()
    }
}