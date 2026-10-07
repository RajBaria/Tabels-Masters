package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.AuthManager
import com.example.model.SupportedLanguage
import com.example.ui.components.GlassCard
import com.example.ui.viewmodel.QuizViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileAuthScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val profiles by viewModel.allProfiles.collectAsState()
    val currentProfile by viewModel.currentProfile.collectAsState()
    val googleAccount by viewModel.googleAccount.collectAsState()
    val backupStatusMessage by viewModel.backupStatusMessage.collectAsState()
    val language by viewModel.language.collectAsState()

    val isGujarati = language == SupportedLanguage.GUJARATI

    var showGoogleSignInDialog by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var showPinDialogForProfileId by remember { mutableStateOf<Long?>(null) }
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
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
                        text = if (isGujarati) "👤 પ્રોફાઇલ & ગૂગલ ડ્રાઇવ" else "👤 Profiles & Cloud Backup",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isGujarati) "વિદ્યાર્થી પ્રોફાઇલ્સ અને ગૂગલ ડ્રાઇવ સિંક" else "Student profiles and secure Google Drive data sync",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Google Account & Google Drive Sync Card
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
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF4285F4).copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("G", fontWeight = FontWeight.Black, color = Color(0xFF4285F4), fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isGujarati) "ગૂગલ એકાઉન્ટ સિંક" else "Google Drive Sync",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (googleAccount.isLinked) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF34A853).copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CloudDone, contentDescription = null, tint = Color(0xFF34A853), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isGujarati) "લિંક થયેલ છે" else "Connected",
                                        color = Color(0xFF34A853),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CloudOff, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isGujarati) "સાઇન ઇન નથી" else "Not Signed In",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (googleAccount.isLinked) {
                        Text(
                            text = if (isGujarati) "એકાઉન્ટ: ${googleAccount.email}" else "Account: ${googleAccount.email}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        if (googleAccount.lastBackupTimestamp > 0) {
                            val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
                            val lastBackupDate = dateFormat.format(Date(googleAccount.lastBackupTimestamp))
                            Text(
                                text = if (isGujarati) "છેલ્લું બેકઅપ: $lastBackupDate" else "Last Cloud Backup: $lastBackupDate",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Text(
                            text = if (isGujarati) "ગૂગલ એકાઉન્ટ સાથે સાઇન ઇન કરી તમારો ડેટા ડ્રાઇવમાં સુરક્ષિત રાખો." else "Sign in with your Google Account to backup your reports and mastery data safely to Google Drive.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (backupStatusMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = backupStatusMessage!!,
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (googleAccount.isLinked) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.backupDataToGoogleDrive() },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_drive_backup"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isGujarati) "ડ્રાઇવ બેકઅપ" else "Backup to Drive", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.restoreDataFromGoogleDrive() },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_drive_restore"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isGujarati) "રીસ્ટોર" else "Restore", fontSize = 13.sp)
                            }

                            IconButton(onClick = { viewModel.signOutGoogle() }) {
                                Icon(Icons.Default.Logout, contentDescription = "Sign Out", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    } else {
                        // User-initiated Google Sign-In Button
                        Button(
                            onClick = { showGoogleSignInDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_google_signin"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                        ) {
                            Text("G", fontWeight = FontWeight.Black, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(if (isGujarati) "ગૂગલ વડે સાઇન ઇન કરો" else "Sign In with Google", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }

        // Active Profile Card
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
                            text = if (isGujarati) "હાલની સક્રિય પ્રોફાઇલ" else "Current Active Profile",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (currentProfile?.pinHash != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = "PIN Protected",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "PIN Protected",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (currentProfile != null) {
                        val emoji = AuthManager.avatarOptions.getOrNull(currentProfile!!.avatarIndex) ?: "🦁"
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                modifier = Modifier.size(64.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = emoji, fontSize = 32.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = currentProfile!!.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = currentProfile!!.gradeOrLevel,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Switch Profiles List
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
                            text = if (isGujarati) "બધી પ્રોફાઇલ્સ (${profiles.size})" else "All Profiles (${profiles.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = { showCreateDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("add_profile_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isGujarati) "નવી પ્રોફાઇલ" else "New Profile")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    profiles.forEach { p ->
                        val isSelected = p.id == currentProfile?.id
                        val emoji = AuthManager.avatarOptions.getOrNull(p.avatarIndex) ?: "🦁"

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    if (p.pinHash != null && !isSelected) {
                                        showPinDialogForProfileId = p.id
                                    } else {
                                        viewModel.switchProfile(p.id)
                                    }
                                }
                                .testTag("profile_card_${p.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
                            border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = emoji, fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = p.name,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = p.gradeOrLevel,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (p.pinHash != null) {
                                        Icon(
                                            Icons.Default.Lock,
                                            contentDescription = "Secured",
                                            tint = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier
                                                .size(18.dp)
                                                .padding(end = 8.dp)
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Active",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    } else if (profiles.size > 1) {
                                        IconButton(onClick = { viewModel.deleteProfile(p.id) }) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                                                modifier = Modifier.size(18.dp)
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
    }

    // Google Sign In Dialog
    if (showGoogleSignInDialog) {
        var emailInput by remember { mutableStateOf("rk3171998@gmail.com") }
        var nameInput by remember { mutableStateOf("Raj Baria") }

        AlertDialog(
            onDismissRequest = { showGoogleSignInDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("G", color = Color(0xFF4285F4), fontWeight = FontWeight.Black, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(if (isGujarati) "ગૂગલ સાઇન ઇન" else "Sign In with Google")
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isGujarati) "તમારું ગૂગલ એકાઉન્ટ ઇમેઇલ દાખલ કરો:" else "Enter your Google account email to link with Drive:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Google Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_google_email")
                    )

                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Display Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.signInWithGoogle(emailInput, nameInput)
                        showGoogleSignInDialog = false
                    },
                    modifier = Modifier.testTag("btn_confirm_google_signin"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                ) {
                    Text(if (isGujarati) "સાઇન ઇન કરો" else "Sign In")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGoogleSignInDialog = false }) {
                    Text(if (isGujarati) "રદ કરો" else "Cancel")
                }
            }
        )
    }

    // Create Profile Dialog
    if (showCreateDialog) {
        var name by remember { mutableStateOf("") }
        var grade by remember { mutableStateOf("Grade 4") }
        var avatarIdx by remember { mutableIntStateOf(0) }
        var pin by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text(if (isGujarati) "નવી પ્રોફાઇલ બનાવો" else "Create Student Profile", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(if (isGujarati) "વિદ્યાર્થીનું નામ" else "Student Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_profile_name")
                    )

                    OutlinedTextField(
                        value = grade,
                        onValueChange = { grade = it },
                        label = { Text(if (isGujarati) "ધોરણ / વર્ગ" else "Standard / Grade") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(text = if (isGujarati) "અવતાર પસંદ કરો:" else "Choose Avatar:", style = MaterialTheme.typography.labelMedium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(AuthManager.avatarOptions.indices.toList()) { idx ->
                            val isSel = avatarIdx == idx
                            Surface(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .clickable { avatarIdx = idx },
                                color = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape,
                                border = if (isSel) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = AuthManager.avatarOptions[idx], fontSize = 20.sp)
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = pin,
                        onValueChange = { if (it.length <= 4) pin = it },
                        label = { Text(if (isGujarati) "ઓપ્શનલ ૪-અંકનો PIN" else "Optional 4-digit PIN") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_profile_pin")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            viewModel.createProfile(name.trim(), grade.trim(), avatarIdx, pin.ifBlank { null })
                            showCreateDialog = false
                        }
                    },
                    modifier = Modifier.testTag("save_profile_button")
                ) {
                    Text(if (isGujarati) "પ્રોફાઇલ બનાવો" else "Create Profile")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text(if (isGujarati) "રદ કરો" else "Cancel")
                }
            }
        )
    }

    // PIN Authentication Dialog
    if (showPinDialogForProfileId != null) {
        AlertDialog(
            onDismissRequest = {
                showPinDialogForProfileId = null
                pinInput = ""
                pinError = null
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isGujarati) "પ્રોફાઇલ PIN દાખલ કરો" else "Enter Profile PIN")
                }
            },
            text = {
                Column {
                    Text(if (isGujarati) "આ પ્રોફાઇલ PIN વડે સુરક્ષિત છે." else "This student profile is secured with a PIN.")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = {
                            if (it.length <= 6) pinInput = it
                            pinError = null
                        },
                        label = { Text("4-digit PIN") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        isError = pinError != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("enter_pin_input")
                    )
                    if (pinError != null) {
                        Text(
                            text = pinError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val targetId = showPinDialogForProfileId!!
                        viewModel.switchProfile(targetId)
                        val success = viewModel.unlockProfile(pinInput)
                        if (success) {
                            showPinDialogForProfileId = null
                            pinInput = ""
                            pinError = null
                        } else {
                            pinError = if (isGujarati) "ખોટો PIN. ફરીથી પ્રયત્ન કરો." else "Incorrect PIN. Please try again."
                        }
                    },
                    modifier = Modifier.testTag("confirm_pin_button")
                ) {
                    Text(if (isGujarati) "અનલૉક કરો" else "Unlock")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPinDialogForProfileId = null
                    pinInput = ""
                    pinError = null
                }) {
                    Text(if (isGujarati) "રદ કરો" else "Cancel")
                }
            }
        )
    }
}
