package com.example.testcurrencyconverter.presentation.view

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testcurrencyconverter.R
import com.example.testcurrencyconverter.data.database.round
import com.example.testcurrencyconverter.presentation.entity.BaseCurrencyEntity
import com.example.testcurrencyconverter.presentation.entity.CurrencyAdapterEntity


private const val LAYOUT_ID = R.layout.currency_adapter_item

class CurrencyAdapter(
    private val currencyList: MutableList<CurrencyAdapterEntity>,
    private val baseCurrency: BaseCurrencyEntity,
    private val itemClickListener: OnItemClickListener,
    private val textChangeListener: TextChangeListener
): RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    var textChangeListenerDisabled = false

    interface OnItemClickListener {
        fun onItemClick(item: CurrencyAdapterEntity)
    }

    interface TextChangeListener {
        fun onTextChanged(item: CurrencyAdapterEntity, newValue: String, baseCurrency: BaseCurrencyEntity)
    }


    // ALEX_Z: зачем нужен этот метод?
    fun getCurrencyList(): MutableList<CurrencyAdapterEntity>{
        return currencyList
    }

    class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvCurrencyName: TextView = view.findViewById(R.id.tvCurrencyName)
        val etCurrencyAmount: EditText = view.findViewById(R.id.etCurrencyAmount)
        val llRoot: LinearLayout = view.findViewById(R.id.llRoot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(LAYOUT_ID, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currencyEntity: CurrencyAdapterEntity = currencyList[position]
        holder.etCurrencyAmount.setText(currencyEntity.value.round(2).toString())
        holder.tvCurrencyName.text = currencyEntity.currency.toString()

        holder.etCurrencyAmount.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (textChangeListenerDisabled) return
                textChangeListener.onTextChanged(currencyEntity, s.toString(), baseCurrency)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        holder.llRoot.setOnClickListener{
            itemClickListener.onItemClick(currencyEntity)
        }
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    fun updateList(newList: MutableList<CurrencyAdapterEntity>){
        val diffResult = DiffUtil.calculateDiff(CurrencyAdapterEntityDiffUtilCallback(currencyList, newList))
        currencyList.clear()
        currencyList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}
