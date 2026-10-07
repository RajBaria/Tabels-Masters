package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MathCategory
import com.example.model.SupportedLanguage
import com.example.ui.components.GlassCard
import com.example.ui.viewmodel.QuizViewModel

@Composable
fun HomeScreen(
    viewModel: QuizViewModel,
    onNavigateToQuiz: (MathCategory) -> Unit,
    onNavigateToLearn: (MathCategory) -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToProfiles: () -> Unit,
    onOpenTutorial: () -> Unit,
    modifier: Modifier = Modifier
) {
    val googleAccount by viewModel.googleAccount.collectAsState()
    val language by viewModel.language.collectAsState()

    val isGujarati = language == SupportedLanguage.GUJARATI

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Welcome Hero Banner
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isGujarati) "✨ મેથ્સ માસ્ટરમાં સ્વાગત છે" else "✨ Welcome to Maths Master",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isGujarati) "સંખ્યાઓ શીખો, આત્મવિશ્વાસ વધારો!" else "Master Numbers, Multiply Confidence!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = if (isGujarati) "ગણિત બને સરળ, સચોટ અને મનોરંજક • ૧ થી ૩૦ ઘડિયા, વર્ગ, ઘન અને મૂળ" else "Math made simple, accurate, and fun • Tables, Squares, Cubes & Roots (1 to 30)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    // Daily Math Formula
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isGujarati) "💡 દૈનિક ગણિત ફોર્મ્યુલા" else "💡 Daily Math Formula",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "15² = 225  •  12³ = 1728  •  √625 = 25  •  ∛1000 = 10",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Launch Grid (Math Domains)
        item {
            Text(
                text = if (isGujarati) "📚 ગણિત વિષયો પસંદ કરો" else "📚 Explore Math Categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // 2x2 Grid of Main Categories
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HomeCategoryCard(
                    title = if (isGujarati) "ઘડિયા" else "Tables",
                    subtitle = if (isGujarati) "૧ થી ૩૦ ઘડિયા ઓડિયો સાથે" else "1 to 30 Tables with Audio",
                    badge = "1-30 ×",
                    color = Color(0xFF4F46E5),
                    modifier = Modifier.weight(1f),
                    testTag = "home_cat_tables",
                    onClick = { onNavigateToLearn(MathCategory.TABLES) }
                )
                HomeCategoryCard(
                    title = if (isGujarati) "વર્ગ" else "Squares",
                    subtitle = if (isGujarati) "૧² થી ૩૦² ઓડિયો સાથે" else "1² to 30² with Audio",
                    badge = "n²",
                    color = Color(0xFF059669),
                    modifier = Modifier.weight(1f),
                    testTag = "home_cat_squares",
                    onClick = { onNavigateToLearn(MathCategory.SQUARES) }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HomeCategoryCard(
                    title = if (isGujarati) "ઘન" else "Cubes",
                    subtitle = if (isGujarati) "૧³ થી ૩૦³ ઓડિયો સાથે" else "1³ to 30³ with Audio",
                    badge = "n³",
                    color = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f),
                    testTag = "home_cat_cubes",
                    onClick = { onNavigateToLearn(MathCategory.CUBES) }
                )
                HomeCategoryCard(
                    title = if (isGujarati) "વર્ગમૂળ" else "Square Roots",
                    subtitle = if (isGujarati) "√૧ થી √૯૦૦ ઓડિયો સાથે" else "√1 to √900 with Audio",
                    badge = "√n",
                    color = Color(0xFF3B82F6),
                    modifier = Modifier.weight(1f),
                    testTag = "home_cat_roots",
                    onClick = { onNavigateToLearn(MathCategory.SQUARE_ROOTS) }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HomeCategoryCard(
                    title = if (isGujarati) "ઘનમૂળ" else "Cube Roots",
                    subtitle = if (isGujarati) "∛૧ થી ∛૨૭૦૦૦ ઓડિયો સાથે" else "∛1 to ∛27000 with Audio",
                    badge = "∛n",
                    color = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f),
                    testTag = "home_cat_cube_roots",
                    onClick = { onNavigateToLearn(MathCategory.CUBE_ROOTS) }
                )
                HomeCategoryCard(
                    title = if (isGujarati) "મિશ્ર ટેસ્ટ" else "Mixed Master",
                    subtitle = if (isGujarati) "બધા વિષયો મિક્સ ક્વિઝ" else "All Categories Mixed Quiz",
                    badge = "∑ ALL",
                    color = Color(0xFFEC4899),
                    modifier = Modifier.weight(1f),
                    testTag = "home_cat_mixed",
                    onClick = { onNavigateToQuiz(MathCategory.MIXED) }
                )
            }
        }

        // Quick Audio Quiz Arena Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isGujarati) "⚡ ઓડિયો ક્વિઝ એરેના" else "⚡ Audio Quiz Arena",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isGujarati) "ટાઈમર પ્રશ્નો, ૮ અવાજ અને ઝડપી પરિણામ!" else "Timed questions, 8 voice styles, and instant feedback!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { onNavigateToQuiz(MathCategory.TABLES) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("home_launch_quiz_button")
                        ) {
                            Icon(Icons.Default.PlayCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isGujarati) "ક્વિઝ શરૂ કરો" else "Start Quiz Now", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Google Drive & Cloud Status Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = CircleShape,
                            color = if (googleAccount.isLinked) Color(0xFF34A853).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    if (googleAccount.isLinked) Icons.Default.CloudDone else Icons.Default.CloudOff,
                                    contentDescription = null,
                                    tint = if (googleAccount.isLinked) Color(0xFF34A853) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isGujarati) "ગૂગલ ડ્રાઇવ બેકઅપ" else "Google Drive Sync",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = if (googleAccount.isLinked) googleAccount.email else (if (isGujarati) "ગૂગલ સાઇન ઇન કરી બેકઅપ લો" else "Sign in to backup your reports"),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToProfiles,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("home_drive_backup_button")
                    ) {
                        Text(if (googleAccount.isLinked) (if (isGujarati) "બેકઅપ" else "Backup") else (if (isGujarati) "સાઇન ઇન" else "Sign In"), fontSize = 13.sp)
                    }
                }
            }
        }

        // Help / Tutorial Tour Button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenTutorial() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.HelpOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isGujarati) "એપ યુઝ ગાઇડ (કેવી રીતે વાપરવી)" else "App Guide & Uses Tour",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun HomeCategoryCard(
    title: String,
    subtitle: String,
    badge: String,
    color: Color,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .height(118.dp)
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = color.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = badge,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = color
                    )
                }
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
    }
}
