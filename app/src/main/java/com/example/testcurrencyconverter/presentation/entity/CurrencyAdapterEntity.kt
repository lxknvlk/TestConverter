package com.example.testcurrencyconverter.presentation.entity

import com.example.testcurrencyconverter.domain.entity.CurrencyType

data class CurrencyAdapterEntity(
    val baseCurrency: BaseCurrencyEntity,
    val currency: CurrencyType,
    val rateToBase: Double,
    var value: Double
) {
    override fun toString(): String {
        return "$currency $value"
    }
}