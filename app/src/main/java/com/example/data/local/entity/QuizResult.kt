package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quiz_results",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["timestamp"]),
        Index(value = ["tableNumber"])
    ]
)
data class QuizResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val mode: String, // "PALAKHA" or "GADIYA"
    val tableNumber: Int, // 0 for random, or 1..100
    val score: Int,
    val totalQuestions: Int,
    val difficulty: String, // "easy", "medium", "hard", "lightning"
    val language: String,
    val totalTimeMs: Long,
    val timestamp: Long = System.currentTimeMillis()
)
