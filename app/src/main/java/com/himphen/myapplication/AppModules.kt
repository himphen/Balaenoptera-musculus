package com.himphen.myapplication

import com.himphen.myapplication.db.LocalDatabase
import com.himphen.myapplication.domain.main.EmptyDB
import com.himphen.myapplication.domain.main.EmptyDBImpl
import com.himphen.myapplication.domain.main.GetFromDB
import com.himphen.myapplication.domain.main.GetFromDBImpl
import com.himphen.myapplication.domain.main.InsertDB
import com.himphen.myapplication.domain.main.InsertDBImpl
import com.himphen.myapplication.network.ApiService
import com.himphen.myapplication.domain.repository.CurrencyRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val myAppModules = module {
    single { ApiService() }

    single {
        CurrencyRepository(get(), get())
    }

    factory<EmptyDB> {
        EmptyDBImpl(get())
    }

    factory<InsertDB> {
        InsertDBImpl(get())
    }

    factory<GetFromDB> {
        GetFromDBImpl(get())
    }

    single { LocalDatabase.getInstance(androidContext()) }

    single { get<LocalDatabase>().currencyInfoDao() }
}