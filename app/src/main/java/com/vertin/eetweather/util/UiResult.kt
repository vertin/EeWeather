package com.vertin.eetweather.util

import com.vertin.eetweather.domain.model.InternalFail

sealed class UiResult<T> {
    class Success<T>(val t: T) : UiResult<T>()
    class Loading<T> : UiResult<T>()
    class Failed<T>(val err: InternalFail) : UiResult<T>()
}