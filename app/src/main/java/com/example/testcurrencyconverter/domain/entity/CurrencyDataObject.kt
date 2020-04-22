package com.example.testcurrencyconverter.domain.entity

import com.example.testcurrencyconverter.presentation.entity.BaseCurrencyEntity
import com.example.testcurrencyconverter.presentation.entity.CurrencyAdapterEntity

class CurrencyDataObject (
    var currencyEntityList: List<CurrencyAdapterEntity>,
    var currentBaseRate: BaseCurrencyEntity
)