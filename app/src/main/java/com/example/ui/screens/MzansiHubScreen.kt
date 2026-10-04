package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.engine.LocalAuraEngine
import com.example.data.engine.ProverbItem
import com.example.data.engine.RecipeItem
import com.example.data.engine.SlangItem
import com.example.ui.components.AuraOrbAvatar
import com.example.ui.theme.AuraBubbleBackground
import com.example.ui.theme.AuraBubbleBorder
import com.example.ui.theme.AuraNightBackground
import com.example.ui.theme.AuraPillContainer
import com.example.ui.theme.AuraPillSelected
import com.example.ui.theme.AuraSendButton
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.viewmodel.AuraViewModel

@Composable
fun MzansiHubScreen(viewModel: AuraViewModel, modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Ubuntu Wisdom", "Load Shedding", "Mzansi Slang", "Local Recipes")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AuraNightBackground)
    ) {
        // Purple Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.aura_cute_banner_1791106634514),
                contentDescription = "Aura Mzansi banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                AuraNightBackground.copy(alpha = 0.95f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Mzansi Life & Culture",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AuraTextPrimary
                )
                Text(
                    text = "Ubuntu wisdom, loadshedding survival & authentic local recipes",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuraTextMuted
                )
            }
        }

        // Tab Row with purple styling
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp,
            containerColor = AuraBubbleBackground,
            contentColor = AuraSendButton,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = AuraSendButton
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) AuraTextPrimary else AuraTextMuted,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.testTag("hub_tab_$index")
                )
            }
        }

        when (selectedTab) {
            0 -> UbuntuWisdomTab(viewModel)
            1 -> LoadsheddingScheduleScreen(viewModel)
            2 -> CuteSlangTab(viewModel)
            3 -> ComfortRecipesTab(viewModel)
        }
    }
}

@Composable
fun UbuntuWisdomTab(viewModel: AuraViewModel) {
    val proverbs = remember { LocalAuraEngine.dailyProverbs }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AuraOrbAvatar(size = 36.dp, animate = true)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Spirit of Ubuntu",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary
                        )
                        Text(
                            text = "'Umuntu ngumuntu ngabantu' — A person is a person through other persons. Discover timeless wisdom from South Africa's languages.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextMuted
                        )
                    }
                }
            }
        }

        items(proverbs) { item ->
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
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = item.language,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row {
                            IconButton(
                                onClick = { viewModel.speakText("${item.originalText}. ${item.englishTranslation}") },
                                modifier = Modifier.testTag("speak_proverb_${item.language}")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Read aloud",
                                    tint = AuraTextPrimary
                                )
                            }
                            IconButton(
                                onClick = { viewModel.saveProverb(item) },
                                modifier = Modifier.testTag("save_proverb_${item.language}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = "Save proverb",
                                    tint = AuraTextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "\"${item.originalText}\"",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.englishTranslation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraSendButton,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.lifeMeaning,
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted,
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CozyLoadsheddingTab(viewModel: AuraViewModel) {
    val stage by viewModel.activeLoadsheddingStage.collectAsStateWithLifecycle()
    val checklist by viewModel.loadsheddingChecklist.collectAsStateWithLifecycle()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PowerOff,
                                contentDescription = "Loadshedding",
                                tint = Color(0xFFF87171),
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Load Shedding Stage Tracker",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AuraTextPrimary
                            )
                        }

                        Surface(
                            color = AuraPillSelected,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Stage $stage",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Select stage to review daily outage hours:",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items((1..8).toList()) { s ->
                            FilterChip(
                                selected = stage == s,
                                onClick = { viewModel.setLoadsheddingStage(s) },
                                label = { Text("Stage $s", fontSize = 12.sp) },
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

                    Spacer(modifier = Modifier.height(10.dp))
                    val hoursOut = stage * 2
                    Text(
                        text = "Stage $stage indicates approximately $hoursOut hours of scheduled rotational cuts per day.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        item {
            Text(
                text = "Blackout Survival Checklist",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AuraTextPrimary
            )
        }

        items(checklist.indices.toList()) { index ->
            val item = checklist[index]
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item.second,
                        onCheckedChange = { viewModel.toggleChecklistItem(index) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AuraSendButton,
                            uncheckedColor = AuraTextMuted
                        ),
                        modifier = Modifier.testTag("checklist_checkbox_$index")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.first,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (item.second) AuraTextMuted else AuraTextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun CuteSlangTab(viewModel: AuraViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedQuizOption by remember { mutableStateOf<Int?>(null) }

    val allSlang = remember { LocalAuraEngine.slangDictionary }
    val filteredSlang = remember(searchQuery) {
        if (searchQuery.isBlank()) allSlang
        else allSlang.filter {
            it.word.contains(searchQuery, ignoreCase = true) ||
            it.meaning.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Quick Slang Quiz Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = "Quiz",
                            tint = AuraSendButton
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mzansi Slang Check",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Question: If someone in SA says 'I am coming just now', when should you realistically expect them?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraTextMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    val options = listOf("Immediately right this second", "Later today, or whenever they get there!", "They already arrived")
                    options.forEachIndexed { index, option ->
                        Button(
                            onClick = { selectedQuizOption = index },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .testTag("quiz_option_$index"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedQuizOption == index) {
                                    if (index == 1) AuraSendButton else Color(0xFFDC2626)
                                } else Color(0xFF1B0E2E)
                            )
                        ) {
                            Text(
                                text = option,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }

                    if (selectedQuizOption != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (selectedQuizOption == 1) "Correct! In South Africa, 'Now-now' means immediately, while 'Just now' means later!" else "Incorrect! 'Just now' in SA means sometime later.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedQuizOption == 1) Color(0xFF34D399) else Color(0xFFF87171)
                        )
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search slang (e.g. 'Howzit', 'Lekker', 'Eish')", color = AuraTextMuted.copy(alpha = 0.6f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("slang_search_field"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuraSendButton,
                    unfocusedBorderColor = AuraBubbleBorder,
                    focusedTextColor = AuraTextPrimary,
                    unfocusedTextColor = AuraTextPrimary
                ),
                singleLine = true
            )
        }

        items(filteredSlang) { slang ->
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = slang.word,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AuraSendButton
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${slang.pronunciation})",
                                style = MaterialTheme.typography.bodySmall,
                                color = AuraTextMuted
                            )
                        }

                        Row {
                            IconButton(
                                onClick = { viewModel.speakText("${slang.word}. ${slang.meaning}. ${slang.exampleSentence}") },
                                modifier = Modifier.testTag("speak_slang_${slang.word}")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Speak",
                                    tint = AuraTextPrimary
                                )
                            }
                            IconButton(
                                onClick = { viewModel.saveSlang(slang) },
                                modifier = Modifier.testTag("save_slang_${slang.word}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = "Save",
                                    tint = AuraTextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = slang.meaning,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraTextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "\"${slang.exampleSentence}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
fun ComfortRecipesTab(viewModel: AuraViewModel) {
    val recipes = remember { LocalAuraEngine.localRecipes }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AuraBubbleBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuraBubbleBorder),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = "Food",
                        tint = AuraSendButton,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Mzansi Kitchen Classics",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary
                        )
                        Text(
                            text = "Authentic recipes from Durban Bunny Chow to Cape Malay Bobotie and Chakalaka.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextMuted
                        )
                    }
                }
            }
        }

        items(recipes) { recipe ->
            Card(
                shape = RoundedCornerShape(18.dp),
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
                        Column {
                            Text(
                                text = recipe.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AuraTextPrimary
                            )
                            Text(
                                text = "${recipe.region} • ${recipe.prepTime}",
                                style = MaterialTheme.typography.labelSmall,
                                color = AuraSendButton,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = { viewModel.saveRecipe(recipe) },
                            modifier = Modifier.testTag("save_recipe_${recipe.name.take(6)}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = "Save recipe",
                                tint = AuraTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraTextMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Ingredients:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextPrimary
                    )
                    recipe.ingredients.forEach { ing ->
                        Text(
                            text = "• $ing",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Preparation Steps:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextPrimary
                    )
                    recipe.steps.forEachIndexed { i, step ->
                        Text(
                            text = "${i + 1}. $step",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFF1B0E2E),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = recipe.auraTip,
                            style = MaterialTheme.typography.bodySmall,
                            color = AuraTextMuted,
                            modifier = Modifier.padding(12.dp),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}
