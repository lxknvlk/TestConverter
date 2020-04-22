package com.example.testcurrencyconverter.presentation.entity

import com.example.testcurrencyconverter.domain.entity.CurrencyType


// ALEX_Z: зачем нужен этот класс?
class BaseCurrencyEntity (
    val currency: CurrencyType,
    // ALEX_Z: зачем тут значение? CurrencyAdapterEntity уже выполняет эту функцию
    var value: Double
){
    override fun toString(): String {
        return "$currency $value"
    }
}