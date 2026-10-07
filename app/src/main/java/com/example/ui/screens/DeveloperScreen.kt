package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard

@Composable
fun DeveloperScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Avatar & Header
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6), Color(0xFFEC4899))
                                )
                            )
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "RB",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Rajkumar Baria",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = "Verified Creator",
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Text(
                        text = "App Developer & Creator",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Passionate about creating accessible, engaging educational tools in regional languages.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        // Contact & Social Links
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Connect with Creator",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    SocialLinkCard(
                        title = "Facebook",
                        subtitle = "Follow on Facebook",
                        icon = Icons.Default.Share,
                        badgeColor = Color(0xFF1877F2),
                        testTag = "dev_link_fb",
                        onClick = { openUrl(context, "https://www.facebook.com/share/1EK5jqWrK9/") }
                    )

                    SocialLinkCard(
                        title = "Instagram",
                        subtitle = "@rajsbaria",
                        icon = Icons.Default.Person,
                        badgeColor = Color(0xFFE1306C),
                        testTag = "dev_link_ig",
                        onClick = { openUrl(context, "https://instagram.com/rajsbaria") }
                    )

                    SocialLinkCard(
                        title = "X (Twitter)",
                        subtitle = "@raj_baria",
                        icon = Icons.Default.OpenInNew,
                        badgeColor = Color(0xFF1DA1F2),
                        testTag = "dev_link_x",
                        onClick = { openUrl(context, "https://x.com/raj_baria") }
                    )

                    SocialLinkCard(
                        title = "WhatsApp",
                        subtitle = "+91 6352840795",
                        icon = Icons.Default.Phone,
                        badgeColor = Color(0xFF25D366),
                        testTag = "dev_link_wa",
                        onClick = { openUrl(context, "https://wa.me/916352840795") }
                    )

                    SocialLinkCard(
                        title = "Email Me",
                        subtitle = "rk3171998@gmail.com",
                        icon = Icons.Default.Email,
                        badgeColor = Color(0xFFEA4335),
                        testTag = "dev_link_email",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:rk3171998@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Feedback on Tables Master App")
                            }
                            context.startActivity(Intent.createChooser(intent, "Send Email"))
                        }
                    )

                    SocialLinkCard(
                        title = "Website & Downloads",
                        subtitle = "rajbaria.github.io/my-downloads",
                        icon = Icons.Default.Language,
                        badgeColor = Color(0xFF4F46E5),
                        testTag = "dev_link_web",
                        onClick = { openUrl(context, "https://rajbaria.github.io/my-downloads/") }
                    )

                    SocialLinkCard(
                        title = "Privacy Policy",
                        subtitle = "View safety & data privacy details",
                        icon = Icons.Default.Security,
                        badgeColor = Color(0xFF059669),
                        testTag = "dev_link_privacy",
                        onClick = { showPrivacyPolicyDialog = true }
                    )
                }
            }
        }

        // App Information
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tables Master v2.1",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "© 2026 Rajkumar Baria. All rights reserved.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Built with Jetpack Compose & Offline-first Room Database",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

    if (showPrivacyPolicyDialog) {
        PrivacyPolicyDialog(onDismiss = { showPrivacyPolicyDialog = false })
    }
}

@Composable
fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Privacy Policy", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Tables Master Privacy Policy",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "App: Tables Master\nPackage: com.rajkumarbaria.tablesmaster\nDeveloper: Rajkumar Baria\nEmail: rk3171998@gmail.com",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "1. Data Collection: None\nWe do NOT collect, transmit, sell, or rent any personal information. All your learning records, scores, mistakes, and profiles are stored 100% locally on your device.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "2. Google Drive Backup\nCloud backup is entirely user-initiated and optional. Your data is exported directly into your private Google Drive account. We do not operate external proprietary storage servers.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "3. Permissions\n- VIBRATE: Used for haptic feedback during quiz answers.\n- INTERNET: Used strictly for optional Google Drive backup and external creator links.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "4. Children's Privacy\nTables Master contains NO third-party advertisements, NO analytics tracking, and NO profiling. It is fully COPPA & GDPR-K compliant for learners of all ages.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun SocialLinkCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = badgeColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Icon(
                Icons.Default.OpenInNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (_: Exception) {}
}
