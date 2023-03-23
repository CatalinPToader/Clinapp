package com.catalin.clinapp.data

import com.catalin.clinapp.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            if (username == "user" && password == "user")
                Result.Success(LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe"))
            else if (username == "medic" && password == "medic")
                Result.Success(LoggedInUser(java.util.UUID.randomUUID().toString(), "Medic John Doe"))
            else
                Result.Error(Exception("Invalid user"))
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}