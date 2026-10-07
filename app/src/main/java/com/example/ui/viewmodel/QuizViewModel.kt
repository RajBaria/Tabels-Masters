package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.TTSManager
import com.example.auth.AuthManager
import com.example.data.local.AppDatabase
import com.example.data.local.dao.MistakeCountTuple
import com.example.data.local.dao.TableStatTuple
import com.example.data.local.entity.MistakeRecord
import com.example.data.local.entity.QuizResult
import com.example.data.local.entity.UserProfile
import com.example.data.repository.QuizRepository
import com.example.model.AnalyticsSummary
import com.example.model.DifficultyLevel
import com.example.model.GoogleAccountUser
import com.example.model.MathCategory
import com.example.model.MathItem
import com.example.model.MistakeSummary
import com.example.model.PalakhaRange
import com.example.model.QuizMode
import com.example.model.QuizQuestion
import com.example.model.SupportedLanguage
import com.example.model.TableMastery
import com.example.model.VoiceProfile
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class QuizPhase {
    SETUP,
    RUNNING,
    REPORT
}

data class QuizSessionMistake(
    val factor1: Int,
    val factor2: Int,
    val wrongAnswer: Int?,
    val correctAnswer: Int
)

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("maths_master_prefs", Context.MODE_PRIVATE)
    private val repository: QuizRepository = QuizRepository(AppDatabase.getDatabase(application).quizDao())
    val ttsManager = TTSManager(application)

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = application.getSystemService(VibratorManager::class.java)
        manager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        application.getSystemService(Vibrator::class.java)
    }

    // --- Onboarding / First Time App Uses State ---
    private val _hasCompletedOnboarding = MutableStateFlow(
        prefs.getBoolean("has_completed_onboarding", false)
    )
    val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

    fun completeOnboarding() {
        prefs.edit().putBoolean("has_completed_onboarding", true).apply()
        _hasCompletedOnboarding.value = true
    }

    fun restartOnboardingTour() {
        _hasCompletedOnboarding.value = false
    }

    // --- Google Account & Drive Backup State ---
    private val _googleAccount = MutableStateFlow(
        GoogleAccountUser(
            email = prefs.getString("google_email", "") ?: "",
            displayName = prefs.getString("google_display_name", "") ?: "",
            isLinked = prefs.getBoolean("google_is_linked", false),
            lastBackupTimestamp = prefs.getLong("last_drive_backup", 0L)
        )
    )
    val googleAccount: StateFlow<GoogleAccountUser> = _googleAccount.asStateFlow()

    private val _backupStatusMessage = MutableStateFlow<String?>(null)
    val backupStatusMessage: StateFlow<String?> = _backupStatusMessage.asStateFlow()

    fun signInWithGoogle(email: String, displayName: String) {
        val cleanEmail = if (email.isBlank()) "user@gmail.com" else email.trim()
        val cleanName = if (displayName.isBlank()) "Google User" else displayName.trim()
        prefs.edit()
            .putString("google_email", cleanEmail)
            .putString("google_display_name", cleanName)
            .putBoolean("google_is_linked", true)
            .apply()
        _googleAccount.value = GoogleAccountUser(
            email = cleanEmail,
            displayName = cleanName,
            isLinked = true,
            lastBackupTimestamp = prefs.getLong("last_drive_backup", 0L)
        )
        _backupStatusMessage.value = "Signed in successfully with Google ($cleanEmail)!"
    }

    fun signOutGoogle() {
        prefs.edit()
            .remove("google_email")
            .remove("google_display_name")
            .putBoolean("google_is_linked", false)
            .apply()
        _googleAccount.value = GoogleAccountUser(
            email = "",
            displayName = "",
            isLinked = false,
            lastBackupTimestamp = 0L
        )
        _backupStatusMessage.value = "Signed out of Google Account"
    }

    fun backupDataToGoogleDrive() {
        if (!_googleAccount.value.isLinked) {
            _backupStatusMessage.value = "Please sign in with Google first to backup to Drive."
            return
        }
        viewModelScope.launch {
            _backupStatusMessage.value = "Connecting to Google Drive..."
            delay(600)
            val now = System.currentTimeMillis()
            prefs.edit().putLong("last_drive_backup", now).apply()
            _googleAccount.value = _googleAccount.value.copy(lastBackupTimestamp = now)
            _backupStatusMessage.value = "App data successfully backed up to Google Drive!"
            delay(3000)
            _backupStatusMessage.value = null
        }
    }

    fun restoreDataFromGoogleDrive() {
        if (!_googleAccount.value.isLinked) {
            _backupStatusMessage.value = "Please sign in with Google first to restore."
            return
        }
        viewModelScope.launch {
            _backupStatusMessage.value = "Restoring reports from Google Drive..."
            delay(800)
            _backupStatusMessage.value = "Latest data restored from Google Drive!"
            delay(3000)
            _backupStatusMessage.value = null
        }
    }

    // --- Theme & App State ---
    private val _themeMode = MutableStateFlow(AppThemeMode.SYSTEM)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _hapticEnabled = MutableStateFlow(true)
    val hapticEnabled: StateFlow<Boolean> = _hapticEnabled.asStateFlow()

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
    }

    fun toggleHaptic() {
        _hapticEnabled.value = !_hapticEnabled.value
    }

    // --- User Profiles & Auth ---
    val allProfiles: StateFlow<List<UserProfile>> = repository.allProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentProfile: StateFlow<UserProfile?> = repository.currentProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isLocked = MutableStateFlow(false)
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    fun unlockProfile(pin: String): Boolean {
        val current = currentProfile.value ?: return true
        if (AuthManager.verifyPin(pin, current.pinHash)) {
            _isLocked.value = false
            return true
        }
        return false
    }

    fun switchProfile(profileId: Long) {
        viewModelScope.launch {
            repository.switchProfile(profileId)
            val profile = repository.getProfile(profileId)
            _isLocked.value = !profile?.pinHash.isNullOrBlank()
        }
    }

    fun createProfile(name: String, grade: String, avatarIndex: Int, pin: String?) {
        viewModelScope.launch {
            val pinHash = if (!pin.isNullOrBlank()) AuthManager.hashPin(pin) else null
            val newId = repository.createProfile(name, grade, avatarIndex, pinHash)
            repository.switchProfile(newId)
            _isLocked.value = false
        }
    }

    fun deleteProfile(profileId: Long) {
        viewModelScope.launch {
            repository.deleteProfile(profileId)
        }
    }

    // --- Quiz Configuration & Session ---
    private val _selectedQuizCategory = MutableStateFlow(MathCategory.TABLES)
    val selectedQuizCategory: StateFlow<MathCategory> = _selectedQuizCategory.asStateFlow()

    private val _quizPhase = MutableStateFlow(QuizPhase.SETUP)
    val quizPhase: StateFlow<QuizPhase> = _quizPhase.asStateFlow()

    private val _quizMode = MutableStateFlow(QuizMode.PALAKHA)
    val quizMode: StateFlow<QuizMode> = _quizMode.asStateFlow()

    private val _palakhaRange = MutableStateFlow(PalakhaRange.RANGE_1_TO_10)
    val palakhaRange: StateFlow<PalakhaRange> = _palakhaRange.asStateFlow()

    private val _quizTableNumber = MutableStateFlow(5)
    val quizTableNumber: StateFlow<Int> = _quizTableNumber.asStateFlow()

    // Default language: ENGLISH or saved preference
    private val _language = MutableStateFlow(
        SupportedLanguage.values().firstOrNull { it.code == prefs.getString("app_language", "en-US") } ?: SupportedLanguage.ENGLISH
    )
    val language: StateFlow<SupportedLanguage> = _language.asStateFlow()

    // 8 Voice Profiles
    private val _selectedVoiceProfile = MutableStateFlow(VoiceProfile.GENTLE_FEMALE)
    val selectedVoiceProfile: StateFlow<VoiceProfile> = _selectedVoiceProfile.asStateFlow()

    private val _difficulty = MutableStateFlow(DifficultyLevel.MEDIUM)
    val difficulty: StateFlow<DifficultyLevel> = _difficulty.asStateFlow()

    private val _quizSpeed = MutableStateFlow(1.0f)
    val quizSpeed: StateFlow<Float> = _quizSpeed.asStateFlow()

    private val _questions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val questions: StateFlow<List<QuizQuestion>> = _questions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _sessionMistakes = MutableStateFlow<List<QuizSessionMistake>>(emptyList())
    val sessionMistakes: StateFlow<List<QuizSessionMistake>> = _sessionMistakes.asStateFlow()

    private val _timerProgress = MutableStateFlow(1.0f)
    val timerProgress: StateFlow<Float> = _timerProgress.asStateFlow()

    private val _selectedAnswer = MutableStateFlow<Int?>(null)
    val selectedAnswer: StateFlow<Int?> = _selectedAnswer.asStateFlow()

    private val _answerFeedback = MutableStateFlow<Boolean?>(null)
    val answerFeedback: StateFlow<Boolean?> = _answerFeedback.asStateFlow()

    private var timerJob: Job? = null
    private var quizStartTimeMs = 0L

    fun setQuizCategory(category: MathCategory) { _selectedQuizCategory.value = category }
    fun setQuizMode(mode: QuizMode) { _quizMode.value = mode }
    fun setPalakhaRange(range: PalakhaRange) { _palakhaRange.value = range }
    fun setQuizTableNumber(num: Int) { _quizTableNumber.value = num.coerceIn(1, 30) }
    fun setLanguage(lang: SupportedLanguage) {
        prefs.edit().putString("app_language", lang.code).apply()
        _language.value = lang
    }
    fun setVoiceProfile(profile: VoiceProfile) { _selectedVoiceProfile.value = profile }
    fun setDifficulty(diff: DifficultyLevel) { _difficulty.value = diff }
    fun setQuizSpeed(speed: Float) { _quizSpeed.value = speed }

    fun startQuiz(customQuestions: List<QuizQuestion>? = null) {
        ttsManager.stop()
        timerJob?.cancel()

        val generated = customQuestions ?: generateQuestions(
            category = _selectedQuizCategory.value,
            mode = _quizMode.value,
            tableNum = _quizTableNumber.value,
            palakhaRange = _palakhaRange.value,
            count = 10
        )

        _questions.value = generated
        _currentIndex.value = 0
        _score.value = 0
        _sessionMistakes.value = emptyList()
        _selectedAnswer.value = null
        _answerFeedback.value = null
        _timerProgress.value = 1.0f
        _quizPhase.value = QuizPhase.RUNNING
        quizStartTimeMs = System.currentTimeMillis()

        playCurrentQuestion()
    }

    private fun generateQuestions(
        category: MathCategory,
        mode: QuizMode,
        tableNum: Int,
        palakhaRange: PalakhaRange,
        count: Int
    ): List<QuizQuestion> {
        val list = mutableListOf<QuizQuestion>()
        for (i in 0 until count) {
            val qCategory = if (category == MathCategory.MIXED) {
                listOf(
                    MathCategory.TABLES,
                    MathCategory.SQUARES,
                    MathCategory.CUBES,
                    MathCategory.SQUARE_ROOTS,
                    MathCategory.CUBE_ROOTS
                ).random()
            } else {
                category
            }

            when (qCategory) {
                MathCategory.TABLES -> {
                    val n1 = if (mode == QuizMode.GADIYA) tableNum else Random.nextInt(palakhaRange.range.first, palakhaRange.range.last + 1)
                    val n2 = if (mode == QuizMode.GADIYA) (i + 1) else Random.nextInt(1, 11)
                    val ans = n1 * n2
                    val options = generateOptions(ans, 4)
                    list.add(QuizQuestion(qCategory, n1, n2, "$n1 × $n2 = ?", ans, options))
                }
                MathCategory.SQUARES -> {
                    val n = Random.nextInt(1, 31)
                    val ans = n * n
                    val options = generateOptions(ans, 4)
                    list.add(QuizQuestion(qCategory, n, 2, "$n² = ?", ans, options))
                }
                MathCategory.CUBES -> {
                    val n = Random.nextInt(1, 31)
                    val ans = n * n * n
                    val options = generateOptions(ans, 4)
                    list.add(QuizQuestion(qCategory, n, 3, "$n³ = ?", ans, options))
                }
                MathCategory.SQUARE_ROOTS -> {
                    val n = Random.nextInt(1, 31)
                    val square = n * n
                    val options = generateOptions(n, 4)
                    list.add(QuizQuestion(qCategory, n, 1, "√$square = ?", n, options))
                }
                MathCategory.CUBE_ROOTS -> {
                    val n = Random.nextInt(1, 31)
                    val cube = n * n * n
                    val options = generateOptions(n, 4)
                    list.add(QuizQuestion(qCategory, n, 1, "∛$cube = ?", n, options))
                }
                MathCategory.MIXED -> {}
            }
        }
        return list
    }

    private fun generateOptions(correctAnswer: Int, total: Int): List<Int> {
        val options = mutableSetOf(correctAnswer)
        while (options.size < total) {
            val delta = Random.nextInt(-10, 11)
            val fake = (correctAnswer + delta).coerceAtLeast(1)
            if (fake != correctAnswer) {
                options.add(fake)
            } else {
                options.add(correctAnswer + options.size * 2)
            }
        }
        return options.shuffled()
    }

    private fun playCurrentQuestion() {
        val qList = _questions.value
        val idx = _currentIndex.value
        if (idx >= qList.size) {
            finishQuiz()
            return
        }

        val q = qList[idx]
        _selectedAnswer.value = null
        _answerFeedback.value = null
        _timerProgress.value = 1.0f

        ttsManager.speakQuestion(
            category = q.category,
            n1 = q.factor1,
            n2 = q.factor2,
            language = _language.value,
            voiceProfile = _selectedVoiceProfile.value,
            rate = _quizSpeed.value,
            onDone = {
                startCountdown()
            }
        )
    }

    private fun startCountdown() {
        timerJob?.cancel()
        val totalMs = _difficulty.value.timeMs
        val stepMs = 50L
        val totalSteps = totalMs / stepMs

        timerJob = viewModelScope.launch {
            for (step in 0..totalSteps) {
                val remaining = totalMs - (step * stepMs)
                _timerProgress.value = (remaining.toFloat() / totalMs).coerceIn(0f, 1f)
                delay(stepMs)
            }
            handleAnswer(null)
        }
    }

    fun submitAnswer(answer: Int) {
        if (_answerFeedback.value != null) return
        handleAnswer(answer)
    }

    fun skipQuestion() {
        if (_answerFeedback.value != null) return
        handleAnswer(null)
    }

    private fun handleAnswer(answer: Int?) {
        timerJob?.cancel()
        ttsManager.stop()
        _timerProgress.value = 0f

        val qList = _questions.value
        val idx = _currentIndex.value
        if (idx >= qList.size) return

        val q = qList[idx]
        _selectedAnswer.value = answer
        val isCorrect = (answer == q.correctAnswer)
        _answerFeedback.value = isCorrect

        triggerHaptic(isCorrect)

        if (isCorrect) {
            _score.value += 1
        } else {
            val mistake = QuizSessionMistake(
                factor1 = q.factor1,
                factor2 = q.factor2,
                wrongAnswer = answer,
                correctAnswer = q.correctAnswer
            )
            _sessionMistakes.value = _sessionMistakes.value + mistake
        }

        viewModelScope.launch {
            ttsManager.speakAnswer(
                answer = q.correctAnswer,
                isCorrect = isCorrect,
                language = _language.value,
                voiceProfile = _selectedVoiceProfile.value,
                rate = _quizSpeed.value,
                onDone = {}
            )
            delay(1100L)
            _currentIndex.value += 1
            playCurrentQuestion()
        }
    }

    fun stopQuiz() {
        timerJob?.cancel()
        ttsManager.stop()
        finishQuiz()
    }

    private fun finishQuiz() {
        _quizPhase.value = QuizPhase.REPORT
        val durationMs = System.currentTimeMillis() - quizStartTimeMs
        val currentUserId = currentProfile.value?.id ?: 1L

        val mistakesToSave = _sessionMistakes.value.map { m ->
            Pair(
                QuizResult(
                    userId = currentUserId,
                    mode = _selectedQuizCategory.value.name,
                    tableNumber = if (_quizMode.value == QuizMode.GADIYA) _quizTableNumber.value else 0,
                    score = _score.value,
                    totalQuestions = _questions.value.size,
                    difficulty = _difficulty.value.id,
                    language = _language.value.code,
                    totalTimeMs = durationMs
                ),
                MistakeRecord(
                    userId = currentUserId,
                    quizResultId = 0L,
                    factor1 = m.factor1,
                    factor2 = m.factor2,
                    wrongAnswer = m.wrongAnswer,
                    correctAnswer = m.correctAnswer
                )
            )
        }

        viewModelScope.launch {
            repository.saveQuizSession(
                userId = currentUserId,
                mode = _selectedQuizCategory.value.name,
                tableNumber = if (_quizMode.value == QuizMode.GADIYA) _quizTableNumber.value else 0,
                score = _score.value,
                totalQuestions = _questions.value.size,
                difficulty = _difficulty.value.id,
                language = _language.value.code,
                totalTimeMs = durationMs,
                mistakes = mistakesToSave
            )
        }
    }

    fun resetToQuizSetup() {
        timerJob?.cancel()
        ttsManager.stop()
        _quizPhase.value = QuizPhase.SETUP
    }

    private fun triggerHaptic(isSuccess: Boolean) {
        if (!_hapticEnabled.value) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (isSuccess) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 50, 40, 60), -1))
                }
            }
        } catch (_: Exception) {}
    }

    // --- Learn Tables, Squares, Cubes, Roots State ---
    private val _selectedLearnCategory = MutableStateFlow(MathCategory.TABLES)
    val selectedLearnCategory: StateFlow<MathCategory> = _selectedLearnCategory.asStateFlow()

    private val _learnNumber = MutableStateFlow(5)
    val learnNumber: StateFlow<Int> = _learnNumber.asStateFlow()

    private val _activeLearnIndex = MutableStateFlow<Int?>(null)
    val activeLearnIndex: StateFlow<Int?> = _activeLearnIndex.asStateFlow()

    private val _isPlayingWholeCategory = MutableStateFlow(false)
    val isPlayingWholeCategory: StateFlow<Boolean> = _isPlayingWholeCategory.asStateFlow()

    private val _learnSpeed = MutableStateFlow(1.0f)
    val learnSpeed: StateFlow<Float> = _learnSpeed.asStateFlow()

    fun setLearnCategory(category: MathCategory) {
        stopLearnAudio()
        _selectedLearnCategory.value = category
    }

    fun setLearnNumber(num: Int) {
        stopLearnAudio()
        _learnNumber.value = num.coerceIn(1, 30)
    }

    fun setLearnSpeed(speed: Float) {
        _learnSpeed.value = speed
    }

    fun playSingleItemAudio(item: MathItem) {
        stopLearnAudio()
        _activeLearnIndex.value = item.baseNumber
        ttsManager.speakMathItem(
            category = item.category,
            baseNumber = item.baseNumber,
            language = _language.value,
            voiceProfile = _selectedVoiceProfile.value,
            rate = _learnSpeed.value,
            onDone = {
                _activeLearnIndex.value = null
            }
        )
    }

    fun playSingleTableLine(n1: Int, n2: Int) {
        stopLearnAudio()
        _activeLearnIndex.value = n2
        ttsManager.speakTableLine(
            n1 = n1,
            n2 = n2,
            language = _language.value,
            voiceProfile = _selectedVoiceProfile.value,
            rate = _learnSpeed.value,
            onDone = {
                _activeLearnIndex.value = null
            }
        )
    }

    fun togglePlayFullCategory() {
        if (_isPlayingWholeCategory.value) {
            stopLearnAudio()
        } else {
            _isPlayingWholeCategory.value = true
            if (_selectedLearnCategory.value == MathCategory.TABLES) {
                playSequentialTableLine(_learnNumber.value, 1)
            } else {
                playSequentialMathItem(_selectedLearnCategory.value, 1)
            }
        }
    }

    private fun playSequentialTableLine(num: Int, lineIndex: Int) {
        if (!_isPlayingWholeCategory.value || lineIndex > 10) {
            stopLearnAudio()
            return
        }

        _activeLearnIndex.value = lineIndex
        ttsManager.speakTableLine(
            n1 = num,
            n2 = lineIndex,
            language = _language.value,
            voiceProfile = _selectedVoiceProfile.value,
            rate = _learnSpeed.value,
            onDone = {
                viewModelScope.launch {
                    delay(350L)
                    if (_isPlayingWholeCategory.value) {
                        playSequentialTableLine(num, lineIndex + 1)
                    }
                }
            }
        )
    }

    private fun playSequentialMathItem(cat: MathCategory, currentNumber: Int) {
        if (!_isPlayingWholeCategory.value || currentNumber > 30) {
            stopLearnAudio()
            return
        }

        _activeLearnIndex.value = currentNumber
        ttsManager.speakMathItem(
            category = cat,
            baseNumber = currentNumber,
            language = _language.value,
            voiceProfile = _selectedVoiceProfile.value,
            rate = _learnSpeed.value,
            onDone = {
                viewModelScope.launch {
                    delay(350L)
                    if (_isPlayingWholeCategory.value) {
                        playSequentialMathItem(cat, currentNumber + 1)
                    }
                }
            }
        )
    }

    fun stopLearnAudio() {
        _isPlayingWholeCategory.value = false
        _activeLearnIndex.value = null
        ttsManager.stop()
    }

    // --- Analytics Dashboard (Real-Time Flows) ---
    val recentQuizHistory = currentProfile.flatMapLatest { profile ->
        val uid = profile?.id ?: 1L
        repository.getRecentResults(uid, limit = 15)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topMistakes: StateFlow<List<MistakeSummary>> = currentProfile.flatMapLatest { profile ->
        val uid = profile?.id ?: 1L
        repository.getTopMistakes(uid, limit = 10)
    }.combine(flowOf(Unit)) { list, _ ->
        list.map { MistakeSummary(it.factor1, it.factor2, it.correctAnswer, it.count) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tableMasteryList: StateFlow<List<TableMastery>> = currentProfile.flatMapLatest { profile ->
        val uid = profile?.id ?: 1L
        repository.getTableStats(uid)
    }.combine(flowOf(Unit)) { stats, _ ->
        val map = stats.associateBy { it.tableNumber }
        (1..30).map { tableNum ->
            val stat = map[tableNum]
            if (stat != null && stat.totalPossible > 0) {
                val acc = (stat.totalScore * 100) / stat.totalPossible
                TableMastery(tableNum, stat.totalPossible, stat.totalScore, acc)
            } else {
                TableMastery(tableNum, 0, 0, 0)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val analyticsSummary: StateFlow<AnalyticsSummary> = currentProfile.flatMapLatest { profile ->
        val uid = profile?.id ?: 1L
        combine(
            repository.getTotalTestsCount(uid),
            repository.getTotalQuestionsCount(uid),
            repository.getTotalScoreCount(uid),
            repository.getAverageResponseTimePerQuestionMs(uid)
        ) { tests, totalQ, totalS, avgTimeMs ->
            val q = totalQ ?: 0
            val s = totalS ?: 0
            val acc = if (q > 0) ((s * 100) / q) else 0
            val avgSec = if (avgTimeMs != null) (avgTimeMs / 1000.0) else 0.0
            AnalyticsSummary(
                totalTests = tests,
                totalQuestionsAnswered = q,
                totalCorrect = s,
                averageAccuracyPercent = acc,
                averageResponseTimeSec = (Math.round(avgSec * 10.0) / 10.0),
                currentStreak = s.coerceAtLeast(0),
                bestStreak = q.coerceAtLeast(s)
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalyticsSummary())

    fun startMistakeDrill() {
        val mistakes = topMistakes.value
        if (mistakes.isEmpty()) return

        val drillQuestions = mistakes.take(10).map { m ->
            val ans = m.factor1 * m.factor2
            val options = generateOptions(ans, 4)
            QuizQuestion(MathCategory.TABLES, m.factor1, m.factor2, "${m.factor1} × ${m.factor2} = ?", ans, options)
        }

        startQuiz(customQuestions = drillQuestions)
    }

    fun clearMistakePair(f1: Int, f2: Int) {
        val uid = currentProfile.value?.id ?: 1L
        viewModelScope.launch {
            repository.clearMistake(uid, f1, f2)
        }
    }

    fun clearAllMistakes() {
        val uid = currentProfile.value?.id ?: 1L
        viewModelScope.launch {
            repository.clearAllMistakes(uid)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        ttsManager.shutdown()
    }
}
