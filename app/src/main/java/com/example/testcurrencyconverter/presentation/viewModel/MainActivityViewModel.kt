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

    val baseCurrencyLiveData: MutableLiveData<CurrencyType> by lazy {
        val mLiveCurrencyLiveData = MutableLiveData<CurrencyType>()
        mLiveCurrencyLiveData.postValue(CurrencyType.USD)
        mLiveCurrencyLiveData
    }

    val currentBaseRatesLiveData: LiveData<MutableList<CurrencyAdapterEntity>> by lazy {
        Transformations.switchMap(baseCurrencyLiveData) { _ ->
            Transformations.map(applicationGraph.currencyRepository().getRatesForBase(baseCurrency)){ currencyEntityList ->
                val currencyAdapterEntityList = mutableListOf<CurrencyAdapterEntity>()
                currencyAdapterEntityList.add(CurrencyAdapterEntity(baseCurrency, baseCurrencyValue))

                currencyEntityList.forEach {
                    currencyAdapterEntityList.add(CurrencyAdapterEntity(it.other, it.value * baseCurrencyValue))
                }

                currencyAdapterEntityList
            }
        }
    }


    fun setNewBaseCurrency(currency: CurrencyType, value: Double){
        baseCurrencyValue = value
        baseCurrency = currency
        baseCurrencyLiveData.postValue(currency)
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