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
            mainActivityViewModel.setNewBaseCurrency(item.currency, item.value)
            initOservers()
        }
    }

    private val textChangeListener = object: CurrencyAdapter.TextChangeListener {
        override fun onTextChanged(item: CurrencyAdapterEntity, newValue: String) {
            if (newValue.isEmpty()) return

            val newValueDouble = try {
                newValue.toDouble()
            } catch (e: Exception) {
                return
            }

            if (item.currency == mainActivityViewModel.baseCurrency) {
                mainActivityViewModel.setNewBaseCurrencyValue(newValueDouble)
                initOservers()
            }
        }
    }

    private fun initOservers(){
        mainActivityViewModel.currentBaseRatesLiveData.observe(this, Observer { currencyAdapterEntityList ->
            tvUpdatedAt.text = Date().toString()

            updateList(currencyAdapterEntityList)
        })
    }

    private fun updateList(currencyAdapterEntityList: MutableList<CurrencyAdapterEntity>) {
        if (rvCurrencyList.adapter == null){
            rvCurrencyList.adapter = CurrencyAdapter(
                currencyAdapterEntityList.toMutableList(),
                itemClickListener,
                textChangeListener
            )
        } else {
            val adapter: CurrencyAdapter = rvCurrencyList.adapter as CurrencyAdapter
            adapter.updateList(currencyAdapterEntityList)
        }
    }
}
