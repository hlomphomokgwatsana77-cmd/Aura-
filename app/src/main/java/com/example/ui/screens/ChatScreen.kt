package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ChatMessageEntity
import com.example.ui.components.AuraOrbAvatar
import com.example.ui.components.AuraWaveformVisualizer
import com.example.ui.theme.AuraBubbleBackground
import com.example.ui.theme.AuraBubbleBorder
import com.example.ui.theme.AuraDivider
import com.example.ui.theme.AuraMicButton
import com.example.ui.theme.AuraNightBackground
import com.example.ui.theme.AuraPillContainer
import com.example.ui.theme.AuraPillSelected
import com.example.ui.theme.AuraSendButton
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.viewmodel.AuraViewModel

@Composable
fun ChatScreen(
    viewModel: AuraViewModel,
    onOpenSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isThinking by viewModel.isThinking.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()

    var inputText by remember { mutableStateOf("") }
    var showClearDialog by remember { mutableStateOf(false) }
    var selectedTopNav by remember { mutableIntStateOf(0) }
    var isVoiceActive by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Auto-scroll to the bottom when new message arrives
    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    if (showClearDialog) {
        AlertDialog(
            containerColor = AuraBubbleBackground,
            titleContentColor = AuraTextPrimary,
            textContentColor = AuraTextMuted,
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear Conversation?") },
            text = { Text("Clear all messages and start fresh with Aura?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearChat()
                        showClearDialog = false
                    },
                    modifier = Modifier.testTag("confirm_clear_button")
                ) {
                    Text("Clear", color = Color(0xFFC084FC))
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = AuraTextMuted)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AuraNightBackground)
            .imePadding()
    ) {
        // Top 'X' Close icon row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Soft close / reset */ },
                modifier = Modifier
                    .size(36.dp)
                    .testTag("close_header_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = AuraTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Top Navigation Capsule Bar (Matching Screenshot)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Glowing Aura Orb Icon
            AuraOrbAvatar(size = 46.dp, animate = true)

            Spacer(modifier = Modifier.width(10.dp))

            // Center: Dark Purple Rounded Capsule Pill with 5 Icons
            Surface(
                color = AuraPillContainer,
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                modifier = Modifier
                    .height(44.dp)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val navIcons = listOf(
                        Icons.Default.ChatBubble to "Chat",
                        Icons.Default.Mic to "Voice",
                        Icons.Default.ShoppingBag to "Hub",
                        Icons.Default.Restaurant to "Recipes",
                        Icons.Default.Explore to "Vibe"
                    )

                    navIcons.forEachIndexed { index, (icon, desc) ->
                        val isSelected = selectedTopNav == index
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) AuraPillSelected else Color.Transparent)
                                .clickable {
                                    selectedTopNav = index
                                    if (index == 1) {
                                        // Voice mode toggle
                                        isVoiceActive = !isVoiceActive
                                        if (isVoiceActive) {
                                            viewModel.speakText("Howzit! I'm Aura. Listening to your voice.")
                                        } else {
                                            viewModel.stopSpeaking()
                                        }
                                    } else {
                                        isVoiceActive = false
                                    }
                                }
                                .testTag("top_nav_pill_$index"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = desc,
                                tint = if (isSelected) Color.White else AuraTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right: Equalizer / Tune Settings Icon with purple indicator dot
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AuraPillContainer)
                    .border(1.dp, AuraBubbleBorder, CircleShape)
                    .clickable { onOpenSettings() }
                    .testTag("open_settings_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Settings",
                    tint = AuraTextPrimary,
                    modifier = Modifier.size(20.dp)
                )

                // Purple notification dot on top right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 6.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC084FC))
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Subheader: Aura Companion + English (South Africa) + Slang | Clear
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AuraOrbAvatar(size = 28.dp, animate = true, isSpeaking = isSpeaking)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Aura Companion",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextPrimary,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "English (South Africa) + Slang",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted,
                        fontSize = 12.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showClearDialog = true }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                    .testTag("clear_header_button")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Clear",
                    tint = AuraTextMuted,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Clear",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraTextMuted,
                    fontSize = 12.sp
                )
            }
        }

        // Thin Divider
        HorizontalDivider(
            color = AuraDivider,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        // Sub-screen rendering with smooth fluid transition
        Box(modifier = Modifier.weight(1f)) {
            AnimatedContent(
                targetState = selectedTopNav,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                },
                label = "subscreen_transition"
            ) { targetTab ->
                when (targetTab) {
                    1 -> VoicePulseScreen(viewModel = viewModel)
                    2 -> LoadsheddingScheduleScreen(viewModel = viewModel)
                    3 -> ComfortRecipesTab(viewModel = viewModel)
                    4 -> CuteSlangTab(viewModel = viewModel)
                    else -> PrimaryChatFeed(
                        messages = messages,
                        isThinking = isThinking,
                        listState = listState,
                        onSpeak = { viewModel.speakText(it) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Bottom Rounded Input Bar (Exact Screenshot)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(AuraBubbleBackground)
                .border(1.dp, AuraBubbleBorder, RoundedCornerShape(28.dp))
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                textStyle = TextStyle(
                    color = AuraTextPrimary,
                    fontSize = 15.sp
                ),
                cursorBrush = SolidColor(AuraSendButton),
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                decorationBox = { innerTextField ->
                    if (inputText.isEmpty()) {
                        Text(
                            text = "Hi",
                            style = TextStyle(
                                color = AuraTextMuted.copy(alpha = 0.8f),
                                fontSize = 15.sp
                            )
                        )
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Dark Purple Circular Microphone Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AuraMicButton)
                    .clickable {
                        // Quick audio prompt or speech trigger
                        val prompt = if (inputText.isNotBlank()) inputText else "Howzit Aura! Tell me a cute Mzansi joke or advice."
                        inputText = ""
                        viewModel.sendMessage(prompt)
                    }
                    .testTag("mic_input_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice input",
                    tint = AuraTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Vibrant Purple Circular Send Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AuraSendButton)
                    .clickable {
                        val text = inputText
                        if (text.isNotBlank()) {
                            inputText = ""
                            viewModel.sendMessage(text)
                        } else {
                            viewModel.sendMessage("Hi")
                        }
                    }
                    .testTag("send_message_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun PrimaryChatFeed(
    messages: List<ChatMessageEntity>,
    isThinking: Boolean,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            ScreenshotMessageBubble(
                message = message,
                onSpeak = { onSpeak(message.text) }
            )
        }

        if (isThinking) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AuraOrbAvatar(size = 24.dp, animate = true)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(AuraBubbleBackground)
                            .border(1.dp, AuraBubbleBorder, RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                strokeWidth = 2.dp,
                                color = AuraSendButton
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Aura is typing...",
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraTextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenshotMessageBubble(
    message: ChatMessageEntity,
    onSpeak: () -> Unit
) {
    val isUser = message.sender == "USER"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            // Glowing concentric orb icon next to Aura's bubble
            AuraOrbAvatar(size = 26.dp, animate = false)
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(if (isUser) 0.8f else 0.88f)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .background(if (isUser) AuraPillSelected else AuraBubbleBackground)
                .border(
                    width = 1.dp,
                    color = if (isUser) Color(0xFF9333EA) else AuraBubbleBorder,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .clickable { if (!isUser) onSpeak() }
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuraTextPrimary,
                    lineHeight = 22.sp,
                    fontSize = 14.sp
                )

                if (!isUser) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Read aloud",
                            tint = AuraTextMuted.copy(alpha = 0.6f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VoicePulseScreen(viewModel: AuraViewModel) {
    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Aura Voice Companion",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AuraTextPrimary
        )
        Text(
            text = "Speak in English, isiZulu, Sepedi, or Mzansi slang",
            style = MaterialTheme.typography.bodySmall,
            color = AuraTextMuted
        )

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier.size(130.dp),
            contentAlignment = Alignment.Center
        ) {
            AuraOrbAvatar(size = 110.dp, animate = true, isSpeaking = isSpeaking)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Silky undulating audio waveform
        AuraWaveformVisualizer(
            isLive = isSpeaking,
            height = 56.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            color = AuraBubbleBackground,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Tap the mic icon on the bottom bar to chat with Aura, or tap the speaker icon on any message to hear it spoken aloud.",
                style = MaterialTheme.typography.bodySmall,
                color = AuraTextMuted,
                modifier = Modifier.padding(14.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
