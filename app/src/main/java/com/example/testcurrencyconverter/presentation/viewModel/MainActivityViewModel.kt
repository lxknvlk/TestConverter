package com.example.testcurrencyconverter.presentation.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.testcurrencyconverter.data.dagger.ApplicationGraph
import com.example.testcurrencyconverter.data.dagger.DaggerApplicationGraph
import com.example.testcurrencyconverter.data.dagger.DatabaseModule
import com.example.testcurrencyconverter.domain.entity.CurrencyType
import com.example.testcurrencyconverter.presentation.entity.CurrencyAdapterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application): ViewModel() {

    private val applicationGraph: ApplicationGraph = DaggerApplicationGraph.builder().databaseModule(
        DatabaseModule(application)
    ).build()

//    var baseCurrencyValue: Double = 1.0
    var baseCurrency: CurrencyType = CurrencyType.USD

    val baseCurrencyLiveData: MutableLiveData<CurrencyAdapterEntity> by lazy {
        val mLiveCurrencyLiveData = MutableLiveData<CurrencyAdapterEntity>()
        mLiveCurrencyLiveData.postValue(CurrencyAdapterEntity(CurrencyType.USD, 1.0))
        mLiveCurrencyLiveData
    }

    val currentBaseRatesLiveData: LiveData<MutableList<CurrencyAdapterEntity>> by lazy {
        Transformations.switchMap(baseCurrencyLiveData) { baseCurrencyEntity ->
            baseCurrency = baseCurrencyEntity.currency
            Transformations.map(applicationGraph.currencyRepository().getRatesForBase(baseCurrencyEntity.currency)){ currencyEntityList ->
                val currencyAdapterEntityList = mutableListOf<CurrencyAdapterEntity>()
                currencyAdapterEntityList.add(baseCurrencyEntity)

                currencyEntityList.forEach {
                    currencyAdapterEntityList.add(CurrencyAdapterEntity(it.other, it.value * baseCurrencyEntity.value))
                }

                currencyAdapterEntityList
            }
        }
    }

    fun setNewBaseCurrency(item: CurrencyAdapterEntity){
        baseCurrencyLiveData.postValue(item)
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