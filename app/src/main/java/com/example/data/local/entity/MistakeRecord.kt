package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mistake_records",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["quizResultId"]),
        Index(value = ["factor1", "factor2"]),
        Index(value = ["timestamp"])
    ]
)
data class MistakeRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val quizResultId: Long,
    val factor1: Int,
    val factor2: Int,
    val wrongAnswer: Int?,
    val correctAnswer: Int,
    val timestamp: Long = System.currentTimeMillis()
)
