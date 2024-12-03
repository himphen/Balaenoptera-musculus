package com.himphen.myapplication.ui.main.viewmodel

sealed class MainViewModelResult {
    sealed class EmptyDB : MainViewModelResult() {
        data object Success : EmptyDB()
        data class Failed(val message: String?) : EmptyDB()
    }
    sealed class InsertDB : MainViewModelResult() {
        data object Success : InsertDB()
        data class Failed(val message: String?) : InsertDB()
    }
    sealed class SelectData : MainViewModelResult() {
        data class Failed(val message: String?) : InsertDB()
    }
}