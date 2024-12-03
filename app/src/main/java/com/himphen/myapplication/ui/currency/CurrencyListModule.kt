package com.himphen.myapplication.ui.currency

import com.himphen.myapplication.ui.currency.viewmodel.CurrencyListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val currencyListModule = module {

    viewModel {
        CurrencyListViewModel()
    }
}