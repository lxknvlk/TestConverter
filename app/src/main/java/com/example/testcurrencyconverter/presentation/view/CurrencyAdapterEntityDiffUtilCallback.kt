package com.example.testcurrencyconverter.presentation.view

import androidx.recyclerview.widget.DiffUtil
import com.example.testcurrencyconverter.presentation.entity.CurrencyAdapterEntity

class CurrencyAdapterEntityDiffUtilCallback(
    private val oldList: List<CurrencyAdapterEntity>,
    private val newList: List<CurrencyAdapterEntity>
): DiffUtilCallback<CurrencyAdapterEntity>(oldList, newList){

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        val oldBase = oldItem.currency
        val newBase = newItem.currency

        return oldBase == newBase
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.value == newItem.value
    }
}

abstract class DiffUtilCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>
): DiffUtil.Callback(){

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

}