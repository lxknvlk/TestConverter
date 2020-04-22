package com.example.testcurrencyconverter.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testcurrencyconverter.presentation.view.MainApplication

class MainActivityViewModelFactory(
    private val application: MainApplication
): ViewModelProvider.AndroidViewModelFactory(application){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return MainActivityViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
