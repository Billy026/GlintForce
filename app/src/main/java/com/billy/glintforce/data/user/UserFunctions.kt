package com.billy.glintforce.data.user

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun addUser(userId: String) {
    val user = hashMapOf(
        "userId" to userId,
        "lang" to "en",
        "darkTheme" to false,
        "showWidget" to true,
        "report" to 0,
        "dateGenerated" to "",
        "reportGenerated" to false
    )

    Firebase.firestore.collection("user").document(userId)
        .set(user)
        .addOnSuccessListener { _ ->
            Log.d("Firestore - User", "User added with ID: $userId")
        }.addOnFailureListener { e ->
            Log.w("Firestore - User", "Error adding user", e)
        }
}

fun editUser(userId: String, field: UserFields, value: Any) {
    val path = Firebase.firestore.collection("user").document(userId)

    path.get()
        .addOnSuccessListener { doc ->
            if (doc.exists()) {
                try {
                    val updatedUser = mapOf(
                        "userId" to if (field == UserFields.USERID) {
                            value as String
                        } else {
                            doc.data?.get("userId")
                        },
                        "lang" to if (field == UserFields.LANG) {
                            value as String
                        } else {
                            doc.data?.get("lang")
                        },
                        "darkTheme" to if (field == UserFields.DARKTHEME) {
                            value as Boolean
                        } else {
                            doc.data?.get("darkTheme")
                        },
                        "showWidget" to if (field == UserFields.SHOWWIDGET) {
                            value as Boolean
                        } else {
                            doc.data?.get("showWidget")
                        },
                        "report" to if (field == UserFields.REPORT) {
                            value as Int
                        } else {
                            doc.data?.get("report")
                        },
                        "dateGenerated" to if (field == UserFields.DATEGENERATED) {
                            value as String
                        } else {
                            doc.data?.get("dateGenerated")
                        },
                        "reportGenerated" to if (field == UserFields.REPORTGENERATED) {
                            value as Boolean
                        } else {
                            doc.data?.get("reportGenerated")
                        }
                    )

                    path.update(updatedUser)
                        .addOnSuccessListener { _ ->
                            Log.d("Firestore - User", "User $userId updated successfully")
                        }.addOnFailureListener { e ->
                            Log.w("Firestore - User", "Error updating user", e)
                        }
                } catch (e: ClassCastException) {
                    Log.w("Firestore - User", "Error casting value", e)
                }
            } else {
                Log.w("Firestore - User", "No such user")
            }
        }.addOnFailureListener { e ->
            Log.w("Firestore - User", "Error checking for user", e)
        }
}

fun deleteUser(userId: String) {
    Firebase.firestore.collection("user").document(userId)
        .delete()
        .addOnSuccessListener { _ ->
            Log.d("Firestore - User", "User $userId deleted successfully")
        }.addOnFailureListener { e ->
            Log.w("Firestore - User", "Error deleting user", e)
        }
}

/**
 * Function to add User entry for existing users.
 * Used for transition from local database to Firestore.
 */
fun addUserIfNull(userId: String) {
    Firebase.firestore.collection("user").document(userId)
        .get()
        .addOnSuccessListener { doc ->
            if (!doc.exists()) addUser(userId)
        }.addOnFailureListener { e ->
            Log.w("Firestore - User", "Error checking for user", e)
        }
}