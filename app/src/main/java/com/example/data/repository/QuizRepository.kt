package com.example.data.repository

import com.example.data.local.dao.MistakeCountTuple
import com.example.data.local.dao.QuizDao
import com.example.data.local.dao.TableStatTuple
import com.example.data.local.entity.MistakeRecord
import com.example.data.local.entity.QuizResult
import com.example.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

class QuizRepository(private val quizDao: QuizDao) {

    val allProfiles: Flow<List<UserProfile>> = quizDao.getAllProfiles()
    val currentProfile: Flow<UserProfile?> = quizDao.getCurrentProfile()

    suspend fun getProfile(id: Long): UserProfile? = quizDao.getProfileById(id)

    suspend fun createProfile(name: String, grade: String, avatarIndex: Int, pinHash: String?): Long {
        val newProfile = UserProfile(
            name = name,
            gradeOrLevel = grade,
            avatarIndex = avatarIndex,
            pinHash = pinHash,
            isCurrent = false
        )
        return quizDao.insertProfile(newProfile)
    }

    suspend fun switchProfile(id: Long) {
        quizDao.switchCurrentProfile(id)
    }

    suspend fun updateProfile(profile: UserProfile) {
        quizDao.updateProfile(profile)
    }

    suspend fun deleteProfile(id: Long) {
        quizDao.deleteProfile(id)
    }

    suspend fun saveQuizSession(
        userId: Long,
        mode: String,
        tableNumber: Int,
        score: Int,
        totalQuestions: Int,
        difficulty: String,
        language: String,
        totalTimeMs: Long,
        mistakes: List<Pair<QuizResult, MistakeRecord>>
    ): Long {
        val result = QuizResult(
            userId = userId,
            mode = mode,
            tableNumber = tableNumber,
            score = score,
            totalQuestions = totalQuestions,
            difficulty = difficulty,
            language = language,
            totalTimeMs = totalTimeMs
        )
        val resultId = quizDao.insertQuizResult(result)

        if (mistakes.isNotEmpty()) {
            val records = mistakes.map { it.second.copy(quizResultId = resultId, userId = userId) }
            quizDao.insertMistakes(records)
        }
        return resultId
    }

    fun getRecentResults(userId: Long, limit: Int = 20): Flow<List<QuizResult>> =
        quizDao.getRecentResults(userId, limit)

    fun getTotalTestsCount(userId: Long): Flow<Int> =
        quizDao.getTotalTestsCount(userId)

    fun getTotalQuestionsCount(userId: Long): Flow<Int?> =
        quizDao.getTotalQuestionsCount(userId)

    fun getTotalScoreCount(userId: Long): Flow<Int?> =
        quizDao.getTotalScoreCount(userId)

    fun getAverageResponseTimePerQuestionMs(userId: Long): Flow<Double?> =
        quizDao.getAverageResponseTimePerQuestionMs(userId)

    fun getTableStats(userId: Long): Flow<List<TableStatTuple>> =
        quizDao.getTableStats(userId)

    fun getTopMistakes(userId: Long, limit: Int = 10): Flow<List<MistakeCountTuple>> =
        quizDao.getTopMistakes(userId, limit)

    suspend fun clearMistake(userId: Long, f1: Int, f2: Int) =
        quizDao.clearMistakePair(userId, f1, f2)

    suspend fun clearAllMistakes(userId: Long) =
        quizDao.clearAllMistakes(userId)
}
