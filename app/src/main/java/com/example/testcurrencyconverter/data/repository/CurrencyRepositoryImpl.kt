package com.example.testcurrencyconverter.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.example.testcurrencyconverter.data.database.CurrencyDao
import com.example.testcurrencyconverter.data.mapper.CurrencyDataEntityMapper
import com.example.testcurrencyconverter.data.mapper.CurrencyEntityDataMapper
import com.example.testcurrencyconverter.data.servercall.ApiCallExecutorImpl
import com.example.testcurrencyconverter.domain.entity.ApiResponseEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyType
import com.example.testcurrencyconverter.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyDao: CurrencyDao,
    // ALEX_Z: почему передается конкретная реализация интерфейса для всех параметров?
    private val currencyEntityDataMapper: CurrencyEntityDataMapper,
    private val currencyDataEntityMapper: CurrencyDataEntityMapper,
    private val apiCallExecutor: ApiCallExecutorImpl
): CurrencyRepository {
    override suspend fun updateRates() {
        //TODO move this to provider
        val supportedCurrencies = listOf<CurrencyType>(
            CurrencyType.EUR,
            CurrencyType.USD,
            CurrencyType.RUB,
            CurrencyType.GBP
        )

        // ALEX_Z: зачем вызов каждого значения внутри корутины? Memory leak
        // - fixed
        GlobalScope.launch(Dispatchers.IO){
            val currencyRatesList = mutableListOf<CurrencyEntity>()

            supportedCurrencies.forEach { currencyType ->

                val ratesEntity: ApiResponseEntity? = apiCallExecutor.getRates(currencyType)
                ratesEntity?.let { currencyEntity ->
                    supportedCurrencies.forEach {
                        if (it != currencyEntity.currency){
                            val value = currencyEntity.rates[it.toString()]?.toDouble() ?: 0.0
                            currencyRatesList.add(CurrencyEntity(currencyEntity.currency, it, value))
                        }
                    }
                }
            }

            currencyRatesList.forEach {
                currencyDao.upsert(currencyEntityDataMapper.mapFrom(it))
            }
        }
    }

    override fun getRates(): LiveData<List<CurrencyEntity>> {
        return Transformations.map(currencyDao.get()) { currencyRateDataList ->
            val currencyEntityList = currencyRateDataList.map {
                currencyDataEntityMapper.mapFrom(it)
            }

            currencyEntityList
        }
    }

    override fun getRatesSync(): List<CurrencyEntity> {
        val currencyRateDataList = currencyDao.getSync()

        return currencyRateDataList.map {
            currencyDataEntityMapper.mapFrom(it)
        }
    }

    override fun getRatesForBase(baseCurrency: CurrencyType): LiveData<List<CurrencyEntity>> {
        return Transformations.map(currencyDao.getRatesForBase(baseCurrency.toString())){ currencyRateDataList ->
            val currencyEntityList = currencyRateDataList.map {
                currencyDataEntityMapper.mapFrom(it)
            }

            currencyEntityList
        }
    }

    override suspend fun getRatesForBaseSync(baseCurrency: CurrencyType): List<CurrencyEntity> {
        return currencyDao.getRatesForBaseSync(baseCurrency.toString()).map {
            currencyDataEntityMapper.mapFrom(it)
        }
    }
}