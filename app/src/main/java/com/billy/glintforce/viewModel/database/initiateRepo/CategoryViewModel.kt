package com.billy.glintforce.viewModel.database.initiateRepo

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewModel : ViewModel() {
    private var _categoryList = MutableStateFlow<List<Categories>>(emptyList())
    var categoryList = _categoryList.asStateFlow()

    init {
        getCategoryList()
    }

    private fun getCategoryList() {
        val db = Firebase.firestore

        db.collection("categories")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (value != null) {
                    _categoryList.value = value.toObjects()
                    Log.d("Firestore", "Database successfully retrieved")
                }
            }
    }
}