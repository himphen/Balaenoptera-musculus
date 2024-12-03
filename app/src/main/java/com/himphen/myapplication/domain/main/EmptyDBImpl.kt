package com.himphen.myapplication.domain.main

import com.himphen.myapplication.domain.repository.CurrencyRepository

class EmptyDBImpl(
    private val repository: CurrencyRepository,
) : EmptyDB {
    override suspend operator fun invoke(): Result<Boolean> {
        return repository.clearData()
    }
}