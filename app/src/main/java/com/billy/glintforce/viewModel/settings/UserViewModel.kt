package com.billy.glintforce.viewModel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billy.glintforce.data.toRemove.userDatabase.User
import com.billy.glintforce.data.toRemove.userDatabase.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val userId: String
) : ViewModel() {
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    init {
        loadUserName(userId)
    }

    fun loadUserName(userId: String) {
        viewModelScope.launch {
            userRepository.getUser(userId).collect { user ->
                if (user != null){
                    _userName.value = user.userName
                } else {
                    // Handle the case where the user is not found, i.e. when the user is first time logging in the app
                    createUser(userId, "User")
                }
            }
        }
    }

    private fun createUser(userId: String, userName: String) {
        viewModelScope.launch {
            val user = User(userId = userId, userName = userName)
            userRepository.insertUser(user)
            _userName.value = userName
        }
    }

    fun updateUserName(newUserName: String) {
        viewModelScope.launch {
            userRepository.updateUserName(userId, newUserName)
            _userName.value = newUserName
        }
    }
}