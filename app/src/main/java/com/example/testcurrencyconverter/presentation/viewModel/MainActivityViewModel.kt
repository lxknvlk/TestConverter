package com.example.testcurrencyconverter.presentation.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.testcurrencyconverter.data.dagger.ApplicationGraph
import com.example.testcurrencyconverter.data.dagger.DaggerApplicationGraph
import com.example.testcurrencyconverter.data.dagger.DatabaseModule
import com.example.testcurrencyconverter.domain.entity.CurrencyDataObject
import com.example.testcurrencyconverter.domain.entity.ApiResponseEntity
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

    var baseCurrency: CurrencyType = CurrencyType.USD
    var baseCurrencyValue: Double = 1.0

    val currentBaseRatesLiveData: MutableLiveData<List<CurrencyAdapterEntity>> by lazy {
        val mRatesUpdateLiveData = MediatorLiveData<List<CurrencyAdapterEntity>>()

        mRatesUpdateLiveData.addSource(applicationGraph.currencyRepository().getRatesForBase(baseCurrency)) { currencyEntityList ->
            val currencyAdapterEntityList = buildCurrencyAdapterEntityList(currencyEntityList)
            mRatesUpdateLiveData.postValue(currencyAdapterEntityList)
        }

        mRatesUpdateLiveData
    }

    fun refreshValues(){
        GlobalScope.launch(Dispatchers.IO){
            val currencyEntityList = applicationGraph.currencyRepository().getRatesForBaseSync(baseCurrency)
            val currencyAdapterEntityList = buildCurrencyAdapterEntityList(currencyEntityList)
            currentBaseRatesLiveData.postValue(currencyAdapterEntityList)
        }
    }

    private fun buildCurrencyAdapterEntityList(currencyEntityList: List<CurrencyEntity>): List<CurrencyAdapterEntity> {
        val currencyAdapterEntityList = mutableListOf<CurrencyAdapterEntity>()
        currencyAdapterEntityList.add(CurrencyAdapterEntity(baseCurrency, baseCurrencyValue))

        currencyEntityList.forEach {
            currencyAdapterEntityList.add(CurrencyAdapterEntity(it.other, it.value * baseCurrencyValue))
        }

        return currencyAdapterEntityList
    }

    fun setNewBaseCurrency(currency: CurrencyType){
        baseCurrency = currency
        refreshValues()
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