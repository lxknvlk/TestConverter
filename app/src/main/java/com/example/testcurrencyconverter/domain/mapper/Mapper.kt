package com.example.testcurrencyconverter.domain.mapper

abstract class Mapper<in E, out T> {
    abstract fun mapFrom(from: E): T
}
