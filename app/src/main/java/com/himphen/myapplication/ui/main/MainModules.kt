package com.himphen.myapplication.ui.main

import com.himphen.myapplication.ui.main.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModules = module {
    viewModel {
        MainViewModel(get(), get(), get())
    }
}