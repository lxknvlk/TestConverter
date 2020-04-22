package com.example.testcurrencyconverter.data.mapper

import com.example.testcurrencyconverter.data.entity.CurrencyData
import com.example.testcurrencyconverter.domain.entity.ApiResponseEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyEntity
import com.example.testcurrencyconverter.domain.mapper.Mapper

class CurrencyEntityDataMapper: Mapper<CurrencyEntity, CurrencyData>() {
    override fun mapFrom(from: CurrencyEntity): CurrencyData {
        return CurrencyData(
            base = from.base,
            other = from.other,
            value = from.value
        )
    }
}