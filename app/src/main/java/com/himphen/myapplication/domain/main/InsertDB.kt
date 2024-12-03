package com.himphen.myapplication.domain.main

interface InsertDB {
    suspend operator fun invoke(): Result<Boolean>
}