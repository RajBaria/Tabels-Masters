package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.CardDefaults.elevatedCardColors
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MathCategory
import com.example.model.MathItem
import com.example.model.SupportedLanguage
import com.example.model.VoiceProfile
import com.example.ui.components.GlassCard
import com.example.ui.viewmodel.QuizViewModel
import kotlin.math.roundToInt

@Composable
fun LearnScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val category by viewModel.selectedLearnCategory.collectAsState()
    val tableNum by viewModel.learnNumber.collectAsState()
    val activeIndex by viewModel.activeLearnIndex.collectAsState()
    val isPlayingAll by viewModel.isPlayingWholeCategory.collectAsState()
    val learnSpeed by viewModel.learnSpeed.collectAsState()
    val language by viewModel.language.collectAsState()
    val voiceProfile by viewModel.selectedVoiceProfile.collectAsState()

    val isGujarati = language == SupportedLanguage.GUJARATI
    val listState = rememberLazyListState()

    LaunchedEffect(activeIndex) {
        if (activeIndex != null) {
            listState.animateScrollToItem(index = (activeIndex!! + 3).coerceAtLeast(0))
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isGujarati) "📖 ગણિત અને સૂત્રો શીખો" else "📖 Learn Math & Formulas",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isGujarati) "૧ થી ૩૦ ઘડિયા, વર્ગ, ઘન, વર્ગમૂળ & ઘનમૂળ" else "Tables, Squares, Cubes, Square Roots & Cube Roots (1 to 30)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Category Switcher Grid (All options clearly visible, no clipping)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isGujarati) "વિભાગ પસંદ કરો:" else "Select Category:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LearnCategoryTile(
                            category = MathCategory.TABLES,
                            selected = category == MathCategory.TABLES,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setLearnCategory(MathCategory.TABLES) }
                        )
                        LearnCategoryTile(
                            category = MathCategory.SQUARES,
                            selected = category == MathCategory.SQUARES,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setLearnCategory(MathCategory.SQUARES) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LearnCategoryTile(
                            category = MathCategory.CUBES,
                            selected = category == MathCategory.CUBES,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setLearnCategory(MathCategory.CUBES) }
                        )
                        LearnCategoryTile(
                            category = MathCategory.SQUARE_ROOTS,
                            selected = category == MathCategory.SQUARE_ROOTS,
                            language = language,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setLearnCategory(MathCategory.SQUARE_ROOTS) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LearnCategoryTile(
                            category = MathCategory.CUBE_ROOTS,
                            selected = category == MathCategory.CUBE_ROOTS,
                            language = language,
                            modifier = Modifier.fillMaxWidth(0.5f),
                            onClick = { viewModel.setLearnCategory(MathCategory.CUBE_ROOTS) }
                        )
                    }
                }
            }
        }

        // Category-Specific Controls: Table Number Slider for Tables mode
        if (category == MathCategory.TABLES) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = cardColors(containerColor = MaterialTheme.colorScheme.surface)
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

                        // Slider for Table 1 to 30
                        Slider(
                            value = tableNum.toFloat(),
                            onValueChange = { viewModel.setLearnNumber(it.roundToInt()) },
                            valueRange = 1f..30f,
                            steps = 28,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("learn_table_slider")
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
                                        .clickable { viewModel.setLearnNumber(num) }
                                        .testTag("learn_table_chip_$num"),
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

        // Voice Profile Selection (8 distinct voices)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
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
                            val isSel = voiceProfile == profile
                            FilterChip(
                                selected = isSel,
                                onClick = { viewModel.setVoiceProfile(profile) },
                                label = { Text(profile.getTitle(language)) },
                                modifier = Modifier.testTag("voice_chip_${profile.id}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Speed Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isGujarati) "ઝડપ સ્લાઇડર" else "Speech Speed Slider",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${(learnSpeed * 10).roundToInt() / 10.0}x",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Slider(
                        value = learnSpeed,
                        onValueChange = { viewModel.setLearnSpeed((it * 10).roundToInt() / 10f) },
                        valueRange = 0.5f..2.5f,
                        steps = 7,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("learn_speed_slider")
                    )
                }
            }
        }

        // Play All / Stop Reading Button
        item {
            val playBtnText = when (category) {
                MathCategory.TABLES -> if (isPlayingAll) (if (isGujarati) "વાચન બંધ કરો" else "Stop Audio Reading") else (if (isGujarati) "આખો ઘડિયો વાંચો ($tableNum × ૧ થી ૧૦)" else "Play Entire Table ($tableNum × 1 to 10)")
                MathCategory.SQUARES -> if (isPlayingAll) (if (isGujarati) "વાચન બંધ કરો" else "Stop Audio Reading") else (if (isGujarati) "૧ થી ૩૦ ના વર્ગ સાંભળો" else "Play Squares (1² to 30²)")
                MathCategory.CUBES -> if (isPlayingAll) (if (isGujarati) "વાચન બંધ કરો" else "Stop Audio Reading") else (if (isGujarati) "૧ થી ૩૦ ના ઘન સાંભળો" else "Play Cubes (1³ to 30³)")
                MathCategory.SQUARE_ROOTS -> if (isPlayingAll) (if (isGujarati) "વાચન બંધ કરો" else "Stop Audio Reading") else (if (isGujarati) "વર્ગમૂળ સાંભળો (√૧ થી √૯૦૦)" else "Play Square Roots (√1 to √900)")
                MathCategory.CUBE_ROOTS -> if (isPlayingAll) (if (isGujarati) "વાચન બંધ કરો" else "Stop Audio Reading") else (if (isGujarati) "ઘનમૂળ સાંભળો (∛૧ થી ∛૨૭૦૦૦)" else "Play Cube Roots (∛1 to ∛27000)")
                MathCategory.MIXED -> if (isPlayingAll) (if (isGujarati) "વાચન બંધ કરો" else "Stop Audio Reading") else (if (isGujarati) "બધા સાંભળો" else "Play All Audio")
            }

            Button(
                onClick = { viewModel.togglePlayFullCategory() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("play_full_table_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPlayingAll) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    if (isPlayingAll) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = playBtnText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Items List depending on category
        if (category == MathCategory.TABLES) {
            items((1..10).toList()) { multiplier ->
                val isActive = activeIndex == multiplier
                val product = tableNum * multiplier

                val cardColor by animateColorAsState(
                    targetValue = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    label = "line_color"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.playSingleTableLine(tableNum, multiplier) }
                        .testTag("table_line_$multiplier"),
                    shape = RoundedCornerShape(14.dp),
                    colors = cardColors(containerColor = cardColor),
                    elevation = cardElevation(defaultElevation = if (isActive) 6.dp else 2.dp),
                    border = if (isActive) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$tableNum",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "  ×  ",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$multiplier",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "  =  ",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$product",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Icon(
                            Icons.Default.VolumeUp,
                            contentDescription = "Speak row",
                            tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        } else {
            items((1..30).toList()) { baseNum ->
                val isActive = activeIndex == baseNum
                val (formulaText, resultText) = when (category) {
                    MathCategory.SQUARES -> Pair("$baseNum²", "${baseNum * baseNum}")
                    MathCategory.CUBES -> Pair("$baseNum³", "${baseNum * baseNum * baseNum}")
                    MathCategory.SQUARE_ROOTS -> Pair("√${baseNum * baseNum}", "$baseNum")
                    MathCategory.CUBE_ROOTS -> Pair("∛${baseNum * baseNum * baseNum}", "$baseNum")
                    else -> Pair("$baseNum", "$baseNum")
                }

                val item = MathItem(category, baseNum, formulaText, resultText, "$formulaText = $resultText")

                val cardColor by animateColorAsState(
                    targetValue = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    label = "math_item_color"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.playSingleItemAudio(item) }
                        .testTag("math_item_$baseNum"),
                    shape = RoundedCornerShape(14.dp),
                    colors = cardColors(containerColor = cardColor),
                    elevation = cardElevation(defaultElevation = if (isActive) 6.dp else 2.dp),
                    border = if (isActive) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "$baseNum",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = formulaText,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "  =  ",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = resultText,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Icon(
                            Icons.Default.VolumeUp,
                            contentDescription = "Speak formula",
                            tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LearnCategoryTile(
    category: MathCategory,
    selected: Boolean,
    language: SupportedLanguage,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(52.dp)
            .clickable { onClick() }
            .testTag("learn_cat_${category.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = cardElevation(defaultElevation = if (selected) 4.dp else 1.dp),
        border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
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
