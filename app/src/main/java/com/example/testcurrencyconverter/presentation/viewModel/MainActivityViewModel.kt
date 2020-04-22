package com.example.testcurrencyconverter.presentation.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.testcurrencyconverter.data.dagger.ApplicationGraph
import com.example.testcurrencyconverter.data.dagger.DaggerApplicationGraph
import com.example.testcurrencyconverter.data.dagger.DatabaseModule
import com.example.testcurrencyconverter.data.database.round
import com.example.testcurrencyconverter.data.entity.CurrencyData
import com.example.testcurrencyconverter.domain.entity.CurrencyDataObject
import com.example.testcurrencyconverter.domain.entity.CurrencyEntity
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

    private val currentBaseRateLiveData: MutableLiveData<BaseCurrencyEntity> by lazy {
        MutableLiveData<BaseCurrencyEntity>()
    }

    val ratesUpdateLiveData: LiveData<List<CurrencyEntity>?> by lazy {
        val mRatesUpdateLiveData = MediatorLiveData<List<CurrencyEntity>?>()

        mRatesUpdateLiveData.addSource(applicationGraph.currencyRepository().getRates()) {
            mRatesUpdateLiveData.postValue(it)
        }

        mRatesUpdateLiveData
    }

    val ratesLiveData: LiveData<CurrencyDataObject> by lazy {
        Transformations.switchMap(currentBaseRateLiveData) { baseCurrency ->
            Transformations.map(applicationGraph.currencyRepository().getRates()){ currencyEntityList ->

                val currencyAdapterEntityList: List<CurrencyAdapterEntity> = buildCurrencyAdapterEntityList(
                    currencyEntityList.toMutableList(),
                    baseCurrency)

                CurrencyDataObject(currencyAdapterEntityList, baseCurrency)
            }
        }
    }

    private fun buildCurrencyAdapterEntityList(currencyList: MutableList<CurrencyEntity>, baseCurrency: BaseCurrencyEntity)
            : List<CurrencyAdapterEntity>{

        val currencyAdapterEntityList = mutableListOf<CurrencyAdapterEntity>()
        val sortedCurrencyList = sortCurrencyList(currencyList, baseCurrency)

        sortedCurrencyList.forEach {
            val ratesMap: Map<String, Double> = it.rates
            val rate: Double = ratesMap[baseCurrency.currency.toString()] ?: 1.0
            val rateToBase = 1 / rate
            val value = baseCurrency.value * rateToBase

            currencyAdapterEntityList.add(CurrencyAdapterEntity(baseCurrency, it.currency, rateToBase, value))
        }

        return currencyAdapterEntityList
    }

    private fun sortCurrencyList(currencyList: MutableList<CurrencyEntity>, baseCurrency: BaseCurrencyEntity): List<CurrencyEntity>{
        if (currencyList.size == 0) return currencyList

        val baseElement = currencyList.filter { it.currency == baseCurrency.currency }[0]
        currencyList.remove(baseElement)
        currencyList.add(0, baseElement)
        return currencyList
    }
    
    fun setCurrentBaseCurrency(baseCurrency: BaseCurrencyEntity){
        currentBaseRateLiveData.postValue(baseCurrency)
    }

    fun updateRates(){
        GlobalScope.launch(Dispatchers.IO) {
            applicationGraph.currencyRepository().updateRates()

//            while (true){
//                applicationGraph.currencyRepository().updateRates()
//                delay(1000)
//            }
        }
    }
}