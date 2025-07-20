package com.billy.glintforce.viewModel.settings.initiateRepo

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferenceViewModel : ViewModel() {
    private var _userPreferenceList = MutableStateFlow<List<UserPreferences>>(emptyList())
    var userPreferenceList = _userPreferenceList.asStateFlow()

    init {
        getUserPreferenceList()
    }

    private fun getUserPreferenceList() {
        val db = Firebase.firestore

        db.collection("user_prefs")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (value != null) {
                    _userPreferenceList.value = value.toObjects()
                    Log.d("Firestore", "Database successfully retrieved")
                }
            }
    }
}