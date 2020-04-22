package com.example.testcurrencyconverter.data.database

import androidx.room.TypeConverter
import com.example.testcurrencyconverter.domain.entity.CurrencyType
import org.json.JSONObject
import java.io.StringReader
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.round


class TypeConverters {

    @TypeConverter
    fun jsonToString(jsonObject: JSONObject): String{
        return jsonObject.toString()
    }

    @TypeConverter
    fun stringToJson(string: String): JSONObject{
        return JSONObject(string)
    }

    @TypeConverter
    fun currencyTypeToString(type: CurrencyType): String{
        return type.toString()
    }

    @TypeConverter
    fun stringToCurrencyType(typeString: String): CurrencyType{
        return when (typeString){
            CurrencyType.EUR.toString() -> CurrencyType.EUR
            CurrencyType.USD.toString() -> CurrencyType.USD
            CurrencyType.RUB.toString() -> CurrencyType.RUB
            CurrencyType.GBP.toString() -> CurrencyType.GBP
            else -> CurrencyType.EUR
        }
    }

    @TypeConverter
    fun mapToString(map: Map<String, Double>): String {
        return map.toString()
    }

    @TypeConverter
    fun stringToMap(string: String): Map<String, Double> {
        val props = Properties()
        props.load(StringReader(string.substring(1, string.length - 1).replace(", ", "\n")))
        val map: MutableMap<String, Double> = HashMap()
        for ((key, value) in props.entries) {
            map[key as String] = (value as String).toDouble()
        }

        return map
    }
}

fun stringToCurrencyType(typeString: String): CurrencyType{
    return when (typeString){
        CurrencyType.EUR.toString() -> CurrencyType.EUR
        CurrencyType.USD.toString() -> CurrencyType.USD
        CurrencyType.RUB.toString() -> CurrencyType.RUB
        CurrencyType.GBP.toString() -> CurrencyType.GBP
        else -> CurrencyType.EUR
    }
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}