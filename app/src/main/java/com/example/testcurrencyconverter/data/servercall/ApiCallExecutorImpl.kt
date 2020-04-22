package com.example.testcurrencyconverter.data.servercall

import com.example.testcurrencyconverter.data.mapper.ApiResponseEntityMapper
import com.example.testcurrencyconverter.domain.entity.ApiResponseEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyType
import com.example.testcurrencyconverter.domain.servercall.ApiCallExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiCallExecutorImpl @Inject constructor(
    private val OkHttpClient: OkHttpClient,
    // ALEX_Z: можем ли заменить на Mapper<JSONObject, CurrencyEntity>?
    private val apiResponseEntityMapper: ApiResponseEntityMapper
): ApiCallExecutor {
    override suspend fun getRates(baseCurrency: CurrencyType): ApiResponseEntity? {
        // ALEX_Z: зачем обертка в Dispatchers.IO?
        return withContext(Dispatchers.IO){
            val request: Request = Request.Builder()
                .url("https://api.exchangeratesapi.io/latest?base=$baseCurrency")
                .build()

            val call: Call = OkHttpClient.newCall(request)
            val response: Response = call.execute()

            if (response.isSuccessful) {
                val result = response.body()?.string()
                if (result != null){
                    return@withContext apiResponseEntityMapper.mapFrom(JSONObject(result))
                }
            }

            null
        }
    }
}