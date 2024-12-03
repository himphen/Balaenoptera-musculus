package com.himphen.myapplication.domain

import com.himphen.myapplication.db.dao.CurrencyInfoDao
import com.himphen.myapplication.db.entity.CurrencyInfoEntity
import com.himphen.myapplication.domain.repository.CurrencyRepository
import com.himphen.myapplication.network.ApiService
import com.himphen.myapplication.network.ApiServiceModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var repository: CurrencyRepository

    @MockK
    private lateinit var apiService: ApiService

    @MockK
    private lateinit var dao: CurrencyInfoDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coEvery { dao.getAll() } returns flow { emit(emptyList()) }

        repository = CurrencyRepository(apiService, dao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `insertData should return success`() = runBlocking {
        val fiatApiModels = listOf(
            ApiServiceModel(id = "USD", name = "US Dollar", symbol = "USD", code = "USD")
        )

        val cryptoApiModels = listOf(
            ApiServiceModel(id = "BTC", name = "Bitcoin", symbol = "BTC", code = null),
        )

        coEvery { apiService.getCryptoData() } returns cryptoApiModels
        coEvery { apiService.getFiatData() } returns fiatApiModels
        coEvery { dao.add(any()) } just Runs

        val result = repository.insertData()

        assertTrue(result.isSuccess)
        assertEquals(result, Result.success(true))

        coVerify {
            dao.add(
                listOf(
                    CurrencyInfoEntity(id = "BTC", name = "Bitcoin", symbol = "BTC", code = null),
                    CurrencyInfoEntity(id = "USD", name = "US Dollar", symbol = "USD", code = "USD")
                )
            )
        }
    }

    @Test
    fun `insertData should return failure when API call fails`() = runBlocking {
        coEvery { apiService.getCryptoData() } throws Exception("API error")

        val result = repository.insertData()

        assertTrue(result.isFailure)
        assertEquals("API error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `insertData should return failure when dao add fails`() = runBlocking {
        val fiatApiModels = listOf(
            ApiServiceModel(id = "USD", name = "US Dollar", symbol = "USD", code = "USD")
        )

        val cryptoApiModels = listOf(
            ApiServiceModel(id = "BTC", name = "Bitcoin", symbol = "BTC", code = null),
        )

        coEvery { apiService.getCryptoData() } returns cryptoApiModels
        coEvery { apiService.getFiatData() } returns fiatApiModels


        coEvery { dao.add(any()) } throws Exception("DAO error")

        val result = repository.insertData()

        assertTrue(result.isFailure)
        assertEquals("DAO error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `clearAll should return success`() = runBlocking {
        coEvery { dao.clearAll() } just Runs

        val result = repository.clearData()

        assertTrue(result.isSuccess)
        assertEquals(result, Result.success(true))

        coVerify {
            dao.clearAll()
        }
    }

    @Test
    fun `clearAll should return failure when dao clear fails`() = runBlocking {
        coEvery { dao.clearAll() } throws Exception("DAO error")

        val result = repository.clearData()

        assertTrue(result.isFailure)
        assertEquals("DAO error", result.exceptionOrNull()?.message)

        coVerify {
            dao.clearAll()
        }
    }

}