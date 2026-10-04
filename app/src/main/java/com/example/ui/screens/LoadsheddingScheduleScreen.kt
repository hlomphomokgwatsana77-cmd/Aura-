package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.engine.LoadsheddingLookupEngine
import com.example.data.model.LoadsheddingArea
import com.example.ui.components.AuraOrbAvatar
import com.example.ui.components.AuraWaveformVisualizer
import com.example.ui.theme.AuraBubbleBackground
import com.example.ui.theme.AuraBubbleBorder
import com.example.ui.theme.AuraNightBackground
import com.example.ui.theme.AuraPillContainer
import com.example.ui.theme.AuraPillSelected
import com.example.ui.theme.AuraSendButton
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.viewmodel.AuraViewModel
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun LoadsheddingScheduleScreen(
    viewModel: AuraViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val stage by viewModel.activeLoadsheddingStage.collectAsStateWithLifecycle()
    val selectedArea by viewModel.selectedArea.collectAsStateWithLifecycle()
    val bookmarkedIds by viewModel.bookmarkedAreaIds.collectAsStateWithLifecycle()
    val checklist by viewModel.loadsheddingChecklist.collectAsStateWithLifecycle()
    val isAlertEnabled by viewModel.is30MinAlertEnabled.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedProvinceFilter by remember { mutableStateOf("All") }
    var showAreaPicker by remember { mutableStateOf(false) }

    // Live second-by-second ticker for lifelike countdown
    var currentSecondsOfDay by remember {
        val cal = Calendar.getInstance()
        mutableIntStateOf(cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND))
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            val cal = Calendar.getInstance()
            currentSecondsOfDay = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND)
        }
    }

    // Permission launcher for Android 13+ (POST_NOTIFICATIONS)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.toggle30MinAlert(true)
        }
    }

    val status = remember(selectedArea, stage, currentSecondsOfDay / 60) {
        viewModel.getAreaStatus(selectedArea, stage)
    }

    // Calculate lifelike remaining countdown
    val countdownText = remember(status, currentSecondsOfDay) {
        val nextSlot = status.currentSlot ?: status.nextSlot
        if (nextSlot == null) {
            "Grid Normal • No immediate outages"
        } else {
            val targetTime = if (status.isPowerOffNow) nextSlot.endTime else nextSlot.startTime
            val parts = targetTime.split(":")
            val targetSecs = (parts.getOrNull(0)?.toIntOrNull() ?: 0) * 3600 +
                    (parts.getOrNull(1)?.toIntOrNull() ?: 0) * 60

            val diff = if (targetSecs >= currentSecondsOfDay) {
                targetSecs - currentSecondsOfDay
            } else {
                (24 * 3600 - currentSecondsOfDay) + targetSecs
            }

            val hours = diff / 3600
            val minutes = (diff % 3600) / 60
            val seconds = diff % 60

            val formatted = String.format("%02dh %02dm %02ds", hours, minutes, seconds)
            if (status.isPowerOffNow) {
                "Power returns in: $formatted"
            } else {
                "Next outage in: $formatted"
            }
        }
    }

    val filteredAreas = remember(searchQuery, selectedProvinceFilter, bookmarkedIds) {
        var list = LoadsheddingLookupEngine.allAreas
        if (selectedProvinceFilter == "Favorites ★") {
            list = list.filter { bookmarkedIds.contains(it.id) }
        } else if (selectedProvinceFilter != "All") {
            list = list.filter { it.province.equals(selectedProvinceFilter, ignoreCase = true) }
        }
        if (searchQuery.isNotBlank()) {
            val q = searchQuery.trim().lowercase()
            list = list.filter {
                it.suburb.lowercase().contains(q) ||
                it.municipality.lowercase().contains(q) ||
                it.province.lowercase().contains(q)
            }
        }
        list
    }

    // Smooth pulsing border animation for active power card
    val infiniteTransition = rememberInfiniteTransition(label = "border_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(AuraNightBackground)
    ) {
        // Top Hero Header
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AuraOrbAvatar(size = 40.dp, animate = true)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Load Shedding Live",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = AuraTextPrimary
                                )
                                Text(
                                    text = "Real-time schedule & 30-min advance alerts",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraTextMuted
                                )
                            }
                        }

                        Surface(
                            color = if (stage > 0) Color(0xFF6B21A8) else Color(0xFF065F46),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (stage > 0) "Stage $stage Active" else "Grid Normal",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "National Eskom Stage:",
                        style = MaterialTheme.typography.labelMedium,
                        color = AuraTextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Stage Selector Chips (0 to 6)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val stages = listOf(0, 1, 2, 3, 4, 6)
                        items(stages) { s ->
                            val isSelected = stage == s
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setLoadsheddingStage(s) },
                                label = { Text(if (s == 0) "Normal (0)" else "Stage $s", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AuraPillSelected,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color(0xFF1B0E2E),
                                    labelColor = AuraTextMuted
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.testTag("stage_chip_$s")
                            )
                        }
                    }
                }
            }
        }

        // Selected Suburb Status Card (Hero Feature with Lifelike Ticking Countdown)
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (status.isPowerOffNow) Color(0xFFEF4444).copy(alpha = glowAlpha)
                    else AuraSendButton.copy(alpha = glowAlpha)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = AuraSendButton,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = selectedArea.suburb,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AuraTextPrimary
                                )
                            }
                            Text(
                                text = "${selectedArea.municipality} • Block ${selectedArea.blockNumber}",
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraTextMuted
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.toggleBookmarkArea(selectedArea.id) },
                                modifier = Modifier.size(36.dp).testTag("bookmark_area_button")
                            ) {
                                val isBookmarked = bookmarkedIds.contains(selectedArea.id)
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (isBookmarked) AuraSendButton else AuraTextMuted
                                )
                            }

                            Surface(
                                color = AuraPillContainer,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.clickable { showAreaPicker = !showAreaPicker }
                            ) {
                                Text(
                                    text = if (showAreaPicker) "Close" else "Change",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AuraSendButton,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Ticking Status Banner with Pulsing Icon
                    val bannerBg = if (status.isPowerOffNow) Color(0xFF3F0B13) else Color(0xFF1B0E2E)
                    val bannerBorder = if (status.isPowerOffNow) Color(0xFF7F1D1D) else AuraBubbleBorder
                    val iconTint = if (status.isPowerOffNow) Color(0xFFF87171) else Color(0xFF34D399)

                    Surface(
                        color = bannerBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, bannerBorder),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (status.isPowerOffNow) Icons.Default.PowerOff else Icons.Default.ElectricBolt,
                                contentDescription = "Status",
                                tint = iconTint,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (status.isPowerOffNow) "⚡ Power is Currently OFF" else "💡 Power is ON",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (status.isPowerOffNow) Color(0xFFFCA5A5) else Color(0xFF6EE7B7)
                                )
                                Text(
                                    text = countdownText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AuraTextPrimary,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = status.formattedStatus,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraTextMuted,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Scheduled Slots for Selected Area
                    val activeSlots = selectedArea.scheduleByStage[stage] ?: emptyList()

                    Text(
                        text = "Scheduled Outage Slots (Stage $stage):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (activeSlots.isEmpty()) {
                        Text(
                            text = "No scheduled outages for ${selectedArea.suburb} in Stage $stage.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextMuted
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            activeSlots.forEach { slot ->
                                Surface(
                                    color = Color(0xFF160D27),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E174D)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = "Time",
                                                tint = AuraSendButton,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "${slot.day}:  ${slot.startTime} – ${slot.endTime}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                color = AuraTextPrimary
                                            )
                                        }

                                        Surface(
                                            color = AuraPillContainer,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "Stage ${slot.stageRequired}+",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = AuraTextMuted,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Background 30-Minute Outage Alerts Feature Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF26153E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isAlertEnabled) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = if (isAlertEnabled) AuraSendButton else AuraTextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "30-Min Outage Alerts",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AuraTextPrimary
                                )
                                Text(
                                    text = "Wakeup notifications before power cuts",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraTextMuted
                                )
                            }
                        }

                        Switch(
                            checked = isAlertEnabled,
                            onCheckedChange = { checked ->
                                if (checked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val hasPermission = ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) == PackageManager.PERMISSION_GRANTED
                                    if (!hasPermission) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        viewModel.toggle30MinAlert(true)
                                    }
                                } else {
                                    viewModel.toggle30MinAlert(checked)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AuraSendButton,
                                uncheckedThumbColor = AuraTextMuted,
                                uncheckedTrackColor = Color(0xFF1B0E2E)
                            ),
                            modifier = Modifier.testTag("toggle_30min_alert_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Aura automatically schedules a background alert 30 minutes before any scheduled power cut in ${selectedArea.suburb} so you have time to charge batteries, boil water, and protect electronics.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (isAlertEnabled) Color(0xFF1E1038) else Color(0xFF1A1322),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (isAlertEnabled) "● Active for ${selectedArea.suburb}" else "○ Alerts Disabled",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isAlertEnabled) Color(0xFFC084FC) else AuraTextMuted,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val hasPerm = ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) == PackageManager.PERMISSION_GRANTED
                                    if (!hasPerm) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        viewModel.triggerTestNotification()
                                    }
                                } else {
                                    viewModel.triggerTestNotification()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E174D)),
                            modifier = Modifier.testTag("test_notification_button")
                        ) {
                            Text("Test Alert Now", color = AuraSendButton, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Suburb Directory & Switcher Sheet
        item {
            AnimatedVisibility(visible = showAreaPicker) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Select Suburb or Area",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Suburb search field
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search suburb, metro, or block...", color = AuraTextMuted.copy(alpha = 0.6f)) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = AuraTextMuted)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = AuraTextMuted)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("suburb_search_field"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AuraSendButton,
                                unfocusedBorderColor = AuraBubbleBorder,
                                focusedTextColor = AuraTextPrimary,
                                unfocusedTextColor = AuraTextPrimary
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick Province Chips
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val filterOptions = listOf("All", "Gauteng", "Western Cape", "KwaZulu-Natal", "Favorites ★")
                            items(filterOptions) { filter ->
                                FilterChip(
                                    selected = selectedProvinceFilter == filter,
                                    onClick = { selectedProvinceFilter = filter },
                                    label = { Text(filter, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AuraPillSelected,
                                        selectedLabelColor = Color.White,
                                        containerColor = Color(0xFF1B0E2E),
                                        labelColor = AuraTextMuted
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Suburb Results
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            filteredAreas.forEach { area ->
                                val isCurrent = area.id == selectedArea.id
                                Surface(
                                    onClick = {
                                        viewModel.selectArea(area)
                                        showAreaPicker = false
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isCurrent) Color(0xFF2A1544) else Color(0xFF1B0E2E),
                                    border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.dp, AuraSendButton) else null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("area_item_${area.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = area.suburb,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCurrent) AuraSendButton else AuraTextPrimary
                                            )
                                            Text(
                                                text = "${area.municipality} • Block ${area.blockNumber} (${area.province})",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = AuraTextMuted
                                            )
                                        }

                                        if (isCurrent) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Selected",
                                                tint = AuraSendButton,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Outage Preparation Checklist
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Outage Readiness Checklist",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextPrimary
                    )
                    Text(
                        text = "Simple routines before the lights turn off",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    checklist.forEachIndexed { index, (label, checked) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { viewModel.toggleChecklistItem(index) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AuraSendButton,
                                    uncheckedColor = AuraTextMuted
                                ),
                                modifier = Modifier.testTag("schedule_checklist_$index")
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (checked) AuraTextMuted else AuraTextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
