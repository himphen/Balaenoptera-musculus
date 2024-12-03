package com.himphen.myapplication.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {

    protected val <T> SharedFlow<T>.mutable: MutableSharedFlow<T>
        get() = this as MutableSharedFlow<T>

    protected val <T> StateFlow<T>.mutable: MutableStateFlow<T>
        get() = this as MutableStateFlow<T>

}