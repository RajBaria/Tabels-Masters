package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.local.entity.MistakeRecord
import com.example.data.local.entity.QuizResult
import com.example.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

data class MistakeCountTuple(
    val factor1: Int,
    val factor2: Int,
    val correctAnswer: Int,
    val count: Int
)

data class TableStatTuple(
    val tableNumber: Int,
    val totalScore: Int,
    val totalPossible: Int,
    val count: Int
)

@Dao
interface QuizDao {

    // --- Profiles ---
    @Query("SELECT * FROM user_profiles ORDER BY createdAt ASC")
    fun getAllProfiles(): Flow<List<UserProfile>>

    @Query("SELECT * FROM user_profiles WHERE isCurrent = 1 LIMIT 1")
    fun getCurrentProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE id = :id LIMIT 1")
    suspend fun getProfileById(id: Long): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile): Long

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("UPDATE user_profiles SET isCurrent = 0")
    suspend fun clearCurrentProfile()

    @Query("UPDATE user_profiles SET isCurrent = 1 WHERE id = :id")
    suspend fun setCurrentProfileInternal(id: Long)

    @Transaction
    suspend fun switchCurrentProfile(id: Long) {
        clearCurrentProfile()
        setCurrentProfileInternal(id)
    }

    @Query("DELETE FROM user_profiles WHERE id = :id")
    suspend fun deleteProfile(id: Long)

    // --- Quiz Results ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResult): Long

    @Query("SELECT * FROM quiz_results WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentResults(userId: Long, limit: Int = 20): Flow<List<QuizResult>>

    @Query("SELECT COUNT(*) FROM quiz_results WHERE userId = :userId")
    fun getTotalTestsCount(userId: Long): Flow<Int>

    @Query("SELECT SUM(totalQuestions) FROM quiz_results WHERE userId = :userId")
    fun getTotalQuestionsCount(userId: Long): Flow<Int?>

    @Query("SELECT SUM(score) FROM quiz_results WHERE userId = :userId")
    fun getTotalScoreCount(userId: Long): Flow<Int?>

    @Query("SELECT AVG(totalTimeMs * 1.0 / totalQuestions) FROM quiz_results WHERE userId = :userId AND totalQuestions > 0")
    fun getAverageResponseTimePerQuestionMs(userId: Long): Flow<Double?>

    @Query("SELECT tableNumber, SUM(score) as totalScore, SUM(totalQuestions) as totalPossible, COUNT(*) as count FROM quiz_results WHERE userId = :userId AND tableNumber > 0 GROUP BY tableNumber")
    fun getTableStats(userId: Long): Flow<List<TableStatTuple>>

    // --- Mistakes ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistakes(mistakes: List<MistakeRecord>)

    @Query("""
        SELECT factor1, factor2, correctAnswer, COUNT(*) as count 
        FROM mistake_records 
        WHERE userId = :userId 
        GROUP BY factor1, factor2, correctAnswer 
        ORDER BY count DESC 
        LIMIT :limit
    """)
    fun getTopMistakes(userId: Long, limit: Int = 10): Flow<List<MistakeCountTuple>>

    @Query("DELETE FROM mistake_records WHERE userId = :userId AND factor1 = :f1 AND factor2 = :f2")
    suspend fun clearMistakePair(userId: Long, f1: Int, f2: Int)

    @Query("DELETE FROM mistake_records WHERE userId = :userId")
    suspend fun clearAllMistakes(userId: Long)
}
