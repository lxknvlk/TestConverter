package com.example.testcurrencyconverter.domain.entity

data class CurrencyEntity(
    val base: CurrencyType,
    val other: CurrencyType,
    val value: Double
)