package com.example.testcurrencyconverter.data.mapper

import com.example.testcurrencyconverter.data.database.stringToCurrencyType
import com.example.testcurrencyconverter.domain.entity.ApiResponseEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyType
import com.example.testcurrencyconverter.domain.mapper.Mapper
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject

class ApiResponseEntityMapper: Mapper<JSONObject, ApiResponseEntity>() {
    override fun mapFrom(from: JSONObject): ApiResponseEntity {
        val ratesObject = from.getJSONObject("rates")
        val base = from.getString("base")

        val baseType: CurrencyType = stringToCurrencyType(base)

        val rates: Map<String, Double> = ObjectMapper().readValue(ratesObject.toString(), object :TypeReference<Map<String,Double>>(){})

        return ApiResponseEntity(baseType, rates)
    }
}

/*
example api response
{
   "rates":{
      "CAD":1.4060350362,
      "HKD":7.7513528387,
      ...
      "GBP":0.8014766578,
      "USD":1.0
   },
   "base":"USD",
   "date":"2020-04-15"
}
 */