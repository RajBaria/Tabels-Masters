package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TimerBar(
    progress: Float, // 1.0f down to 0.0f
    modifier: Modifier = Modifier
) {
    val barColor = when {
        progress > 0.5f -> Brush.horizontalGradient(listOf(Color(0xFF10B981), Color(0xFF3B82F6)))
        progress > 0.25f -> Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Color(0xFFEAB308)))
        else -> Brush.horizontalGradient(listOf(Color(0xFFEF4444), Color(0xFFDC2626)))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(barColor)
        )
    }
}
