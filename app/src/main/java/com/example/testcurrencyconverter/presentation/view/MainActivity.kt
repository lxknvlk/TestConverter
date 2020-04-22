package com.example.testcurrencyconverter.presentation.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testcurrencyconverter.R
import com.example.testcurrencyconverter.data.database.round
import com.example.testcurrencyconverter.domain.entity.CurrencyType
import com.example.testcurrencyconverter.presentation.entity.BaseCurrencyEntity
import com.example.testcurrencyconverter.presentation.entity.CurrencyAdapterEntity
import com.example.testcurrencyconverter.presentation.viewModel.MainActivityViewModel
import com.example.testcurrencyconverter.presentation.viewModel.MainActivityViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

private const val LAYOUT_ID = R.layout.activity_main

class MainActivity : AppCompatActivity() {

    private lateinit var mainAcitvityViewModelFactory: MainActivityViewModelFactory

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this, mainAcitvityViewModelFactory).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LAYOUT_ID)

        rvCurrencyList?.apply {
            if (layoutManager == null) layoutManager = LinearLayoutManager(context)
        }


        initOservers()
        mainActivityViewModel.updateRates()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        mainAcitvityViewModelFactory = MainActivityViewModelFactory(
            application as MainApplication
        )

        return super.onCreateView(name, context, attrs)
    }

    private val itemClickListener = object: CurrencyAdapter.OnItemClickListener {
        override fun onItemClick(item: CurrencyAdapterEntity) {
            mainActivityViewModel.setCurrentBaseCurrency(BaseCurrencyEntity(item.currency, 1.0))
        }
    }

    private val textChangeListener = object: CurrencyAdapter.TextChangeListener {
        override fun onTextChanged(item: CurrencyAdapterEntity, newValue: String, baseCurrency: BaseCurrencyEntity) {
            val currencyAdapter: CurrencyAdapter = rvCurrencyList.adapter as CurrencyAdapter
            val currencyElements = ArrayList(currencyAdapter.getCurrencyList().map { it.copy() })

            if (newValue.isEmpty()) return

            val newValueDouble = try {
                newValue.toDouble()
            } catch (e: Exception) {
                return
            }

            val skippedCurrency = if (item.currency == baseCurrency.currency){
                baseCurrency.value = newValueDouble
                baseCurrency.currency
            } else {
                baseCurrency.value = newValueDouble / item.rateToBase
                item.currency
            }

            currencyElements.forEach {
                val isBaseCurrency = it.currency == baseCurrency.currency
                val isSkippedCurrency = it.currency == skippedCurrency

                if (!isSkippedCurrency){
                    if (isBaseCurrency){
                        it.value = baseCurrency.value
                    } else {
                        it.value = baseCurrency.value * it.rateToBase
                    }

                }
            }

            updateList(currencyElements)
        }
    }

    private fun updateList(currencyElements: MutableList<CurrencyAdapterEntity>){
        val currencyAdapter: CurrencyAdapter = rvCurrencyList.adapter as CurrencyAdapter
        currencyAdapter.textChangeListenerDisabled = true
        rvCurrencyList.post { currencyAdapter.updateList(currencyElements) }
        rvCurrencyList.postDelayed({ currencyAdapter.textChangeListenerDisabled = false }, 100)
    }

    private fun initOservers(){
        mainActivityViewModel.ratesLiveData.observe(this, Observer { currencyDataObject ->
            tvUpdatedAt.text = Date().toString()
            rvCurrencyList.adapter = CurrencyAdapter(
                currencyDataObject.currencyEntityList.toMutableList(),
                currencyDataObject.currentBaseRate,
                itemClickListener,
                textChangeListener
            )
        })

        mainActivityViewModel.ratesUpdateLiveData.observe(this, Observer {
            mainActivityViewModel.setCurrentBaseCurrency(BaseCurrencyEntity(CurrencyType.USD, 1.0))
        })
    }
}
