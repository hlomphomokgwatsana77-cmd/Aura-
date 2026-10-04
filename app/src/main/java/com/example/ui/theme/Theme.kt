package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = M3DeepPurplePrimary,
    onPrimary = M3DeepPurpleOnPrimary,
    primaryContainer = M3DeepPurpleContainer,
    onPrimaryContainer = M3DeepPurpleOnContainer,
    secondary = M3VioletSecondary,
    onSecondary = M3VioletOnSecondary,
    secondaryContainer = M3VioletContainer,
    onSecondaryContainer = M3VioletOnContainer,
    tertiary = M3AmethystTertiary,
    onTertiary = M3AmethystOnTertiary,
    tertiaryContainer = M3AmethystContainer,
    onTertiaryContainer = M3AmethystOnContainer,
    background = M3DarkBackground,
    onBackground = M3TextPrimaryDark,
    surface = M3DarkSurface,
    onSurface = M3TextPrimaryDark,
    surfaceVariant = M3DarkSurfaceVariant,
    onSurfaceVariant = M3TextSecondaryDark,
    outline = M3DarkOutline,
    outlineVariant = M3DarkOutlineVariant
)

private val LightColorScheme = lightColorScheme(
    primary = M3DeepPurplePrimaryLight,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Color(0xFFF3E8FF),
    onPrimaryContainer = Color(0xFF3B0764),
    secondary = M3VioletSecondaryLight,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = Color(0xFFEDE9FE),
    onSecondaryContainer = Color(0xFF2E1065),
    tertiary = M3AmethystTertiaryLight,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    tertiaryContainer = Color(0xFFFDF4FF),
    onTertiaryContainer = Color(0xFF4A0E4E),
    background = M3LightBackground,
    onBackground = M3TextPrimaryLight,
    surface = M3LightSurface,
    onSurface = M3TextPrimaryLight,
    surfaceVariant = M3LightSurfaceVariant,
    onSurfaceVariant = M3TextSecondaryLight,
    outline = M3LightOutline,
    outlineVariant = M3LightOutlineVariant
)

@Composable
fun AuraTheme(
    darkTheme: Boolean = true, // Default to sleek night deep purple aesthetic
    dynamicColor: Boolean = false, // Preserve deep purple brand palette
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    AuraTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
