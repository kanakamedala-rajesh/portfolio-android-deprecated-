package com.venkatasudha.portfolio.data

import com.google.firebase.auth.FirebaseUser
import com.venkatasudha.portfolio.entities.NetworkRequestStates
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(private val dataSource: LoginDataSource) {
    var user: FirebaseUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun performLogin(email: String, password: String): Flow<NetworkRequestStates<FirebaseUser>> {
        return dataSource.emailLogin(email, password)
    }

    private fun setLoggedInUser(loggedInUser: FirebaseUser) {
        this.user = loggedInUser
    }
}