package com.example.model

enum class MathCategory(
    val id: String,
    val displayName: String,
    val gujaratiName: String,
    val hindiName: String,
    val symbol: String
) {
    TABLES("tables", "Multiplication Tables", "ઘડિયા", "पहाड़े", "×"),
    SQUARES("squares", "Squares (n²)", "વર્ગ (n²)", "वर्ग (n²)", "n²"),
    CUBES("cubes", "Cubes (n³)", "ઘન (n³)", "घन (n³)", "n³"),
    SQUARE_ROOTS("square_roots", "Square Roots (√n)", "વર્ગમૂળ (√n)", "वर्गमूल (√n)", "√n"),
    CUBE_ROOTS("cube_roots", "Cube Roots (∛n)", "ઘનમૂળ (∛n)", "घनमूल (∛n)", "∛n"),
    MIXED("mixed", "Mixed Math Master", "મિશ્ર ટેસ્ટ", "मिश्रित टेस्ट", "∑");

    fun getTitle(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.GUJARATI -> gujaratiName
        SupportedLanguage.HINDI -> hindiName
        else -> displayName
    }
}

enum class QuizMode(val displayName: String, val gujaratiName: String) {
    PALAKHA("Random Practice", "પલાખા (રેન્ડમ)"),
    GADIYA("Specific Table", "ચોક્કસ ઘડિયો");

    fun getTitle(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.GUJARATI -> gujaratiName
        else -> displayName
    }
}

enum class PalakhaRange(val label: String, val gujaratiLabel: String, val range: IntRange) {
    RANGE_1_TO_10("1 to 10", "૧ થી ૧૦", 1..10),
    RANGE_11_TO_20("11 to 20", "૧૧ થી ૨૦", 11..20),
    RANGE_21_TO_30("21 to 30", "૨૧ થી ૩૦", 21..30),
    RANGE_RANDOM("Random (1 to 30)", "રેન્ડમ (૧ થી ૩૦)", 1..30);

    fun getTitle(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.GUJARATI -> gujaratiLabel
        else -> label
    }
}

enum class VoiceProfile(
    val id: String,
    val displayName: String,
    val gujaratiName: String,
    val iconEmoji: String,
    val pitch: Float,
    val speedOffset: Float,
    val isMale: Boolean
) {
    GENTLE_FEMALE("female_gentle", "Gentle Female", "પ્રેમાળ સ્ત્રી", "👩", 1.20f, 0.95f, false),
    DEEP_MALE("male_deep", "Deep Male", "ગંભીર પુરુષ", "👨", 0.82f, 0.95f, true),
    PLAYFUL_GIRL("girl_playful", "Young Girl", "બાળકી", "👧", 1.38f, 1.05f, false),
    ENERGETIC_BOY("boy_young", "Young Student", "વિદ્યાર્થી", "👦", 0.92f, 1.05f, true),
    TEACHER_FEMALE("teacher_female", "Teacher", "શિક્ષિકા", "👩‍🏫", 1.15f, 0.90f, false),
    PROFESSOR_MALE("professor_male", "Professor", "પ્રોફેસર", "👨‍🏫", 0.78f, 0.90f, true),
    SPEED_TRAINER("speed_trainer", "Speed Trainer", "સ્પીડ ટ્રેનર", "⚡", 1.25f, 1.20f, false),
    CALM_PRECISION("calm_precision", "Calm Precision", "શાંત સચોટ", "🎯", 1.00f, 1.00f, true);

    fun getTitle(lang: SupportedLanguage): String = when (lang) {
        SupportedLanguage.GUJARATI -> "$iconEmoji $gujaratiName"
        else -> "$iconEmoji $displayName"
    }
}

enum class DifficultyLevel(val id: String, val label: String, val timeMs: Long) {
    EASY("easy", "Easy (8s)", 8000L),
    MEDIUM("medium", "Med (5s)", 5000L),
    HARD("hard", "Hard (3s)", 3000L),
    LIGHTNING("lightning", "Fast (2s)", 2000L)
}

enum class SupportedLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en-US", "English", "English"),
    GUJARATI("gu-IN", "Gujarati", "ગુજરાતી"),
    HINDI("hi-IN", "Hindi", "हिन्दी"),
    MARATHI("mr-IN", "Marathi", "मराठी"),
    TAMIL("ta-IN", "Tamil", "தமிழ்"),
    TELUGU("te-IN", "Telugu", "తెలుగు"),
    BENGALI("bn-IN", "Bengali", "বাংলা")
}

data class QuizQuestion(
    val category: MathCategory = MathCategory.TABLES,
    val factor1: Int,
    val factor2: Int = 1,
    val questionPrompt: String = "",
    val correctAnswer: Int,
    val options: List<Int>
)

data class MathItem(
    val category: MathCategory,
    val baseNumber: Int,
    val formulaDisplay: String,
    val resultDisplay: String,
    val fullExpression: String
)

data class TableMastery(
    val tableNumber: Int,
    val totalAttempts: Int,
    val correctAttempts: Int,
    val accuracyPercent: Int
)

data class MistakeSummary(
    val factor1: Int,
    val factor2: Int,
    val correctAnswer: Int,
    val timesWrong: Int
)

data class AnalyticsSummary(
    val totalTests: Int = 0,
    val totalQuestionsAnswered: Int = 0,
    val totalCorrect: Int = 0,
    val averageAccuracyPercent: Int = 0,
    val averageResponseTimeSec: Double = 0.0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0
)

data class GoogleAccountUser(
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val isLinked: Boolean = false,
    val lastBackupTimestamp: Long = 0L
)
