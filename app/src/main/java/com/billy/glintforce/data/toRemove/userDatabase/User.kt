package com.billy.glintforce.data.toRemove.userDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data in a User entity in User Database
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val userName: String = "User"
)
