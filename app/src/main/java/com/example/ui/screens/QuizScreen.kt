package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DifficultyLevel
import com.example.model.MathCategory
import com.example.model.PalakhaRange
import com.example.model.QuizMode
import com.example.model.SupportedLanguage
import com.example.model.VoiceProfile
import com.example.ui.components.GlassCard
import com.example.ui.components.TimerBar
import com.example.ui.viewmodel.QuizPhase
import com.example.ui.viewmodel.QuizViewModel
import kotlin.math.roundToInt

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val phase by viewModel.quizPhase.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        when (phase) {
            QuizPhase.SETUP -> QuizSetupView(viewModel)
            QuizPhase.RUNNING -> QuizRunningView(viewModel)
            QuizPhase.REPORT -> QuizReportView(viewModel)
        }
    }
}

@Composable
private fun QuizSetupView(viewModel: QuizViewModel) {
    val quizCategory by viewModel.selectedQuizCategory.collectAsState()
    val currentMode by viewModel.quizMode.collectAsState()
    val palakhaRange by viewModel.palakhaRange.collectAsState()
    val tableNum by viewModel.quizTableNumber.collectAsState()
    val language by viewModel.language.collectAsState()
    val voiceProfile by viewModel.selectedVoiceProfile.collectAsState()
    val difficulty by viewModel.difficulty.collectAsState()
    val quizSpeed by viewModel.quizSpeed.collectAsState()

    val isGujarati = language == SupportedLanguage.GUJARATI

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isGujarati) "🎧 ઓડિયો ટેસ્ટ એરેના" else "🎧 Audio Test Arena",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (isGujarati) "પ્રશ્ન સાંભળો અને ઝડપથી ઉત્તર આપો" else "Listen to questions and practice rapid response",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Test Categories Grid (ALL 6 options clearly visible on screen without hidden slider)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isGujarati) "ટેસ્ટ વિષય પસંદ કરો (Category):" else "Select Test Category:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Row 1: Tables & Squares
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CategorySelectionButton(
                            category = MathCategory.TABLES,
                            selected = quizCategory == MathCategory.TABLES,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setQuizCategory(MathCategory.TABLES) }
                        )
                        CategorySelectionButton(
                            category = MathCategory.SQUARES,
                            selected = quizCategory == MathCategory.SQUARES,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setQuizCategory(MathCategory.SQUARES) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Row 2: Cubes & Square Roots
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CategorySelectionButton(
                            category = MathCategory.CUBES,
                            selected = quizCategory == MathCategory.CUBES,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setQuizCategory(MathCategory.CUBES) }
                        )
                        CategorySelectionButton(
                            category = MathCategory.SQUARE_ROOTS,
                            selected = quizCategory == MathCategory.SQUARE_ROOTS,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setQuizCategory(MathCategory.SQUARE_ROOTS) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Row 3: Cube Roots & Mixed Master
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CategorySelectionButton(
                            category = MathCategory.CUBE_ROOTS,
                            selected = quizCategory == MathCategory.CUBE_ROOTS,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setQuizCategory(MathCategory.CUBE_ROOTS) }
                        )
                        CategorySelectionButton(
                            category = MathCategory.MIXED,
                            selected = quizCategory == MathCategory.MIXED,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setQuizCategory(MathCategory.MIXED) }
                        )
                    }
                }
            }
        }

        // If Tables is selected: show Mode & Palakha range
        if (quizCategory == MathCategory.TABLES) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isGujarati) "પદ્ધતિ (Mode)" else "Quiz Mode",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            FilterChip(
                                selected = currentMode == QuizMode.PALAKHA,
                                onClick = { viewModel.setQuizMode(QuizMode.PALAKHA) },
                                label = { Text(if (isGujarati) "પલાખા (રેન્ડમ)" else "Random Practice") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("chip_mode_palakha")
                            )
                            FilterChip(
                                selected = currentMode == QuizMode.GADIYA,
                                onClick = { viewModel.setQuizMode(QuizMode.GADIYA) },
                                label = { Text(if (isGujarati) "ચોક્કસ ઘડિયો" else "Specific Table") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("chip_mode_gadiya")
                            )
                        }
                    }
                }
            }

            // Palakha Range Options
            if (currentMode == QuizMode.PALAKHA) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (isGujarati) "પલાખા રેન્જ:" else "Question Range:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilterChip(
                                        selected = palakhaRange == PalakhaRange.RANGE_1_TO_10,
                                        onClick = { viewModel.setPalakhaRange(PalakhaRange.RANGE_1_TO_10) },
                                        label = { Text(palakhaRangeButtonText(PalakhaRange.RANGE_1_TO_10, isGujarati)) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("palakha_range_1_10")
                                    )
                                    FilterChip(
                                        selected = palakhaRange == PalakhaRange.RANGE_11_TO_20,
                                        onClick = { viewModel.setPalakhaRange(PalakhaRange.RANGE_11_TO_20) },
                                        label = { Text(palakhaRangeButtonText(PalakhaRange.RANGE_11_TO_20, isGujarati)) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("palakha_range_11_20")
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilterChip(
                                        selected = palakhaRange == PalakhaRange.RANGE_21_TO_30,
                                        onClick = { viewModel.setPalakhaRange(PalakhaRange.RANGE_21_TO_30) },
                                        label = { Text(palakhaRangeButtonText(PalakhaRange.RANGE_21_TO_30, isGujarati)) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("palakha_range_21_30")
                                    )
                                    FilterChip(
                                        selected = palakhaRange == PalakhaRange.RANGE_RANDOM,
                                        onClick = { viewModel.setPalakhaRange(PalakhaRange.RANGE_RANDOM) },
                                        label = { Text(palakhaRangeButtonText(PalakhaRange.RANGE_RANDOM, isGujarati)) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("palakha_range_random")
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Table Selection Slider (1 to 30) (If Gadiya)
            if (currentMode == QuizMode.GADIYA) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isGujarati) "ઘડિયો પસંદ કરો" else "Select Table",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = if (isGujarati) "$tableNum નો ઘડિયો" else "Table of $tableNum",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Slider(
                                value = tableNum.toFloat(),
                                onValueChange = { viewModel.setQuizTableNumber(it.roundToInt()) },
                                valueRange = 1f..30f,
                                steps = 28,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quiz_table_slider")
                            )

                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items((1..30).toList()) { num ->
                                    val isSelected = num == tableNum
                                    Surface(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .clickable { viewModel.setQuizTableNumber(num) },
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        shape = CircleShape
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "$num",
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Voice Style Selection (8 distinct voices)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isGujarati) "અવાજ શૈલી:" else "Voice Style:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = voiceProfile.getTitle(language),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(VoiceProfile.values()) { profile ->
                            FilterChip(
                                selected = voiceProfile == profile,
                                onClick = { viewModel.setVoiceProfile(profile) },
                                label = { Text(profile.getTitle(language)) },
                                modifier = Modifier.testTag("voice_chip_${profile.id}")
                            )
                        }
                    }
                }
            }
        }

        // Difficulty / Wait Time
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isGujarati) "ટેસ્ટ મુશ્કેલી (ટાઈમર)" else "Difficulty (Timer)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DifficultyLevel.values().forEach { diff ->
                            FilterChip(
                                selected = difficulty == diff,
                                onClick = { viewModel.setDifficulty(diff) },
                                label = { Text(diff.label, fontSize = 12.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("diff_chip_${diff.id}")
                            )
                        }
                    }
                }
            }
        }

        // Voice Speed with Slider
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isGujarati) "ઝડપ સ્લાઇડર" else "Speech Speed Slider",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${(quizSpeed * 10).roundToInt() / 10.0}x",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Slider(
                        value = quizSpeed,
                        onValueChange = { viewModel.setQuizSpeed((it * 10).roundToInt() / 10f) },
                        valueRange = 0.5f..2.5f,
                        steps = 7,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("speed_slider")
                    )
                }
            }
        }

        // Start Button
        item {
            Button(
                onClick = { viewModel.startQuiz() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("start_quiz_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isGujarati) "ટેસ્ટ શરૂ કરો" else "Start ${quizCategory.displayName} Test",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CategorySelectionButton(
    category: MathCategory,
    selected: Boolean,
    language: SupportedLanguage,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(54.dp)
            .clickable { onClick() }
            .testTag("quiz_cat_${category.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 4.dp else 1.dp),
        border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = category.symbol,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.getTitle(language),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                maxLines = 1
            )
        }
    }
}

private fun palakhaRangeButtonText(range: PalakhaRange, isGujarati: Boolean): String {
    return if (isGujarati) range.gujaratiLabel else range.label
}

@Composable
private fun QuizRunningView(viewModel: QuizViewModel) {
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val score by viewModel.score.collectAsState()
    val timerProgress by viewModel.timerProgress.collectAsState()
    val feedback by viewModel.answerFeedback.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()

    val currentQ = questions.getOrNull(currentIndex)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "speaker_pulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "Question ${currentIndex + 1} / ${questions.size}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = "Score: $score",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.VolumeUp,
                    contentDescription = "Audio Playing",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (currentQ != null) {
                Text(
                    text = currentQ.questionPrompt,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = when (feedback) {
                        true -> Color(0xFF10B981)
                        false -> Color(0xFFEF4444)
                        null -> MaterialTheme.colorScheme.onSurface
                    }
                )
            } else {
                Text(
                    text = "Listening...",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            TimerBar(
                progress = timerProgress,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        if (currentQ != null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OptionCard(
                        option = currentQ.options.getOrNull(0) ?: 0,
                        correctAnswer = currentQ.correctAnswer,
                        selected = selectedAnswer == currentQ.options.getOrNull(0),
                        feedback = feedback,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.submitAnswer(currentQ.options[0]) }
                    )
                    OptionCard(
                        option = currentQ.options.getOrNull(1) ?: 0,
                        correctAnswer = currentQ.correctAnswer,
                        selected = selectedAnswer == currentQ.options.getOrNull(1),
                        feedback = feedback,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.submitAnswer(currentQ.options[1]) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OptionCard(
                        option = currentQ.options.getOrNull(2) ?: 0,
                        correctAnswer = currentQ.correctAnswer,
                        selected = selectedAnswer == currentQ.options.getOrNull(2),
                        feedback = feedback,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.submitAnswer(currentQ.options[2]) }
                    )
                    OptionCard(
                        option = currentQ.options.getOrNull(3) ?: 0,
                        correctAnswer = currentQ.correctAnswer,
                        selected = selectedAnswer == currentQ.options.getOrNull(3),
                        feedback = feedback,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.submitAnswer(currentQ.options[3]) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = { viewModel.stopQuiz() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("stop_quiz_button")
            ) {
                Icon(Icons.Default.Stop, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Stop")
            }

            Button(
                onClick = { viewModel.skipQuestion() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("skip_question_button")
            ) {
                Icon(Icons.Default.FastForward, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun OptionCard(
    option: Int,
    correctAnswer: Int,
    selected: Boolean,
    feedback: Boolean?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            feedback != null && option == correctAnswer -> Color(0xFF10B981)
            feedback != null && selected && !feedback -> Color(0xFFEF4444)
            selected -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.surface
        },
        label = "card_color"
    )

    val textColor = when {
        feedback != null && option == correctAnswer -> Color.White
        feedback != null && selected && !feedback -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    ElevatedCard(
        modifier = modifier
            .height(72.dp)
            .testTag("option_button_$option"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = backgroundColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$option",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
private fun QuizReportView(viewModel: QuizViewModel) {
    val score by viewModel.score.collectAsState()
    val total = 10
    val mistakes by viewModel.sessionMistakes.collectAsState()
    val percent = (score * 100) / total

    val motivationalText = when {
        percent >= 90 -> "🌟 Outstanding! Math Master!"
        percent >= 70 -> "👏 Great Job! Keep it up!"
        percent >= 50 -> "👍 Good effort! Practice makes perfect!"
        else -> "💪 Needs more practice. You can do it!"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Test Completed! 🎉",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "$score / $total",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "$percent% Accuracy",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = motivationalText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (mistakes.isEmpty()) "Mistakes: None! Perfect score! 🎯" else "Mistakes Review (${mistakes.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (mistakes.isEmpty()) Color(0xFF10B981) else MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (mistakes.isNotEmpty()) {
                        mistakes.forEach { m ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Question item: ${m.factor1} × ${m.factor2}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${m.correctAnswer} (You: ${m.wrongAnswer ?: "Skipped"})",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.startQuiz() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("play_again_button"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Play Again", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                if (mistakes.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { viewModel.startMistakeDrill() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("drill_mistakes_button"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Practice These Mistakes 🎯", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.resetToQuizSetup() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("back_to_menu_button"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Back to Menu", fontSize = 16.sp)
                }
            }
        }
    }
}
