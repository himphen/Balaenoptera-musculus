package com.himphen.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CurrencyInfo(
    open val id: String,
    open val name: String,
    open val symbol: String,
) : Parcelable {

    @Parcelize
    data class Crypto(
        override val id: String,
        override val name: String,
        override val symbol: String,
    ) : CurrencyInfo(id, name, symbol)

    @Parcelize
    data class Fiat(
        override val id: String,
        override val name: String,
        override val symbol: String,
        val code: String,
    ) : CurrencyInfo(id, name, symbol)
}