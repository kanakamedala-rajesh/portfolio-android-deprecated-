package com.venkatasudha.portfolio.android.entities

sealed class NetworkRequestStates<out T : Any> {
    object Loading : NetworkRequestStates<Nothing>()
    data class Success<out T : Any>(val data: T) : NetworkRequestStates<T>()
    data class Failed(val exception: Exception, val cause: NetworkFailureCauses? = null) : NetworkRequestStates<Nothing>()


    enum class NetworkFailureCauses {
        LOGIN_FAILURE_INVALID_USER,
        LOGIN_FAILURE_INVALID_CREDENTIALS,
        LOGIN_FAILURE_RE_AUTHENTICATE,
        LOGIN_FAILURE_TOO_MANY_REQUESTS,
        LOGIN_FAILURE_OTHER,
    }
}