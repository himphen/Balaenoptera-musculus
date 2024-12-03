package com.himphen.myapplication.db

import com.himphen.myapplication.db.dao.CurrencyInfoDao
import com.himphen.myapplication.db.entity.CurrencyInfoEntity
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.unmockkAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DaoTest {
    @MockK
    private lateinit var dao: CurrencyInfoDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should get data`() = runBlocking {
        val infosFlow = MutableStateFlow<List<CurrencyInfoEntity>>(emptyList())

        val infos = listOf(
            CurrencyInfoEntity(
                id = "BTC",
                name = "Bitcoin",
                symbol = "BTC",
            )
        )

        coEvery { dao.getAll() } returns infosFlow
        infosFlow.emit(infos)

        val collectedInfos = mutableListOf<CurrencyInfoEntity>()
        val job = launch {
            dao.getAll().collect { collectedInfos.addAll(it) }
        }

        // Wait for the flow to emit the values
        delay(100)
        job.cancel()

        assertEquals(infos, collectedInfos)

        coVerify { dao.getAll() }
    }

    @Test
    fun `should insert data`() = runBlocking {
        val infos = listOf(
            CurrencyInfoEntity(
                id = "BTC",
                name = "Bitcoin",
                symbol = "BTC",
            )
        )

        coEvery { dao.add(infos) } just Runs
        dao.add(infos)
        coVerify { dao.add(infos) }
    }

    @Test
    fun `should clear data`() = runBlocking {
        coEvery { dao.clearAll() } just Runs
        dao.clearAll()
        coVerify { dao.clearAll() }
    }

}