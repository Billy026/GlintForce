package com.billy.glintforce.viewModel.limit.initiateRepo

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GoalTypesViewModel : ViewModel() {
    private var _goalTypeList = MutableStateFlow<List<GoalTypes>>(emptyList())
    var goalTypeList = _goalTypeList.asStateFlow()

    init {
        getGoalTypeList()
    }

    private fun getGoalTypeList() {
        val db = Firebase.firestore

        db.collection("goal_types")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (value != null) {
                    _goalTypeList.value = value.toObjects()
                    Log.d("Firestore", "Database successfully retrieved")
                }
            }
    }
}