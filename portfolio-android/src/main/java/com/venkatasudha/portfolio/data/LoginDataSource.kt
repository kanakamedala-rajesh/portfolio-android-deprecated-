package com.venkatasudha.portfolio.data

import com.google.firebase.auth.FirebaseAuth
import com.venkatasudha.portfolio.entities.LoggedInUser
import com.venkatasudha.portfolio.entities.Result
import java.io.IOException
import java.util.*
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(private val auth: FirebaseAuth) {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), username.split("@")[0])
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}