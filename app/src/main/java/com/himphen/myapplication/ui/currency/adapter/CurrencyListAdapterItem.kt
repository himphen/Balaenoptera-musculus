package com.himphen.myapplication.ui.currency.adapter

sealed class CurrencyListAdapterItem {
    data class Crypto(
        val id: String,
        val name: String,
        val symbol: String,
    ) : CurrencyListAdapterItem()

    data class Fiat(
        val id: String,
        val name: String,
        val symbol: String,
        val code: String,
    ) : CurrencyListAdapterItem()
}