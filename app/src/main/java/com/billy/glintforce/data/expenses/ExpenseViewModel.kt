package com.billy.glintforce.data.expenses

import android.util.Log
import androidx.lifecycle.ViewModel
import com.billy.glintforce.data.expenses.Expenses
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpenseViewModel : ViewModel() {
    private var _expenseList = MutableStateFlow<List<Expenses>>(emptyList())
    var expenseList = _expenseList.asStateFlow()

    init {
        getExpenseList()
    }

    private fun getExpenseList() {
        val db = Firebase.firestore

        db.collection("expenses")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (value != null) {
                    _expenseList.value = value.toObjects()
                    Log.d("Firestore", "Expenses database successfully retrieved")
                }
            }
    }
}