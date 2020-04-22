package com.example.testcurrencyconverter.presentation.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.testcurrencyconverter.data.dagger.ApplicationGraph
import com.example.testcurrencyconverter.data.dagger.DaggerApplicationGraph
import com.example.testcurrencyconverter.data.dagger.DatabaseModule
import com.example.testcurrencyconverter.domain.entity.CurrencyEntity
import com.example.testcurrencyconverter.domain.entity.CurrencyType
import com.example.testcurrencyconverter.presentation.entity.CurrencyAdapterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application): ViewModel() {

    private val applicationGraph: ApplicationGraph = DaggerApplicationGraph.builder().databaseModule(
        DatabaseModule(application)
    ).build()

    var baseCurrency: CurrencyType = CurrencyType.USD
    var baseCurrencyValue: Double = 1.0

    fun currentBaseRatesLiveData(): LiveData<List<CurrencyAdapterEntity>> {
        return Transformations.map(applicationGraph.currencyRepository().getRatesForBase(baseCurrency)){
            buildCurrencyAdapterEntityList(it)
        }
    }

//    private fun refreshCurrencyElements(){
//        GlobalScope.launch(Dispatchers.IO){
//            val currencyEntityList = applicationGraph.currencyRepository().getRatesForBaseSync(baseCurrency)
//            val currencyAdapterEntityList = buildCurrencyAdapterEntityList(currencyEntityList)
//            currentBaseRatesLiveData.postValue(currencyAdapterEntityList)
//        }
//    }

    private fun buildCurrencyAdapterEntityList(currencyEntityList: List<CurrencyEntity>): List<CurrencyAdapterEntity> {
        val currencyAdapterEntityList = mutableListOf<CurrencyAdapterEntity>()
        currencyAdapterEntityList.add(CurrencyAdapterEntity(baseCurrency, baseCurrencyValue))

        currencyEntityList.forEach {
            currencyAdapterEntityList.add(CurrencyAdapterEntity(it.other, it.value * baseCurrencyValue))
        }

        return currencyAdapterEntityList
    }

    fun setNewBaseCurrency(currency: CurrencyType, value: Double){
        baseCurrencyValue = 1.0
        baseCurrency = currency
//        refreshCurrencyElements()
    }

    fun setNewBaseCurrencyValue(value: Double){
        baseCurrencyValue = value
//        refreshCurrencyElements()
    }

    fun updateRates(){
        GlobalScope.launch(Dispatchers.IO) {
//            applicationGraph.currencyRepository().updateRates()

            while (true){
                applicationGraph.currencyRepository().updateRates()
//                refreshCurrencyElements()
                delay(5000)
            }
        }
    }
}