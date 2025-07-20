package com.billy.glintforce.viewModel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.billy.glintforce.data.user.addUserIfNull
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    private val db = FirebaseFirestore.getInstance()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    // Remove when current user class added
    fun getUserId() : String {
        return auth.currentUser?.uid ?: ""
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    private fun checkAuthStatus() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated(currentUser.uid)
        }
    }

    /**
     * Logs in the user with the given valid email and password.
     */
    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be blank")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated(auth.currentUser?.uid ?: "")
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    /**
     * Signs up the user with the given valid email and password.
     */
    fun signup(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be blank")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated(auth.currentUser?.uid ?: "")
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun sendPasswordResetEmail(email: String, isLoggedIn: Boolean) {
        if (email.isEmpty()) {
            _authState.value = AuthState.Error("Email can't be blank")
            return
        }

        _authState.value = AuthState.Loading
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = when {
                        isLoggedIn -> { AuthState.Authenticated(auth.currentUser?.uid?:"") }
                        else -> { AuthState.PasswordReset }
                    }
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    /**
     * Sets the preferred user preferences, as well as navigates to Home page.
     */
    fun transitionToHome(navigateHome: () -> Unit) {
        addUserIfNull(auth.currentUser?.uid ?: "")
        navigateHome()
    }
}

sealed class AuthState {
    data class Authenticated(val uid: String): AuthState()
    data object Unauthenticated: AuthState()
    data object Loading: AuthState()
    data object PasswordReset: AuthState()
    data class Error(val message: String): AuthState()
}
