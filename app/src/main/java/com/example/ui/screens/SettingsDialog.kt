package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.SupportedLanguage
import com.example.model.VoiceProfile
import com.example.ui.theme.AppThemeMode
import com.example.ui.viewmodel.QuizViewModel

@Composable
fun SettingsDialog(
    viewModel: QuizViewModel,
    onDismiss: () -> Unit
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val hapticEnabled by viewModel.hapticEnabled.collectAsState()
    val currentLang by viewModel.language.collectAsState()
    val voiceProfile by viewModel.selectedVoiceProfile.collectAsState()

    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (currentLang == SupportedLanguage.GUJARATI) "સેટિંગ્સ (Settings)" else "App Settings",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // App & Audio Language Selection
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Language,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentLang == SupportedLanguage.GUJARATI) "ભાષા પસંદ કરો (Language)" else "App & Audio Language",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(SupportedLanguage.values()) { lang ->
                            val isSel = currentLang == lang
                            FilterChip(
                                selected = isSel,
                                onClick = { viewModel.setLanguage(lang) },
                                label = { Text(if (lang == SupportedLanguage.ENGLISH) "English" else "${lang.nativeName} (${lang.displayName})") },
                                modifier = Modifier.testTag("settings_lang_${lang.name}")
                            )
                        }
                    }
                }

                // Voice Style Selection
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentLang == SupportedLanguage.GUJARATI) "અવાજ શૈલી (8 Voices)" else "Audio Voice Style (8 Voices)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(VoiceProfile.values()) { profile ->
                            FilterChip(
                                selected = voiceProfile == profile,
                                onClick = { viewModel.setVoiceProfile(profile) },
                                label = { Text(profile.getTitle(currentLang)) },
                                modifier = Modifier.testTag("settings_voice_${profile.id}")
                            )
                        }
                    }
                }

                // Dark Mode / Theme Section
                Column {
                    Text(
                        text = if (currentLang == SupportedLanguage.GUJARATI) "થીમ (Theme)" else "Theme & Appearance",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = themeMode == AppThemeMode.SYSTEM,
                            onClick = { viewModel.setThemeMode(AppThemeMode.SYSTEM) },
                            label = { Text("Auto") },
                            leadingIcon = { Icon(Icons.Default.SettingsBrightness, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_chip_auto")
                        )
                        FilterChip(
                            selected = themeMode == AppThemeMode.LIGHT,
                            onClick = { viewModel.setThemeMode(AppThemeMode.LIGHT) },
                            label = { Text("Light") },
                            leadingIcon = { Icon(Icons.Default.LightMode, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_chip_light")
                        )
                        FilterChip(
                            selected = themeMode == AppThemeMode.DARK,
                            onClick = { viewModel.setThemeMode(AppThemeMode.DARK) },
                            label = { Text("Dark") },
                            leadingIcon = { Icon(Icons.Default.DarkMode, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_chip_dark")
                        )
                    }
                }

                // Haptic Feedback Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Vibration,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (currentLang == SupportedLanguage.GUJARATI) "વાઇબ્રેશન ફીડબેક" else "Haptic Feedback",
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = if (currentLang == SupportedLanguage.GUJARATI) "જવાબ આપતી વખતે વાઇબ્રેશન" else "Vibrate on quiz answers",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = hapticEnabled,
                        onCheckedChange = { viewModel.toggleHaptic() },
                        modifier = Modifier.testTag("haptic_switch")
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.testTag("settings_done_button")) {
                Text(if (currentLang == SupportedLanguage.GUJARATI) "પૂર્ણ (Done)" else "Done", fontWeight = FontWeight.Bold)
            }
        }
    )
}
