package com.example.testcurrencyconverter.domain.repository

import androidx.lifecycle.LiveData
import com.example.testcurrencyconverter.domain.entity.ApiResponseEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyType

interface CurrencyRepository{

    suspend fun updateRates()

    fun getRates():  LiveData<List<CurrencyEntity>>
    fun getRatesSync():  List<CurrencyEntity>
    fun getRatesForBase(baseCurrency: CurrencyType):  LiveData<List<CurrencyEntity>>
}