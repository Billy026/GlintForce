package com.billy.glintforce.viewModel.limit.initiateRepo

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GoalViewModel : ViewModel() {
    private var _goalList = MutableStateFlow<List<Goals>>(emptyList())
    var goalList = _goalList.asStateFlow()

    init {
        getGoalList()
    }

    private fun getGoalList() {
        val db = Firebase.firestore

        db.collection("goals")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (value != null) {
                    _goalList.value = value.toObjects()
                    Log.d("Firestore", "Database successfully retrieved")
                }
            }
    }
}