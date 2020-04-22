package com.example.testcurrencyconverter.domain.entity

data class CurrencyEntity(
    var currency: CurrencyType,
    var rates: Map<String, Double>
)