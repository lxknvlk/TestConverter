package com.example.testcurrencyconverter.domain.servercall

import com.example.testcurrencyconverter.domain.entity.CurrencyEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyType

interface ApiCallExecutor {
    suspend fun getRates(baseCurrency: CurrencyType): CurrencyEntity?
}