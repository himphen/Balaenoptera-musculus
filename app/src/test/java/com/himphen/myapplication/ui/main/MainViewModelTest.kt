package com.himphen.myapplication.ui.main

import com.himphen.myapplication.domain.main.EmptyDB
import com.himphen.myapplication.domain.main.GetFromDB
import com.himphen.myapplication.domain.main.InsertDB
import com.himphen.myapplication.model.CurrencyInfo
import com.himphen.myapplication.ui.MainDispatcherRule
import com.himphen.myapplication.ui.main.viewmodel.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @MockK
    private lateinit var emptyDB: EmptyDB

    @MockK
    private lateinit var insertDB: InsertDB

    @MockK
    private lateinit var getFromDB: GetFromDB

    private lateinit var viewModel: MainViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private fun createVM() {
        viewModel = MainViewModel(
            emptyDB = emptyDB,
            insertDB = insertDB,
            getFromDB = getFromDB,
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `init should have correct init state`() = runTest {
        coEvery { getFromDB.invoke() } returns MutableStateFlow(emptyList())
        createVM()

        assertEquals(viewModel.isLoading.value, false)
        assertEquals(viewModel.dataFromDB.value, emptyList<CurrencyInfo>())
        assertEquals(viewModel.dataA.value, emptyList<CurrencyInfo>())
        assertEquals(viewModel.dataB.value, emptyList<CurrencyInfo>())
        assertEquals(viewModel.selectedData.value, null)
    }

    @Test
    fun `insertDB should emit success when insert operation succeeds`() = runBlocking {
        coEvery { getFromDB.invoke() } returns MutableStateFlow(emptyList())
        createVM()
        coEvery { insertDB.invoke() } returns Result.success(true)

        viewModel.insertDB()

        assertNull(viewModel.selectedData.value)
        assertEquals(viewModel.isLoading.value, false)

        coVerify {
            insertDB.invoke()
        }
    }

    @Test
    fun `emptyDB should emit success when empty operation succeeds`() = runBlocking {
        coEvery { getFromDB.invoke() } returns MutableStateFlow(emptyList())
        createVM()
        coEvery { emptyDB.invoke() } returns Result.success(true)

        viewModel.emptyDB()

        assertNull(viewModel.selectedData.value)
        assertEquals(viewModel.isLoading.value, false)

        coVerify {
            emptyDB.invoke()
        }
    }

    @Test
    fun `selectDataA should emit failure when dataA is empty`() = runBlocking {
        coEvery { getFromDB.invoke() } returns MutableStateFlow(emptyList())
        createVM()

        viewModel.selectDataA()

        assertEquals(viewModel.selectedData.value, null)
    }

    @Test
    fun `selectDataA should set selectedData when dataA is not empty`() = runBlocking {
        val mockData =
            listOf(
                CurrencyInfo.Crypto(id = "BTC", name = "Bitcoin", symbol = "BTC"),
                CurrencyInfo.Fiat(id = "USD", name = "US Dollar", symbol = "$", code = "USD")
            )

        val expectedData =
            listOf(
                CurrencyInfo.Crypto(id = "BTC", name = "Bitcoin", symbol = "BTC"),
            )
        coEvery { getFromDB.invoke() } returns MutableStateFlow(mockData)
        createVM()

        viewModel.selectDataA()

        assertEquals(expectedData, viewModel.selectedData.value)
    }

    @Test
    fun `selectDataB should emit failure when dataB is empty`() = runBlocking {
        coEvery { getFromDB.invoke() } returns MutableStateFlow(emptyList())
        createVM()

        viewModel.selectDataB()

        assertEquals(viewModel.selectedData.value, null)
    }

    @Test
    fun `selectDataB should set selectedData when dataB is not empty`() = runBlocking {
        val mockData =
            listOf(
                CurrencyInfo.Crypto(id = "BTC", name = "Bitcoin", symbol = "BTC"),
                CurrencyInfo.Fiat(id = "USD", name = "US Dollar", symbol = "$", code = "USD")
            )

        val expectedData =
            listOf(
                CurrencyInfo.Fiat(id = "USD", name = "US Dollar", symbol = "$", code = "USD")
            )
        coEvery { getFromDB.invoke() } returns MutableStateFlow(mockData)
        createVM()

        viewModel.selectDataB()

        assertEquals(expectedData, viewModel.selectedData.value)
    }

    @Test
    fun `selectDataAll should emit failure when dataFromDB is empty`() = runBlocking {
        coEvery { getFromDB.invoke() } returns MutableStateFlow(emptyList())
        createVM()

        viewModel.selectDataAll()

        assertEquals(viewModel.selectedData.value, null)
    }

    @Test
    fun `selectDataAll should set selectedData when dataB is not empty`() = runBlocking {
        val mockData =
            listOf(
                CurrencyInfo.Crypto(id = "BTC", name = "Bitcoin", symbol = "BTC"),
                CurrencyInfo.Fiat(id = "USD", name = "US Dollar", symbol = "$", code = "USD")
            )

        val expectedData =
            listOf(
                CurrencyInfo.Crypto(id = "BTC", name = "Bitcoin", symbol = "BTC"),
                CurrencyInfo.Fiat(id = "USD", name = "US Dollar", symbol = "$", code = "USD")
            )

        coEvery { getFromDB.invoke() } returns MutableStateFlow(mockData)
        createVM()

        viewModel.selectDataAll()

        assertEquals(expectedData, viewModel.selectedData.value)
    }
}