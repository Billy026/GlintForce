package com.billy.glintforce.data.toRemove.userDatabase

import kotlinx.coroutines.flow.Flow

/**
 * Offline instance of User Repository
 */
class OfflineUserRepository(private val userDao: UserDAO): UserRepository {
    override fun getUser(userId: String): Flow<User> = userDao.getUser(userId)

    override suspend fun insertUser(user: User) = userDao.insertUser(user)

    override suspend fun updateUserName(userId: String, userName: String) = userDao.updateUserName(userId, userName)
}

