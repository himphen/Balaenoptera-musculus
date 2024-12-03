package com.himphen.myapplication.ui.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.himphen.myapplication.domain.main.EmptyDB
import com.himphen.myapplication.domain.main.GetFromDB
import com.himphen.myapplication.domain.main.InsertDB
import com.himphen.myapplication.model.CurrencyInfo
import com.himphen.myapplication.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val emptyDB: EmptyDB,
    private val insertDB: InsertDB,
    getFromDB: GetFromDB,
) : BaseViewModel() {

    val isLoading: StateFlow<Boolean> = MutableStateFlow(false)

    val dataFromDB: StateFlow<List<CurrencyInfo>> = getFromDB.invoke()
    val dataA: StateFlow<List<CurrencyInfo.Crypto>> = dataFromDB.map { list ->
        list.filterIsInstance<CurrencyInfo.Crypto>()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val dataB: StateFlow<List<CurrencyInfo.Fiat>> = dataFromDB.map { list ->
        list.filterIsInstance<CurrencyInfo.Fiat>()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val selectedData: StateFlow<List<CurrencyInfo>?> = MutableStateFlow(null)

    val vmResult: SharedFlow<MainViewModelResult> = MutableSharedFlow()

    fun emptyDB() {
        viewModelScope.launch {
            selectedData.mutable.value = null
            val result = emptyDB.invoke()
            if (result.isSuccess) {
                vmResult.mutable.emit(MainViewModelResult.EmptyDB.Success)
            } else {
                vmResult.mutable.emit(MainViewModelResult.EmptyDB.Failed(result.exceptionOrNull()?.message))
            }
        }
    }

    fun insertDB() {
        viewModelScope.launch {
            isLoading.mutable.value = true
            selectedData.mutable.value = null
            val result = insertDB.invoke()
            isLoading.mutable.value = false
            if (result.isSuccess) {
                vmResult.mutable.emit(MainViewModelResult.InsertDB.Success)
            } else {
                vmResult.mutable.emit(MainViewModelResult.InsertDB.Failed(result.exceptionOrNull()?.message))
            }
        }
    }

    fun selectDataA() {
        if (dataA.value.isEmpty()) {
            viewModelScope.launch {
                vmResult.mutable.emit(MainViewModelResult.SelectData.Failed("No Data in DB"))
            }
            return
        }

        selectedData.mutable.value = dataA.value
    }

    fun selectDataB() {
        if (dataB.value.isEmpty()) {
            viewModelScope.launch {
                vmResult.mutable.emit(MainViewModelResult.SelectData.Failed("No Data in DB"))
            }
            return
        }

        selectedData.mutable.value = dataB.value
    }

    fun selectDataAll() {
        if (dataFromDB.value.isEmpty()) {
            viewModelScope.launch {
                vmResult.mutable.emit(MainViewModelResult.SelectData.Failed("No Data in DB"))
            }
            return
        }

        selectedData.mutable.value = dataFromDB.value
    }
}