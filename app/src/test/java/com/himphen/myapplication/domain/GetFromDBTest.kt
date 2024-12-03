package com.himphen.myapplication.domain

import com.himphen.myapplication.db.entity.CurrencyInfoEntity
import com.himphen.myapplication.domain.main.GetFromDB
import com.himphen.myapplication.domain.main.GetFromDBImpl
import com.himphen.myapplication.domain.repository.CurrencyRepository
import com.himphen.myapplication.model.CurrencyInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetFromDBTest {
    private lateinit var useCase: GetFromDB

    @MockK
    private lateinit var repository: CurrencyRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        useCase = GetFromDBImpl(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `execute should return info from repository`() = runBlocking {

        val currencyInfoEntities = listOf(
            CurrencyInfoEntity(id = "BTC", name = "Bitcoin", symbol = "BTC", code = null),
            CurrencyInfoEntity(id = "USD", name = "US Dollar", symbol = "USD", code = "USD")
        )

        val infosFlow = MutableStateFlow(emptyList<CurrencyInfoEntity>())

        coEvery { repository.infos } returns infosFlow
        infosFlow.emit(currencyInfoEntities)

        val collectedInfos = mutableListOf<CurrencyInfo>()
        val job = launch {
            useCase.invoke().collect { collectedInfos.addAll(it) }
        }

        // Wait for the flow to emit the values
        delay(100)
        job.cancel()

        val expected = listOf(
            CurrencyInfo.Crypto(id = "BTC", name = "Bitcoin", symbol = "BTC"),
            CurrencyInfo.Fiat(id = "USD", name = "US Dollar", symbol = "USD", code = "USD")
        )

        assertEquals(expected, collectedInfos)

        coVerify { useCase.invoke() }
    }
}