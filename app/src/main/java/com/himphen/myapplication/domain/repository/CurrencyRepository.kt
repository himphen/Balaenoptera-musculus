package com.himphen.myapplication.domain.repository

import com.himphen.myapplication.network.ApiService
import com.himphen.myapplication.db.dao.CurrencyInfoDao
import com.himphen.myapplication.db.entity.CurrencyInfoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class CurrencyRepository(
    private val apiService: ApiService,
    private val dao: CurrencyInfoDao,
) {
    val infos: StateFlow<List<CurrencyInfoEntity>> = dao.getAll().stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    suspend fun insertData(): Result<Boolean> {
        val listA = try {
            withContext(Dispatchers.IO) {
                apiService.getCryptoData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }

        val listB = try {
            withContext(Dispatchers.IO) {
                apiService.getFiatData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }

        val result = buildList {
            addAll(listA)
            addAll(listB)
        }.map {
            CurrencyInfoEntity(
                id = it.id,
                name = it.name,
                symbol = it.symbol,
                code = it.code,
            )
        }

        try {
            withContext(Dispatchers.IO) {
                dao.add(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }

        return Result.success(true)
    }

    suspend fun clearData(): Result<Boolean> {
        try {
            withContext(Dispatchers.IO) {
                dao.clearAll()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }

        return Result.success(true)
    }
}