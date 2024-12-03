package com.himphen.myapplication.ui.currency.viewmodel

import androidx.lifecycle.viewModelScope
import com.himphen.myapplication.model.CurrencyInfo
import com.himphen.myapplication.ui.base.BaseViewModel
import com.himphen.myapplication.ui.currency.adapter.CurrencyListAdapterItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

class CurrencyListViewModel : BaseViewModel() {

    val dataSource: StateFlow<List<CurrencyInfo>?> = MutableStateFlow(null)
    val keyword: StateFlow<String> = MutableStateFlow("")

    val adapterList: SharedFlow<List<CurrencyListAdapterItem>?> =
        combine(dataSource, keyword) { dataSource, keyword ->
            val filteredList = if (keyword.isEmpty()) {
                dataSource
            } else {
                val filterRegex = "^$keyword.*|.+ $keyword.*".toRegex(
                    RegexOption.IGNORE_CASE
                )
                dataSource?.filter {
                    it.name.matches(filterRegex) || it.symbol.startsWith(keyword, true)
                }
            }
            filteredList?.map {
                if (it is CurrencyInfo.Fiat) {
                    CurrencyListAdapterItem.Fiat(
                        id = it.id, name = it.name, symbol = it.symbol, code = it.code
                    )
                } else {
                    CurrencyListAdapterItem.Crypto(
                        id = it.id, name = it.name, symbol = it.symbol
                    )
                }
            }
        }.shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    val isEmpty: StateFlow<Boolean> =
        adapterList.map { it?.isEmpty() == true }
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isSelectedData: StateFlow<Boolean> =
        adapterList.map { it != null }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isEnableSearch: StateFlow<Boolean> = MutableStateFlow(false)

    fun setSearchEnabled() {
        keyword.mutable.value = ""
        isEnableSearch.mutable.value = true
    }

    fun setSearchDisabled() {
        isEnableSearch.mutable.value = false
        keyword.mutable.value = ""
    }

    fun updateSearchKeyword(string: String?) {
        keyword.mutable.value = string.orEmpty()
    }

    fun updateDataSource(list: List<CurrencyInfo>?) {
        setSearchDisabled()
        dataSource.mutable.value = list
    }

}