package com.himphen.myapplication.ui.currency

import com.himphen.myapplication.model.CurrencyInfo
import com.himphen.myapplication.ui.MainDispatcherRule
import com.himphen.myapplication.ui.currency.adapter.CurrencyListAdapterItem
import com.himphen.myapplication.ui.currency.viewmodel.CurrencyListViewModel
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyViewModelTest {

    private lateinit var viewModel: CurrencyListViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private fun createVM() {
        viewModel = CurrencyListViewModel()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `init should have correct init state`() = runTest {
        createVM()

        assertEquals(viewModel.dataSource.value, null)
        assertEquals(viewModel.keyword.value, "")
    }

    @Test
    fun `updateDataSource should set dataSource and disable search`() = runBlocking {
        createVM()

        val mockData = listOf(
            CurrencyInfo.Crypto(id = "BTC", name = "Bitcoin", symbol = "BTC"),
            CurrencyInfo.Fiat(id = "USD", name = "US Dollar", symbol = "USD", code = "USD")
        )

        viewModel.updateDataSource(mockData)


        assertEquals(mockData, viewModel.dataSource.first())
        assertEquals(false, viewModel.isEnableSearch.first())
    }

    @Test
    fun `setSearchEnabled should enable search and reset keyword`() = runBlocking {
        createVM()

        viewModel.updateDataSource(
            listOf(
                CurrencyInfo.Fiat(id = "USD", name = "US Dollar", symbol = "USD", code = "USD")
            )
        )

        viewModel.setSearchEnabled()

        assertEquals("", viewModel.keyword.first())
        assertTrue(viewModel.isEnableSearch.first())
    }

    @Test
    fun `updateSearchKeyword should update keyword`() = runBlocking {
        createVM()

        val keyword = "Dollar"

        viewModel.updateSearchKeyword(keyword)

        assertEquals(keyword, viewModel.keyword.first())
    }

    @Test
    fun `adapterList should filter data based on keyword`() = runBlocking {
        createVM()

        val mockData = listOf(
            CurrencyInfo.Crypto(id = "BTC", name = "Bitcoin", symbol = "BTC"),
            CurrencyInfo.Fiat(id = "USD", name = "US Dollar", symbol = "USD", code = "USD")
        )
        viewModel.updateDataSource(mockData)
        viewModel.updateSearchKeyword("US")

        val adapterList = viewModel.adapterList.first()

        assertEquals(1, adapterList?.size)
        assertEquals("US Dollar", (adapterList?.get(0) as? CurrencyListAdapterItem.Fiat)?.name)
    }

    @Test
    fun `adapterList should filter data based on keyword with name Classic`() = runBlocking {
        createVM()

        val mockData = listOf(
            CurrencyInfo.Crypto(id = "T", name = "Tronclassic", symbol = "T"),
            CurrencyInfo.Crypto(id = "ETC", name = "Ethereum Classic", symbol = "ETC"),
        )
        viewModel.updateDataSource(mockData)
        viewModel.updateSearchKeyword("Classic")

        val adapterList = viewModel.adapterList.first()

        assertEquals(1, adapterList?.size)
    }

    @Test
    fun `adapterList should filter data based on keyword with name Ethereum`() = runBlocking {
        createVM()

        val mockData = listOf(
            CurrencyInfo.Crypto(id = "ETH", name = "Ethereum", symbol = "ETH"),
            CurrencyInfo.Crypto(id = "ETC", name = "Ethereum Classic", symbol = "ETC"),
        )
        viewModel.updateDataSource(mockData)
        viewModel.updateSearchKeyword("Ethereum")

        val adapterList = viewModel.adapterList.first()

        assertEquals(2, adapterList?.size)
    }

    @Test
    fun `adapterList should filter data based on keyword with symbol ET`() = runBlocking {
        createVM()

        val mockData = listOf(
            CurrencyInfo.Crypto(id = "ETH", name = "ETH", symbol = "ETH"),
            CurrencyInfo.Crypto(id = "ETC", name = "ETC", symbol = "ETC"),
            CurrencyInfo.Crypto(id = "ETN", name = "ETN", symbol = "ETN"),
            CurrencyInfo.Crypto(id = "BET", name = "BET", symbol = "BET"),
        )
        viewModel.updateDataSource(mockData)
        viewModel.updateSearchKeyword("ET")

        val adapterList = viewModel.adapterList.first()

        assertEquals(3, adapterList?.size)
    }

    @Test
    fun `isEmpty should be true when adapterList is empty`() = runBlocking {
        createVM()

        viewModel.updateDataSource(emptyList())

        val isEmpty = viewModel.isEmpty.first()

        assertTrue(isEmpty)
    }

    @Test
    fun `isSelectedData should be true when adapterList is not null`() = runBlocking {
        createVM()

        viewModel.updateDataSource(
            listOf(
                CurrencyInfo.Crypto(id = "BTC", name = "Bitcoin", symbol = "BTC"),
                CurrencyInfo.Fiat(id = "USD", name = "US Dollar", symbol = "USD", code = "USD")
            )
        )

        val isSelectedData = viewModel.isSelectedData.first()

        assertTrue(isSelectedData)
    }
}