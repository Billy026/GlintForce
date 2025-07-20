package com.billy.glintforce.viewModel.settings.initiateRepo

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {
    private var _userList = MutableStateFlow<List<User>>(emptyList())
    var userList = _userList.asStateFlow()

    init {
        getUserList()
    }

    private fun getUserList() {
        val db = Firebase.firestore

        db.collection("user")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (value != null) {
                    _userList.value = value.toObjects()
                    Log.d("Firestore", "Database successfully retrieved")
                }
            }
    }
}