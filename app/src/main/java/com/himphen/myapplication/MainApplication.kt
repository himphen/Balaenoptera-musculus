package com.himphen.myapplication

import android.app.Application
import com.himphen.myapplication.ui.currency.currencyListModule
import com.himphen.myapplication.ui.main.mainModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@MainApplication)
            // Load modules
            modules(myAppModules)
            modules(mainModules)
            modules(currencyListModule)
        }
    }
}