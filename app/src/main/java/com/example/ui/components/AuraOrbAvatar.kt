package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AuraOrbAvatar(
    size: Dp = 36.dp,
    animate: Boolean = true,
    isSpeaking: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb_pulse")

    // Lifelike organic breathing scale
    val pulseScale by if (animate) {
        infiniteTransition.animateFloat(
            initialValue = if (isSpeaking) 0.92f else 0.96f,
            targetValue = if (isSpeaking) 1.10f else 1.04f,
            animationSpec = infiniteRepeatable(
                animation = tween(if (isSpeaking) 1100 else 2400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_scale"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    }

    // Expanding resonance ripple
    val rippleAlpha by if (animate) {
        infiniteTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 0.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(2800, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "ripple_alpha"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    val rippleRadiusFactor by if (animate) {
        infiniteTransition.animateFloat(
            initialValue = 0.7f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(2800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "ripple_radius"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    }

    Box(
        modifier = modifier
            .size(size)
            .scale(pulseScale),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val maxRadius = this.size.minDimension / 2f

            // Outer expanding aura resonance wave
            if (animate) {
                drawCircle(
                    color = Color(0xFFA855F7).copy(alpha = rippleAlpha * 0.4f),
                    radius = maxRadius * rippleRadiusFactor,
                    center = center,
                    style = Stroke(width = 1.2.dp.toPx())
                )
            }

            // Outer soft ambient purple glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF9333EA).copy(alpha = 0.55f),
                        Color(0xFF6B21A8).copy(alpha = 0.25f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = maxRadius
                ),
                radius = maxRadius,
                center = center
            )

            // Outer distinct orbital ring
            drawCircle(
                color = Color(0xFFA855F7).copy(alpha = 0.65f),
                radius = maxRadius * 0.86f,
                center = center,
                style = Stroke(width = 1.8.dp.toPx())
            )

            // Middle glowing violet ring
            drawCircle(
                color = Color(0xFFC084FC).copy(alpha = 0.9f),
                radius = maxRadius * 0.63f,
                center = center,
                style = Stroke(width = 2.4.dp.toPx())
            )

            // Inner high-luminance ring
            drawCircle(
                color = Color(0xFFF3E8FF),
                radius = maxRadius * 0.41f,
                center = center,
                style = Stroke(width = 1.6.dp.toPx())
            )

            // Radiant white-violet living core
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFAF5FF),
                        Color(0xFFC084FC)
                    ),
                    center = center,
                    radius = maxRadius * 0.28f
                ),
                radius = maxRadius * 0.28f,
                center = center
            )
        }
    }
}
