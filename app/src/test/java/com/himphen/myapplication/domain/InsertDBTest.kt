package com.himphen.myapplication.domain

import com.himphen.myapplication.domain.main.InsertDB
import com.himphen.myapplication.domain.main.InsertDBImpl
import com.himphen.myapplication.domain.repository.CurrencyRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class InsertDBTest {
    private lateinit var useCase: InsertDB

    @MockK
    private lateinit var repository: CurrencyRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        useCase = InsertDBImpl(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `execute should return success`() = runBlocking {
        coEvery { repository.insertData() } returns Result.success(true)

        assertEquals(useCase.invoke(), Result.success(true))

        coVerify { useCase.invoke() }
    }

    @Test
    fun `execute should return failed`() = runBlocking {
        coEvery { repository.insertData() } returns Result.failure(Throwable())

        assertNotEquals(useCase.invoke(), Result.success(true))

        coVerify { useCase.invoke() }
    }
}