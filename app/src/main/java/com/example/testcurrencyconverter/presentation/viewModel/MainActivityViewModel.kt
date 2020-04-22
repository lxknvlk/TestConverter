package com.example.testcurrencyconverter.presentation.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.testcurrencyconverter.data.dagger.ApplicationGraph
import com.example.testcurrencyconverter.data.dagger.DaggerApplicationGraph
import com.example.testcurrencyconverter.data.dagger.DatabaseModule
import com.example.testcurrencyconverter.domain.entity.CurrencyDataObject
import com.example.testcurrencyconverter.domain.entity.ApiResponseEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyType
import com.example.testcurrencyconverter.presentation.entity.BaseCurrencyEntity
import com.example.testcurrencyconverter.presentation.entity.CurrencyAdapterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application): ViewModel() {

    private val applicationGraph: ApplicationGraph = DaggerApplicationGraph.builder().databaseModule(
        DatabaseModule(application)
    ).build()

    var baseCurrency: CurrencyType = CurrencyType.USD
    var baseCurrencyValue: Double = 1.0


    val currentBaseRatesLiveData: LiveData<List<CurrencyAdapterEntity>> by lazy {
        val mRatesUpdateLiveData = MediatorLiveData<List<CurrencyAdapterEntity>>()

        mRatesUpdateLiveData.addSource(applicationGraph.currencyRepository().getRatesForBase(baseCurrency)) { currencyEntityList ->
            val currencyAdapterEntityList = mutableListOf<CurrencyAdapterEntity>()

            currencyEntityList.forEach {
                currencyAdapterEntityList.add(CurrencyAdapterEntity(it.other, it.value * baseCurrencyValue))
            }

            mRatesUpdateLiveData.postValue(currencyAdapterEntityList)
        }

        mRatesUpdateLiveData
    }

//    private fun buildCurrencyAdapterEntityList(apiResponseList: MutableList<ApiResponseEntity>, baseCurrency: BaseCurrencyEntity)
//            : List<CurrencyAdapterEntity>{
//
//        val currencyAdapterEntityList = mutableListOf<CurrencyAdapterEntity>()
//        val sortedCurrencyList = sortCurrencyList(apiResponseList, baseCurrency)
//
//        sortedCurrencyList.forEach {
//            val ratesMap: Map<String, Double> = it.rates
//            val rate: Double = ratesMap[baseCurrency.currency.toString()] ?: 1.0
//            val rateToBase = 1 / rate
//            val value = baseCurrency.value * rateToBase
//
//            currencyAdapterEntityList.add(CurrencyAdapterEntity(baseCurrency, it.currency, rateToBase, value))
//        }
//
//        return currencyAdapterEntityList
//    }
//
//    private fun sortCurrencyList(apiResponseList: MutableList<ApiResponseEntity>, baseCurrency: BaseCurrencyEntity): List<ApiResponseEntity>{
//        if (apiResponseList.size == 0) return apiResponseList
//
//        val baseElement = apiResponseList.filter { it.currency == baseCurrency.currency }[0]
//        apiResponseList.remove(baseElement)
//        apiResponseList.add(0, baseElement)
//        return apiResponseList
//    }
    
    fun setCurrentBaseCurrency(baseCurrency: BaseCurrencyEntity){
//        currentBaseRateLiveData.postValue(baseCurrency)
    }

    fun updateRates(){
        GlobalScope.launch(Dispatchers.IO) {
            applicationGraph.currencyRepository().updateRates()

            // ALEX_Z: надо бы убрать комментарий внизу
//            while (true){
//                applicationGraph.currencyRepository().updateRates()
//                delay(1000)
//            }
        }
    }
}