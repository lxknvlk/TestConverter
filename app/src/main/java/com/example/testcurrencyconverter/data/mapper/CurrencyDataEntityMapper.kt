package com.example.testcurrencyconverter.data.mapper

import com.example.testcurrencyconverter.data.entity.CurrencyData
import com.example.testcurrencyconverter.domain.entity.ApiResponseEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyEntity
import com.example.testcurrencyconverter.domain.mapper.Mapper

class CurrencyDataEntityMapper: Mapper<CurrencyData, CurrencyEntity>() {
    override fun mapFrom(from: CurrencyData): CurrencyEntity {
        return CurrencyEntity(
            base = from.base,
            other = from.other,
            value = from.value
        )
    }
}