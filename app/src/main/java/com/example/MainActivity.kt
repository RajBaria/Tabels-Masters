package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ui.screens.MainAppScreen
import com.example.ui.theme.GadiyaMasterTheme
import com.example.ui.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.themeMode.collectAsState()

            GadiyaMasterTheme(themeMode = themeMode) {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}
