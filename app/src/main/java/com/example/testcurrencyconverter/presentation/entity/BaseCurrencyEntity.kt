package com.example.testcurrencyconverter.presentation.entity

import com.example.testcurrencyconverter.domain.entity.CurrencyType

class BaseCurrencyEntity (
    val currency: CurrencyType,
    var value: Double
){
    override fun toString(): String {
        return "$currency $value"
    }
}