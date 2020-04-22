package com.example.testcurrencyconverter.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.testcurrencyconverter.data.database.CurrencyDao
import com.example.testcurrencyconverter.data.mapper.CurrencyDataEntityMapper
import com.example.testcurrencyconverter.data.mapper.CurrencyEntityDataMapper
import com.example.testcurrencyconverter.data.servercall.ApiCallExecutorImpl
import com.example.testcurrencyconverter.domain.entity.CurrencyEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyType
import com.example.testcurrencyconverter.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val currencyEntityDataMapper: CurrencyEntityDataMapper,
    private val currencyDataEntityMapper: CurrencyDataEntityMapper,
    private val apiCallExecutor: ApiCallExecutorImpl
): CurrencyRepository {
    override suspend fun updateRates() {
        //TODO move this to provider + dagger
        val supportedCurrencies = listOf<CurrencyType>(
            CurrencyType.EUR,
            CurrencyType.USD,
            CurrencyType.RUB,
            CurrencyType.GBP
        )

        supportedCurrencies.forEach {
            GlobalScope.launch(Dispatchers.IO){
                val ratesEntity = apiCallExecutor.getRates(it)
                if (ratesEntity != null) currencyDao.upsert(currencyEntityDataMapper.mapFrom(ratesEntity))
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
}