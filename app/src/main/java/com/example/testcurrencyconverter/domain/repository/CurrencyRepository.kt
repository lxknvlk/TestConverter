package com.example.testcurrencyconverter.domain.repository

import androidx.lifecycle.LiveData
import com.example.testcurrencyconverter.domain.entity.CurrencyEntity

interface CurrencyRepository{

    suspend fun updateRates()

    fun getRates():  LiveData<List<CurrencyEntity>>
    fun getRatesSync():  List<CurrencyEntity>
}