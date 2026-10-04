package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AuraOrbAvatar
import com.example.ui.theme.AuraBubbleBackground
import com.example.ui.theme.AuraBubbleBorder
import com.example.ui.theme.AuraNightBackground
import com.example.ui.theme.AuraPillSelected
import com.example.ui.theme.AuraSendButton
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.viewmodel.AuraViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VibeScreen(viewModel: AuraViewModel, modifier: Modifier = Modifier) {
    val vibeHistory by viewModel.vibeHistory.collectAsStateWithLifecycle()
    var selectedVibe by remember { mutableStateOf("LEKKER") }
    var journalNote by remember { mutableStateOf("") }
    val dateFormatter = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }

    val vibesList = listOf(
        "LEKKER" to "Lekker (Great)",
        "SHARP" to "Sharp (All good)",
        "UBUNTU" to "Ubuntu (Grounded)",
        "Eish" to "Eish (Rough day)",
        "LOADSHEDDING" to "Loadshedding blues"
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(AuraNightBackground)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AuraOrbAvatar(size = 36.dp, animate = true)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "How's Your Vibe Today?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AuraTextPrimary
                            )
                            Text(
                                text = "Daily check-in for Mzansi life and mental space",
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Current status:",
                        style = MaterialTheme.typography.labelMedium,
                        color = AuraTextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(vibesList) { (key, label) ->
                            FilterChip(
                                selected = selectedVibe == key,
                                onClick = { selectedVibe = key },
                                label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AuraPillSelected,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color(0xFF1B0E2E),
                                    labelColor = AuraTextMuted
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.testTag("vibe_chip_$key")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = journalNote,
                        onValueChange = { journalNote = it },
                        placeholder = { Text("What happened today? (Optional note...)", color = AuraTextMuted.copy(alpha = 0.6f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("vibe_note_field"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AuraSendButton,
                            unfocusedBorderColor = AuraBubbleBorder,
                            focusedTextColor = AuraTextPrimary,
                            unfocusedTextColor = AuraTextPrimary
                        ),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.logVibe(selectedVibe, journalNote.trim())
                            journalNote = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_vibe_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AuraSendButton)
                    ) {
                        Text("Log Vibe & Get Aura's Reflection", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        item {
            Text(
                text = "Vibe History (${vibeHistory.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AuraTextPrimary
            )
        }

        if (vibeHistory.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No vibe check-ins logged yet.", color = AuraTextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Log how you are feeling to track your daily rhythm with Aura.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(vibeHistory, key = { it.id }) { vibe ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = AuraPillSelected,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = vibe.vibeType,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    color = Color.White
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = dateFormatter.format(Date(vibe.timestamp)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AuraTextMuted
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { viewModel.speakText(vibe.auraResponse) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Speak",
                                        tint = AuraTextPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.deleteVibe(vibe.id) },
                                    modifier = Modifier.size(32.dp).testTag("delete_vibe_${vibe.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete",
                                        tint = AuraTextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        if (vibe.userNote.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "\"${vibe.userNote}\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AuraTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = Color(0xFF1B0E2E),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("Aura: ", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AuraSendButton)
                                Text(
                                    text = vibe.auraResponse,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraTextMuted,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
