package com.billy.glintforce.data.toRemove.userDatabase

import kotlinx.coroutines.flow.Flow

/**
 * Interface to be used by all User Repositories
 */
interface UserRepository {
    fun getUser(userId: String): Flow<User>

    suspend fun insertUser(user: User)

    suspend fun updateUserName(userId: String, userName: String)
}