package com.billy.glintforce.mainApplication.settingsTab.container

import android.content.Context
import com.billy.glintforce.data.toRemove.userDatabase.OfflineUserRepository
import com.billy.glintforce.data.toRemove.userDatabase.UserDatabase
import com.billy.glintforce.data.toRemove.userDatabase.UserRepository

/**
 * Interface to inject all dependencies into
 */
interface UserContainer {
    val userRepository: UserRepository
}

/**
 * Data Container to store databases needed for User Accounts
 */
class UserDataContainer(private val context: Context) : UserContainer {
    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(UserDatabase.getDatabase(context).userDao())
    }
}