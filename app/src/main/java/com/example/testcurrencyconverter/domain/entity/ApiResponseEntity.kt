package com.example.testcurrencyconverter.domain.entity

data class ApiResponseEntity(
    var currency: CurrencyType,
    var rates: Map<String, Double>
)