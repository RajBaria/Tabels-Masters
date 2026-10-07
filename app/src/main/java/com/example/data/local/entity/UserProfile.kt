package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val gradeOrLevel: String = "Student",
    val avatarIndex: Int = 0,
    val pinHash: String? = null,
    val isCurrent: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
