package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AuraNightBackground
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.viewmodel.AuraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: AuraViewModel) {
    var showSettings by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = showSettings) {
        showSettings = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraNightBackground)
            .statusBarsPadding()
    ) {
        AnimatedContent(
            targetState = showSettings,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen_transition"
        ) { inSettings ->
            if (inSettings) {
                Scaffold(
                    containerColor = AuraNightBackground,
                    topBar = {
                        TopAppBar(
                            title = { Text("Aura Companion Settings 🎛️", color = AuraTextPrimary) },
                            navigationIcon = {
                                IconButton(onClick = { showSettings = false }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = AuraTextPrimary
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = AuraNightBackground
                            )
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SettingsScreen(viewModel = viewModel)
                    }
                }
            } else {
                ChatScreen(
                    viewModel = viewModel,
                    onOpenSettings = { showSettings = true }
                )
            }
        }
    }
}
