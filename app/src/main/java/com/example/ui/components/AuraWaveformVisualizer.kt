package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sin

@Composable
fun AuraWaveformVisualizer(
    isLive: Boolean = true,
    height: Dp = 60.dp,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "waveform_anim")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(if (isLive) 1800 else 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val width = size.width
        val midY = size.height / 2f
        val maxAmplitude = if (isLive) size.height * 0.38f else size.height * 0.12f

        // Draw multiple smooth harmonic sinusoidal waves
        val waveConfigs = listOf(
            Triple(1.0f, Color(0xFFA855F7), 2.5.dp.toPx()),
            Triple(1.8f, Color(0xFFC084FC).copy(alpha = 0.85f), 2.0.dp.toPx()),
            Triple(2.6f, Color(0xFFE9D5FF).copy(alpha = 0.7f), 1.5.dp.toPx())
        )

        for ((freq, color, strokeWidth) in waveConfigs) {
            val path = Path()
            val points = 80
            for (i in 0..points) {
                val x = (i.toFloat() / points) * width
                // Damped at screen edges for lifelike smooth envelope
                val normalizedX = (x / width) * 2f - 1f
                val envelope = (1f - normalizedX * normalizedX).coerceIn(0f, 1f)

                val wave = sin((x / width * 3.5f * Math.PI + phase * freq).toDouble()).toFloat()
                val y = midY + wave * maxAmplitude * envelope

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        color,
                        Color(0xFFF3E8FF),
                        color,
                        Color.Transparent
                    )
                ),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}
