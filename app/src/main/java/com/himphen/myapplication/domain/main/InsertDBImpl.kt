package com.himphen.myapplication.domain.main

import com.himphen.myapplication.domain.repository.CurrencyRepository

class InsertDBImpl(
    private val repository: CurrencyRepository,
) : InsertDB {
    override suspend operator fun invoke(): Result<Boolean> {
        return repository.insertData()
    }
}