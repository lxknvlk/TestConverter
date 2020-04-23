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

    var baseCurrencyValue: Double = 1.0
    var baseCurrency: CurrencyType = CurrencyType.USD

    var currencyList: MutableList<CurrencyAdapterEntity> = mutableListOf()

    val baseCurrencyLiveData: MutableLiveData<CurrencyType> by lazy {
        val mLiveCurrencyLiveData = MutableLiveData<CurrencyType>()
        mLiveCurrencyLiveData.postValue(CurrencyType.USD)
        mLiveCurrencyLiveData
    }

    val currentBaseRatesLiveData: LiveData<MutableList<CurrencyAdapterEntity>> by lazy {
        Transformations.switchMap(baseCurrencyLiveData) { newBaseCurrency ->
            Transformations.map(applicationGraph.currencyRepository().getRatesForBase(newBaseCurrency)){ currencyEntityList ->
                val currencyAdapterEntityList = mutableListOf<CurrencyAdapterEntity>()
                currencyAdapterEntityList.add(CurrencyAdapterEntity(newBaseCurrency, baseCurrencyValue))

                currencyEntityList.forEach {
                    currencyAdapterEntityList.add(CurrencyAdapterEntity(it.other, it.value * baseCurrencyValue))
                }

                currencyAdapterEntityList
            }
        }
    }


    fun setNewBaseCurrency(currency: CurrencyType, value: Double){
        baseCurrencyValue = 1.0
        baseCurrency = currency
        baseCurrencyLiveData.postValue(currency)
    }

    fun setNewBaseCurrencyValue(value: Double){
        baseCurrencyValue = value
    }

    fun updateRates(){
        GlobalScope.launch(Dispatchers.IO) {
            while (true){
                applicationGraph.currencyRepository().updateRates()
                delay(1000)
            }
        }
    }
}