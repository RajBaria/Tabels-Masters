package com.example.audio

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.model.MathCategory
import com.example.model.SupportedLanguage
import com.example.model.VoiceProfile
import java.util.Locale
import java.util.UUID

class TTSManager(context: Context, onReady: () -> Unit = {}) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val gujaratiMultipliers = mapOf(
        1 to "એકા",
        2 to "દુ",
        3 to "તરી",
        4 to "ચોક",
        5 to "પંચા",
        6 to "છંગ",
        7 to "સત્તા",
        8 to "અઠ્ઠા",
        9 to "નવા",
        10 to "દાન"
    )

    private val hindiMultipliers = mapOf(
        1 to "एकम",
        2 to "दूनी",
        3 to "तिया",
        4 to "चौके",
        5 to "पंचे",
        6 to "छक्के",
        7 to "सत्ते",
        8 to "अट्ठे",
        9 to "नम्मे",
        10 to "दहाई"
    )

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                tts?.language = Locale.US
                onReady()
            } else {
                Log.e("TTSManager", "TTS Initialization failed: $status")
            }
        }
    }

    private fun configureLocaleAndVoice(language: SupportedLanguage, voiceProfile: VoiceProfile, customSpeed: Float) {
        if (!isInitialized || tts == null) return

        val targetLocale = when (language) {
            SupportedLanguage.ENGLISH -> Locale.US
            SupportedLanguage.GUJARATI -> Locale("gu", "IN")
            SupportedLanguage.HINDI -> Locale("hi", "IN")
            SupportedLanguage.MARATHI -> Locale("mr", "IN")
            SupportedLanguage.TAMIL -> Locale("ta", "IN")
            SupportedLanguage.TELUGU -> Locale("te", "IN")
            SupportedLanguage.BENGALI -> Locale("bn", "IN")
        }

        val result = tts?.setLanguage(targetLocale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            val fallback = if (language == SupportedLanguage.GUJARATI || language == SupportedLanguage.MARATHI) {
                Locale("hi", "IN")
            } else {
                Locale.US
            }
            tts?.setLanguage(fallback)
        }

        tts?.setPitch(voiceProfile.pitch)
        val finalRate = (customSpeed * voiceProfile.speedOffset).coerceIn(0.5f, 2.5f)
        tts?.setSpeechRate(finalRate)

        // Try to match system voice by gender
        try {
            val currentVoices = tts?.voices
            if (!currentVoices.isNullOrEmpty()) {
                val targetKeyword = if (voiceProfile.isMale) "male" else "female"
                val match = currentVoices.firstOrNull { voice ->
                    val nameLower = voice.name.lowercase(Locale.ROOT)
                    nameLower.contains(targetKeyword) && (!voiceProfile.isMale || !nameLower.contains("female"))
                }
                if (match != null) {
                    tts?.voice = match
                }
            }
        } catch (_: Exception) {}
    }

    fun speakQuestion(
        category: MathCategory,
        n1: Int,
        n2: Int,
        language: SupportedLanguage,
        voiceProfile: VoiceProfile,
        rate: Float,
        onDone: () -> Unit
    ) {
        stop()
        configureLocaleAndVoice(language, voiceProfile, rate)

        val text = when (category) {
            MathCategory.TABLES -> when (language) {
                SupportedLanguage.GUJARATI -> {
                    val mult = gujaratiMultipliers[n2]
                    if (mult != null) "$n1 $mult" else "$n1 ગુણ્યા $n2 કેટલા?"
                }
                SupportedLanguage.HINDI -> {
                    val mult = hindiMultipliers[n2]
                    if (mult != null) "$n1 $mult" else "$n1 गुणा $n2 कितना?"
                }
                SupportedLanguage.MARATHI -> "$n1 गुणिले $n2 किती?"
                SupportedLanguage.TAMIL -> "$n1 பெருக்கல் $n2 என்ன?"
                SupportedLanguage.TELUGU -> "$n1 గుణకారము $n2 ఎంత?"
                SupportedLanguage.BENGALI -> "$n1 গুণ $n2 কত?"
                SupportedLanguage.ENGLISH -> "What is $n1 times $n2?"
            }
            MathCategory.SQUARES -> when (language) {
                SupportedLanguage.GUJARATI -> "$n1 નો વર્ગ કેટલો થાય?"
                SupportedLanguage.HINDI -> "$n1 का वर्ग क्या है?"
                SupportedLanguage.MARATHI -> "$n1 चा वर्ग किती?"
                SupportedLanguage.TAMIL -> "$n1 இன் வர்க்கம் என்ன?"
                SupportedLanguage.TELUGU -> "$n1 యొక్క వర్గం ఎంత?"
                SupportedLanguage.BENGALI -> "$n1 এর বর্গ কত?"
                SupportedLanguage.ENGLISH -> "What is the square of $n1?"
            }
            MathCategory.CUBES -> when (language) {
                SupportedLanguage.GUJARATI -> "$n1 નો ઘન કેટલો થાય?"
                SupportedLanguage.HINDI -> "$n1 का घन क्या है?"
                SupportedLanguage.MARATHI -> "$n1 चा घन किती?"
                SupportedLanguage.TAMIL -> "$n1 இன் கனசதுரம் என்ன?"
                SupportedLanguage.TELUGU -> "$n1 యొక్క ఘనం ఎంత?"
                SupportedLanguage.BENGALI -> "$n1 এর ঘন কত?"
                SupportedLanguage.ENGLISH -> "What is the cube of $n1?"
            }
            MathCategory.SQUARE_ROOTS -> {
                val sq = n1 * n1
                when (language) {
                    SupportedLanguage.GUJARATI -> "$sq નું વર્ગમૂળ કેટલું થાય?"
                    SupportedLanguage.HINDI -> "$sq का वर्गमूल क्या है?"
                    SupportedLanguage.MARATHI -> "$sq चे वर्गमूळ किती?"
                    SupportedLanguage.TAMIL -> "$sq இன் வர்க்கமூலம் என்ன?"
                    SupportedLanguage.TELUGU -> "$sq యొక్క వర్గమూలం ఎంత?"
                    SupportedLanguage.BENGALI -> "$sq এর বর্গমূল কত?"
                    SupportedLanguage.ENGLISH -> "What is the square root of $sq?"
                }
            }
            MathCategory.CUBE_ROOTS -> {
                val cb = n1 * n1 * n1
                when (language) {
                    SupportedLanguage.GUJARATI -> "$cb નું ઘનમૂળ કેટલું થાય?"
                    SupportedLanguage.HINDI -> "$cb का घनमूल क्या है?"
                    SupportedLanguage.MARATHI -> "$cb चे घनमूळ किती?"
                    SupportedLanguage.TAMIL -> "$cb இன் கனமூலம் என்ன?"
                    SupportedLanguage.TELUGU -> "$cb యొక్క ఘనమూలం ఎంత?"
                    SupportedLanguage.BENGALI -> "$cb এর ঘনমূল কত?"
                    SupportedLanguage.ENGLISH -> "What is the cube root of $cb?"
                }
            }
            MathCategory.MIXED -> "Solve: $n1 times $n2"
        }

        speakUtterance(text, onDone)
    }

    fun speakAnswer(
        answer: Int,
        isCorrect: Boolean,
        language: SupportedLanguage,
        voiceProfile: VoiceProfile,
        rate: Float,
        onDone: () -> Unit
    ) {
        stop()
        configureLocaleAndVoice(language, voiceProfile, rate)

        val prefix = when (language) {
            SupportedLanguage.GUJARATI -> if (isCorrect) "સાચો જવાબ! " else "જવાબ છે "
            SupportedLanguage.HINDI -> if (isCorrect) "सही जवाब! " else "जवाब है "
            SupportedLanguage.MARATHI -> if (isCorrect) "बरोबर! " else "उत्तर आहे "
            SupportedLanguage.TAMIL -> if (isCorrect) "சரி! " else "பதில் "
            SupportedLanguage.TELUGU -> if (isCorrect) "సరైన సమాధానం! " else "సమాధానం "
            SupportedLanguage.BENGALI -> if (isCorrect) "সঠিক উত্তর! " else "উত্তর হল "
            SupportedLanguage.ENGLISH -> if (isCorrect) "Correct! " else "Answer is "
        }

        speakUtterance("$prefix $answer", onDone)
    }

    fun speakMathItem(
        category: MathCategory,
        baseNumber: Int,
        language: SupportedLanguage,
        voiceProfile: VoiceProfile,
        rate: Float,
        onDone: () -> Unit
    ) {
        stop()
        configureLocaleAndVoice(language, voiceProfile, rate)

        val text = when (category) {
            MathCategory.TABLES -> "$baseNumber"
            MathCategory.SQUARES -> {
                val sq = baseNumber * baseNumber
                when (language) {
                    SupportedLanguage.GUJARATI -> "$baseNumber નો વર્ગ $sq"
                    SupportedLanguage.HINDI -> "$baseNumber का वर्ग $sq"
                    SupportedLanguage.MARATHI -> "$baseNumber चा वर्ग $sq"
                    SupportedLanguage.TAMIL -> "$baseNumber இன் வர்க்கம் $sq"
                    SupportedLanguage.TELUGU -> "$baseNumber యొక్క వర్గం $sq"
                    SupportedLanguage.BENGALI -> "$baseNumber এর বর্গ $sq"
                    SupportedLanguage.ENGLISH -> "Square of $baseNumber is $sq"
                }
            }
            MathCategory.CUBES -> {
                val cb = baseNumber * baseNumber * baseNumber
                when (language) {
                    SupportedLanguage.GUJARATI -> "$baseNumber નો ઘન $cb"
                    SupportedLanguage.HINDI -> "$baseNumber का घन $cb"
                    SupportedLanguage.MARATHI -> "$baseNumber चा घन $cb"
                    SupportedLanguage.TAMIL -> "$baseNumber இன் கனசதுரம் $cb"
                    SupportedLanguage.TELUGU -> "$baseNumber యొక్క ఘనం $cb"
                    SupportedLanguage.BENGALI -> "$baseNumber এর ঘন $cb"
                    SupportedLanguage.ENGLISH -> "Cube of $baseNumber is $cb"
                }
            }
            MathCategory.SQUARE_ROOTS -> {
                val sq = baseNumber * baseNumber
                when (language) {
                    SupportedLanguage.GUJARATI -> "$sq નું વર્ગમૂળ $baseNumber"
                    SupportedLanguage.HINDI -> "$sq का वर्गमूल $baseNumber"
                    SupportedLanguage.MARATHI -> "$sq चे वर्गमूळ $baseNumber"
                    SupportedLanguage.TAMIL -> "$sq இன் வர்க்கமூலம் $baseNumber"
                    SupportedLanguage.TELUGU -> "$sq యొక్క వర్గమూలం $baseNumber"
                    SupportedLanguage.BENGALI -> "$sq এর বর্গমূল $baseNumber"
                    SupportedLanguage.ENGLISH -> "Square root of $sq is $baseNumber"
                }
            }
            MathCategory.CUBE_ROOTS -> {
                val cb = baseNumber * baseNumber * baseNumber
                when (language) {
                    SupportedLanguage.GUJARATI -> "$cb નું ઘનમૂળ $baseNumber"
                    SupportedLanguage.HINDI -> "$cb का घनमूल $baseNumber"
                    SupportedLanguage.MARATHI -> "$cb चे घनमूळ $baseNumber"
                    SupportedLanguage.TAMIL -> "$cb இன் கனமூலம் $baseNumber"
                    SupportedLanguage.TELUGU -> "$cb యొక్క ఘనమూలం $baseNumber"
                    SupportedLanguage.BENGALI -> "$cb এর ঘনमूल $baseNumber"
                    SupportedLanguage.ENGLISH -> "Cube root of $cb is $baseNumber"
                }
            }
            MathCategory.MIXED -> "$baseNumber"
        }

        speakUtterance(text, onDone)
    }

    fun speakTableLine(
        n1: Int,
        n2: Int,
        language: SupportedLanguage,
        voiceProfile: VoiceProfile,
        rate: Float,
        onDone: () -> Unit
    ) {
        stop()
        configureLocaleAndVoice(language, voiceProfile, rate)

        val product = n1 * n2
        val text = when (language) {
            SupportedLanguage.GUJARATI -> {
                val mult = gujaratiMultipliers[n2]
                if (mult != null) "$n1 $mult $product" else "$n1 ગુણ્યા $n2 બરાબર $product"
            }
            SupportedLanguage.HINDI -> {
                val mult = hindiMultipliers[n2]
                if (mult != null) "$n1 $mult $product" else "$n1 गुणा $n2 बराबर $product"
            }
            SupportedLanguage.MARATHI -> "$n1 गुणिले $n2 बरोबर $product"
            SupportedLanguage.TAMIL -> "$n1 பெருக்கல் $n2 சமம் $product"
            SupportedLanguage.TELUGU -> "$n1 గుణకారము $n2 సమానం $product"
            SupportedLanguage.BENGALI -> "$n1 গুণ $n2 সমান $product"
            SupportedLanguage.ENGLISH -> "$n1 times $n2 is $product"
        }

        speakUtterance(text, onDone)
    }

    private fun speakUtterance(text: String, onDone: () -> Unit) {
        if (!isInitialized || tts == null) {
            onDone()
            return
        }

        val utteranceId = UUID.randomUUID().toString()

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(id: String?) {
                if (id == utteranceId) {
                    onDone()
                }
            }
            override fun onError(id: String?) {
                if (id == utteranceId) {
                    onDone()
                }
            }
        })

        val params = Bundle()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
    }
}
