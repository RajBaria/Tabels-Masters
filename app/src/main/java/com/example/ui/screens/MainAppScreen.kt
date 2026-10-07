package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppThemeMode
import com.example.ui.viewmodel.QuizPhase
import com.example.ui.viewmodel.QuizViewModel

enum class AppTab(val label: String, val icon: ImageVector, val tag: String) {
    HOME("Home", Icons.Default.Home, "tab_home"),
    QUIZ("Quiz", Icons.Default.Timer, "tab_quiz"),
    LEARN("Learn", Icons.Default.Book, "tab_learn"),
    ANALYTICS("Analytics", Icons.Default.Analytics, "tab_analytics"),
    PROFILES("Profiles", Icons.Default.ManageAccounts, "tab_profiles"),
    DEVELOPER("Developer", Icons.Default.Code, "tab_developer")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: QuizViewModel
) {
    val hasCompletedOnboarding by viewModel.hasCompletedOnboarding.collectAsState()

    // If first time opening app, show the onboarding guide
    if (!hasCompletedOnboarding) {
        OnboardingScreen(onFinish = { viewModel.completeOnboarding() })
        return
    }

    var currentTab by remember { mutableStateOf(AppTab.HOME) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val themeMode by viewModel.themeMode.collectAsState()
    val quizPhase by viewModel.quizPhase.collectAsState()

    BackHandler(enabled = currentTab != AppTab.HOME || quizPhase != QuizPhase.SETUP) {
        if (currentTab == AppTab.QUIZ && quizPhase != QuizPhase.SETUP) {
            viewModel.resetToQuizSetup()
        } else {
            currentTab = AppTab.HOME
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Tables Master",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    actions = {
                        IconButton(
                            onClick = {
                                val nextTheme = when (themeMode) {
                                    AppThemeMode.LIGHT -> AppThemeMode.DARK
                                    AppThemeMode.DARK -> AppThemeMode.SYSTEM
                                    AppThemeMode.SYSTEM -> AppThemeMode.LIGHT
                                }
                                viewModel.setThemeMode(nextTheme)
                            },
                            modifier = Modifier.testTag("quick_theme_toggle")
                        ) {
                            Icon(
                                imageVector = when (themeMode) {
                                    AppThemeMode.DARK -> Icons.Default.DarkMode
                                    AppThemeMode.LIGHT -> Icons.Default.LightMode
                                    AppThemeMode.SYSTEM -> Icons.Default.Palette
                                },
                                contentDescription = "Toggle Theme"
                            )
                        }

                        IconButton(
                            onClick = { showSettingsDialog = true },
                            modifier = Modifier.testTag("open_settings_button")
                        ) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                if (!isWideScreen) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        AppTab.values().forEach { tab ->
                            val isSelected = currentTab == tab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    if (currentTab != tab) {
                                        viewModel.stopLearnAudio()
                                    }
                                    currentTab = tab
                                },
                                icon = {
                                    Icon(
                                        tab.icon,
                                        contentDescription = tab.label,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.label,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        maxLines = 1
                                    )
                                },
                                modifier = Modifier.testTag(tab.tag)
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Wide Screen Navigation Rail
                if (isWideScreen) {
                    NavigationRail(
                        modifier = Modifier.fillMaxHeight(),
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        AppTab.values().forEach { tab ->
                            val isSelected = currentTab == tab
                            NavigationRailItem(
                                selected = isSelected,
                                onClick = {
                                    if (currentTab != tab) {
                                        viewModel.stopLearnAudio()
                                    }
                                    currentTab = tab
                                },
                                icon = { Icon(tab.icon, contentDescription = tab.label) },
                                label = { Text(tab.label) },
                                modifier = Modifier.testTag("${tab.tag}_rail")
                            )
                        }
                    }
                }

                // Main Content constrained for optimal readability on tablets
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = 680.dp)
                    ) {
                        when (currentTab) {
                            AppTab.HOME -> HomeScreen(
                                viewModel = viewModel,
                                onNavigateToQuiz = { cat ->
                                    viewModel.setQuizCategory(cat)
                                    currentTab = AppTab.QUIZ
                                },
                                onNavigateToLearn = { cat ->
                                    viewModel.setLearnCategory(cat)
                                    currentTab = AppTab.LEARN
                                },
                                onNavigateToAnalytics = { currentTab = AppTab.ANALYTICS },
                                onNavigateToProfiles = { currentTab = AppTab.PROFILES },
                                onOpenTutorial = { viewModel.restartOnboardingTour() }
                            )
                            AppTab.QUIZ -> QuizScreen(viewModel = viewModel)
                            AppTab.LEARN -> LearnScreen(viewModel = viewModel)
                            AppTab.ANALYTICS -> AnalyticsDashboardScreen(viewModel = viewModel)
                            AppTab.PROFILES -> ProfileAuthScreen(viewModel = viewModel)
                            AppTab.DEVELOPER -> DeveloperScreen()
                        }
                    }
                }
            }
        }
    }

    if (showSettingsDialog) {
        SettingsDialog(
            viewModel = viewModel,
            onDismiss = { showSettingsDialog = false }
        )
    }
}
