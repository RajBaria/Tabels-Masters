package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard

data class OnboardingStep(
    val emoji: String,
    val title: String,
    val gujaratiTitle: String,
    val description: String,
    val highlightColor: Color
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val steps = remember {
        listOf(
            OnboardingStep(
                emoji = "📐",
                title = "Welcome to Maths Master",
                gujaratiTitle = "મેથ્સ માસ્ટરમાં સ્વાગત છે!",
                description = "Master Multiplication Tables (1-30), Squares (વર્ગ), Cubes (ઘન), and Roots (વર્ગમૂળ & ઘનમૂળ) with ease.",
                highlightColor = Color(0xFF4F46E5)
            ),
            OnboardingStep(
                emoji = "🎧",
                title = "Audio Recitation & 8 Voices",
                gujaratiTitle = "૮ અલગ અલગ અવાજ & ગૂગલ TTS",
                description = "Listen to line-by-line recitation in English, Gujarati, Hindi, and 5 more languages with Male & Female voice profiles.",
                highlightColor = Color(0xFF059669)
            ),
            OnboardingStep(
                emoji = "⚡",
                title = "Audio Quizzes & Palakha",
                gujaratiTitle = "પલાખા & સ્પીડ ટેસ્ટ",
                description = "Challenge yourself with timed audio tests (1 to 10, 11 to 20, 21 to 30, or Random) and interactive sliders.",
                highlightColor = Color(0xFFF59E0B)
            ),
            OnboardingStep(
                emoji = "☁️",
                title = "Analytics & Google Drive Sync",
                gujaratiTitle = "એનાલિટિક્સ & ગૂગલ ડ્રાઇવ બેકઅપ",
                description = "Monitor your accuracy, practice weak spots, and backup your reports safely with your Google Account.",
                highlightColor = Color(0xFF3B82F6)
            )
        )
    }

    var currentStep by remember { mutableIntStateOf(0) }
    val step = steps[currentStep]

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar with Skip Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onFinish,
                    modifier = Modifier.testTag("onboarding_skip_button")
                ) {
                    Text("Skip Tour (છોડો)", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Center Content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier.size(110.dp),
                    shape = CircleShape,
                    color = step.highlightColor.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = step.emoji, fontSize = 54.sp)
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = step.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = step.gujaratiTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = step.highlightColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = step.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Bottom Navigation & Dots
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Step Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    steps.indices.forEach { index ->
                        val isSelected = index == currentStep
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(if (isSelected) 24.dp else 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (isSelected) step.highlightColor else MaterialTheme.colorScheme.surfaceVariant
                                )
                        )
                    }
                }

                // Action Button
                Button(
                    onClick = {
                        if (currentStep < steps.size - 1) {
                            currentStep++
                        } else {
                            onFinish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("onboarding_next_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = step.highlightColor)
                ) {
                    if (currentStep < steps.size - 1) {
                        Text("Next (આગળ)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    } else {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Get Started (શરૂ કરો)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
