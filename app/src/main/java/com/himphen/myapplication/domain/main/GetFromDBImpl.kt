package com.himphen.myapplication.domain.main

import com.himphen.myapplication.db.entity.CurrencyInfoEntity
import com.himphen.myapplication.model.CurrencyInfo
import com.himphen.myapplication.domain.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform

class GetFromDBImpl(
    private val repository: CurrencyRepository,
) : GetFromDB {
    override operator fun invoke(): StateFlow<List<CurrencyInfo>> {
        return repository.infos.transform { list: List<CurrencyInfoEntity> ->
            val result = list.map {
                if (it.code == null) {
                    CurrencyInfo.Crypto(
                        id = it.id, name = it.name, symbol = it.symbol
                    )
                } else {
                    CurrencyInfo.Fiat(
                        id = it.id, name = it.name, symbol = it.symbol, code = it.code
                    )
                }
            }
            emit(result)
        }.stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
    }
}