package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// Material 3 Deep Purple Color Palette
// ==========================================

// Primary Palette (Deep Purple)
val M3DeepPurplePrimary = Color(0xFFA855F7)          // Accessible contrast in dark mode
val M3DeepPurplePrimaryLight = Color(0xFF6B21A8)     // Deep rich purple for light mode
val M3DeepPurpleOnPrimary = Color(0xFF1E0038)
val M3DeepPurpleContainer = Color(0xFF3B0764)       // Deep purple container
val M3DeepPurpleOnContainer = Color(0xFFF3E8FF)     // Crisp accessible lavender

// Secondary Palette (Vibrant Violet)
val M3VioletSecondary = Color(0xFFC084FC)
val M3VioletSecondaryLight = Color(0xFF7E22CE)
val M3VioletOnSecondary = Color(0xFF2E004F)
val M3VioletContainer = Color(0xFF280A48)
val M3VioletOnContainer = Color(0xFFE9D5FF)

// Tertiary Palette (Amethyst Accent)
val M3AmethystTertiary = Color(0xFFE879F9)
val M3AmethystTertiaryLight = Color(0xFF9333EA)
val M3AmethystOnTertiary = Color(0xFF3A004C)
val M3AmethystContainer = Color(0xFF4A0E4E)
val M3AmethystOnContainer = Color(0xFFFDF4FF)

// Dark Theme Surfaces (Deep Midnight Violet)
val M3DarkBackground = Color(0xFF090510)            // Deep midnight purple
val M3DarkSurface = Color(0xFF130B21)               // Deep dark purple card/surface
val M3DarkSurfaceVariant = Color(0xFF1D1131)        // Elevated pill/bubble containers
val M3DarkOutline = Color(0xFF3B2256)               // Subtle border
val M3DarkOutlineVariant = Color(0xFF25163A)

// Light Theme Surfaces (Soft Lavender Wash)
val M3LightBackground = Color(0xFFFAF7FD)
val M3LightSurface = Color(0xFFFFFFFF)
val M3LightSurfaceVariant = Color(0xFFF3EAFB)
val M3LightOutline = Color(0xFFDAC7EA)
val M3LightOutlineVariant = Color(0xFFECE1F5)

// High-Contrast Universally Accessible Typography Colors
val M3TextPrimaryDark = Color(0xFFFAF5FF)           // 18+:1 contrast against background
val M3TextSecondaryDark = Color(0xFFC4B5FD)         // 9+:1 contrast against background
val M3TextTertiaryDark = Color(0xFFA78BFA)

val M3TextPrimaryLight = Color(0xFF240E3E)
val M3TextSecondaryLight = Color(0xFF5B3B7A)

// Functional Aliases for Components
val AuraNightBackground = M3DarkBackground
val AuraPillContainer = M3DarkSurfaceVariant
val AuraPillSelected = Color(0xFF6B21A8)
val AuraPillSelectedGlow = Color(0xFF7E22CE)
val AuraBubbleBackground = M3DarkSurface
val AuraBubbleBorder = M3DarkOutline
val AuraSendButton = Color(0xFF9333EA)
val AuraMicButton = Color(0xFF26153E)
val AuraTextPrimary = M3TextPrimaryDark
val AuraTextMuted = M3TextSecondaryDark
val AuraDivider = M3DarkOutlineVariant

val AuraPurplePrimary = M3DeepPurplePrimary
val AuraPurplePrimaryDark = M3DeepPurplePrimaryLight
val AuraPurpleLight = Color(0xFFE9D5FF)
val AuraPurpleContainer = M3DarkSurfaceVariant
val AuraPurpleContainerDark = M3DarkSurface
val AuraLilacSecondary = M3VioletSecondary
val AuraLilacLight = Color(0xFFF5D0FE)
val AuraLilacContainerDark = Color(0xFF381245)
val AuraPeachWarm = Color(0xFFF472B6)

val CuteDarkBackground = AuraNightBackground
val CuteDarkSurface = AuraBubbleBackground
val CuteDarkSurfaceVariant = AuraPillContainer
val CuteDarkOutline = AuraBubbleBorder

val CuteLightBackground = M3LightBackground
val CuteLightSurface = M3LightSurface
val CuteLightSurfaceVariant = M3LightSurfaceVariant
val CuteLightOutline = M3LightOutline

val TextPrimaryDark = AuraTextPrimary
val TextSecondaryDark = AuraTextMuted
val TextPrimaryLight = M3TextPrimaryLight
val TextSecondaryLight = M3TextSecondaryLight

val Purple80 = AuraPurpleLight
val PurpleGrey80 = AuraLilacLight
val Pink80 = Color(0xFFFDA4AF)
val Purple40 = AuraPurplePrimary
val PurpleGrey40 = AuraLilacSecondary
val Pink40 = Color(0xFFF472B6)
