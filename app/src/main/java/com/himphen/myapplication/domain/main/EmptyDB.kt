package com.himphen.myapplication.domain.main

interface EmptyDB {
    suspend operator fun invoke(): Result<Boolean>
}