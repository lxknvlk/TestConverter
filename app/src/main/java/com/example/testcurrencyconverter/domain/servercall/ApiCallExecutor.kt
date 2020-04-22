package com.example.testcurrencyconverter.domain.servercall

import com.example.testcurrencyconverter.domain.entity.ApiResponseEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyType

interface ApiCallExecutor {
    suspend fun getRates(baseCurrency: CurrencyType): ApiResponseEntity?
}