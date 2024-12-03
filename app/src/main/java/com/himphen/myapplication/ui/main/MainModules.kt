package com.himphen.myapplication.ui.main

import com.himphen.myapplication.ui.main.viewmodel.DemoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModules = module {
    viewModel {
        DemoViewModel(get(), get(), get())
    }
}