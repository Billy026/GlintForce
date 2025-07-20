package com.billy.glintforce.data.expenses

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun addExpense(
    userId: String, category: String, desc: String,
    cost: Double, date: String, time: String, month: String
) {
    val expense = hashMapOf(
        "userId" to userId,
        "category" to category,
        "desc" to desc,
        "cost" to cost,
        "date" to date,
        "time" to time,
        "month" to month
    )

    Firebase.firestore.collection("expenses").document(userId)
        .collection("expense")
        .add(expense)
        .addOnSuccessListener { ref ->
            Log.d("Firestore - Expenses", "Expense added with ID:  ${ref.id}")
        }.addOnFailureListener { e ->
            Log.w("Firestore - Expenses", "Error adding expense", e)
        }
}

fun editExpense(
    expenseId: String, userId: String, category: String, desc: String, cost: Double
) {
    val path = Firebase.firestore.collection("expenses").document(userId)
        .collection("expense").document(expenseId)

    path.get()
        .addOnSuccessListener { doc ->
            if (doc.exists()) {
                val updatedExpense = mapOf(
                    "userId" to userId,
                    "category" to category,
                    "desc" to desc,
                    "cost" to cost,
                    "date" to doc.getString("date"),
                    "time" to doc.getString("time"),
                    "month" to doc.getString("month")
                )

                path.update(updatedExpense)
                    .addOnSuccessListener { _ ->
                        Log.d("Firestore - Expenses", "Expense $expenseId updated successfully")
                    }.addOnFailureListener { e ->
                        Log.w("Firestore - Expenses", "Error updating expense", e)
                    }
            }

            Log.d("Firestore - Expenses", "Expense $expenseId retrieved successfully")
        }.addOnFailureListener { e ->
            Log.w("Firestore - Expenses", "Error retrieving expense", e)
        }
}

fun deleteExpense(userId: String, expenseId: String) {
    Firebase.firestore.collection("expenses").document(userId)
        .collection("expense").document(expenseId)
        .delete()
        .addOnSuccessListener { _ ->
            Log.d("Firestore - Expenses", "Expense $expenseId deleted successfully")
        }.addOnFailureListener { e ->
            Log.w("Firestore - Expenses", "Error deleting expense", e)
        }
}