package com.venkatasudha.portfolio.android.data

import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.venkatasudha.portfolio.android.entities.NetworkRequestStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(private val auth: FirebaseAuth) {

    suspend fun emailLogin(email: String, password: String) = flow {
        emit(NetworkRequestStates.Loading)
        val loginResultTask = auth.signInWithEmailAndPassword(email, password).await()
        Timber.i("Login Success")
        emit(NetworkRequestStates.Success(data = loginResultTask.user!!))
    }.catch {
        Timber.e("Login Failed Exception $it")
        emit(
            NetworkRequestStates.Failed(
                it as Exception,
                when (it) {
                    is FirebaseAuthInvalidCredentialsException -> NetworkRequestStates.NetworkFailureCauses.LOGIN_FAILURE_INVALID_CREDENTIALS
                    is FirebaseAuthInvalidUserException -> NetworkRequestStates.NetworkFailureCauses.LOGIN_FAILURE_INVALID_USER
                    is FirebaseAuthRecentLoginRequiredException -> NetworkRequestStates.NetworkFailureCauses.LOGIN_FAILURE_RE_AUTHENTICATE
                    is FirebaseTooManyRequestsException -> NetworkRequestStates.NetworkFailureCauses.LOGIN_FAILURE_TOO_MANY_REQUESTS
                    else -> {
                        NetworkRequestStates.NetworkFailureCauses.LOGIN_FAILURE_OTHER
                    }
                }
            )
        )
    }.flowOn(Dispatchers.IO)

    fun logout() {
        // TODO: revoke authentication
    }
}
