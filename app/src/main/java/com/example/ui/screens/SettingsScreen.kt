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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AiTone
import com.example.ui.components.AuraOrbAvatar
import com.example.ui.theme.AuraBubbleBackground
import com.example.ui.theme.AuraBubbleBorder
import com.example.ui.theme.AuraNightBackground
import com.example.ui.theme.AuraPillSelected
import com.example.ui.theme.AuraSendButton
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.viewmodel.AuraViewModel

@Composable
fun SettingsScreen(viewModel: AuraViewModel, modifier: Modifier = Modifier) {
    val currentPersona by viewModel.currentPersona.collectAsStateWithLifecycle()
    val selectedProvince by viewModel.selectedProvince.collectAsStateWithLifecycle()
    val savedItems by viewModel.savedWisdom.collectAsStateWithLifecycle()
    val isLive = viewModel.isGeminiLive

    val provinces = listOf(
        "Gauteng", "Western Cape", "KwaZulu-Natal", "Eastern Cape",
        "Free State", "Limpopo", "Mpumalanga", "North West", "Northern Cape"
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(AuraNightBackground)
    ) {
        // AI Model & Status Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AuraOrbAvatar(size = 36.dp, animate = true)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isLive) "Gemini 3.5 Connected" else "Aura Local Mzansi Engine",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AuraTextPrimary
                                )
                                Text(
                                    text = if (isLive) "Real-time generative intelligence" else "Built-in offline South African engine active",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraTextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFF1B0E2E),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isLive) {
                                "Gemini is actively generating responses tuned to everyday South African culture, context, and languages."
                            } else {
                                "Aura is currently using the local Mzansi engine with loadshedding survival tools, slang, recipes, and proverbs. You can also inject your GEMINI_API_KEY in the AI Studio Secrets panel."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextMuted,
                            modifier = Modifier.padding(12.dp),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Persona Selection
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Persona",
                            tint = AuraSendButton
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Aura Companion Style",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val personas = listOf(
                        "MZANSI" to ("Everyday Mzansi" to "Natural blend of standard English with everyday SA slang (Howzit, Lekker, Eish)."),
                        "UBUNTU" to ("Ubuntu Focus" to "Thoughtful, community-minded, supportive, and grounded in Ubuntu principles."),
                        "KASI" to ("Kasi Smart" to "Energetic, streetwise, practical, and motivational."),
                        "LINGO" to ("Multilingual SA" to "Helps with phrases in isiZulu, Sepedi, Sesotho, Afrikaans, and more.")
                    )

                    personas.forEach { (key, pair) ->
                        Surface(
                            onClick = { viewModel.setPersona(key) },
                            shape = RoundedCornerShape(14.dp),
                            color = if (currentPersona == key) Color(0xFF26153E) else Color(0xFF1B0E2E),
                            border = if (currentPersona == key) androidx.compose.foundation.BorderStroke(1.5.dp, AuraSendButton) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag("select_persona_$key")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = pair.first,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentPersona == key) AuraSendButton else AuraTextPrimary
                                    )
                                    if (currentPersona == key) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = AuraSendButton,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = pair.second,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraTextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // Province Selection
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Province",
                            tint = AuraSendButton
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "South African Province",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Aura tailors local greetings and load shedding context for your province.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(provinces) { prov ->
                            FilterChip(
                                selected = selectedProvince == prov,
                                onClick = { viewModel.setProvince(prov) },
                                label = { Text(prov) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AuraPillSelected,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color(0xFF1B0E2E),
                                    labelColor = AuraTextMuted
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.testTag("province_chip_$prov")
                            )
                        }
                    }
                }
            }
        }

        // Saved Bookmarks
        item {
            Text(
                text = "Saved Wisdom & Recipes (${savedItems.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AuraTextPrimary
            )
        }

        if (savedItems.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No saved items yet. Tap the bookmark icon on any recipe or proverb in the Hub to save it here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(savedItems, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AuraTextPrimary
                            )
                            IconButton(
                                onClick = { viewModel.deleteSavedWisdom(item.id) },
                                modifier = Modifier.size(32.dp).testTag("delete_saved_${item.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete",
                                    tint = AuraTextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        if (item.subtitle.isNotBlank()) {
                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraSendButton,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.content,
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextMuted
                        )
                    }
                }
            }
        }

        // About Aura
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "About Aura • Mzansi AI Companion",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AuraSendButton
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Aura is built for all South Africans — normal, relatable, and designed with authentic Mzansi vibes. Whether you need load shedding tips, local recipe guides, everyday chat, or multilingual phrases, Aura is here for everyone.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
