package com.example.testcurrencyconverter.presentation.entity

import com.example.testcurrencyconverter.domain.entity.CurrencyType

data class CurrencyAdapterEntity(
    // ALEX_Z: зачем тут base?
    val baseCurrency: BaseCurrencyEntity,
    val currency: CurrencyType,
    // ALEX_Z: зачем тут rate?
    val rateToBase: Double,
    var value: Double
) {
    override fun toString(): String {
        return "$currency $value"
    }
}