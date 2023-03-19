package com.eyeque.enterprisedatamodule


sealed class APIResult<out T>(val status: ApiStatus, val data: T?, val message: String?) {
    enum class ApiStatus{
        SUCCESS, LOADING, ERROR, IDLE
    }
    data class Idle(val isEdle: Boolean): APIResult<Nothing>(ApiStatus.IDLE, null, null)
    data class Success<out R>(val _data: R): APIResult<R>(ApiStatus.SUCCESS, _data, null)
    data class Error(val exception: String): APIResult<Nothing>(ApiStatus.ERROR, null, exception)
    data class Loading(val isLoading: Boolean): APIResult<Nothing>(ApiStatus.LOADING, null, null)
}