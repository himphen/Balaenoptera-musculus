package com.himphen.myapplication.domain.main

import com.himphen.myapplication.model.CurrencyInfo
import kotlinx.coroutines.flow.StateFlow

interface GetFromDB {
    operator fun invoke(): StateFlow<List<CurrencyInfo>>
}