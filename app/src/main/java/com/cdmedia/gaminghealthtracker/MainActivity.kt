package com.cdmedia.gaminghealthtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon

import androidx.compose.material3.Tab
import androidx.compose.material3.Scaffold

import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cdmedia.gaminghealthtracker.ui.theme.GamingHealthTrackerTheme

val viewModel = GameViewModel()
val numbersOnly = Regex("^\\d+?")

fun checkDigitsOnly(stringToCheck: String): Boolean {
    return stringToCheck.matches(numbersOnly)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamingHealthTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        Modifier
                            .padding(12.dp)
                            .padding(top = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween) {
                        TabRow(selectedTabIndex = viewModel.state,
                            Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Tab(
                                selected = viewModel.state == 0,
                                onClick = { viewModel.state = 0 }
                            ) {
                                MediumPaddingText(stringId = R.string.setup_tab)
                            }
                            Tab(
                                selected = viewModel.state == 1,
                                onClick = { viewModel.state = 1 }
                            ) {
                                MediumPaddingText(stringId = R.string.game_tab)
                            }
                        }
                        Column() {
                            if (viewModel.state == 0) {
                                SelectGameType()

                            } else if(viewModel.state == 1) {
                                    Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                        when (viewModel.currentGameType) {
                                            "magic" -> {
                                                MagicGameLayout()
                                            }
                                            "dungeon" -> {
                                                DungeonGameLayout()
                                            }
                                            else -> {
                                                Text(text = stringResource(id = R.string.no_game), fontSize = 24.sp, modifier = Modifier
                                                    .padding(8.dp)
                                                    .align(Alignment.CenterHorizontally))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


@Composable
fun MagicGameLayout() {
    LazyColumn {
        for(i in 1..viewModel.players){
            item {
                val player by remember { mutableStateOf(Player(name = "Player $i", startingHealth = viewModel.startingPlayerHealth)) }
                Row {
                    CustomTextWithSizePaddingAlignment(
                        textToDisplay = player.playerName,
                        sizeOfFont = 24.sp,
                        paddingToUse =8.dp
                    )
                }
                CustomTextWithSizePaddingAlignment(
                    textToDisplay = player.health.toString(),
                    sizeOfFont = 24.sp,
                    paddingToUse = 8.dp
                )
                Row {
                    Button(onClick = { player.addFiveHealth() } ) {
                        Text(text = "+5")
                    }
                    Button(onClick = { player.addOneHealth() } ) {
                        Text(text = "+1")
                    }
                    Button(onClick = { player.removeOneHealth() } ) {
                        Text(text = "-1")
                    }
                    Button(onClick = { player.removeFiveHealth() } ) {
                        Text(text = "-5")
                    }
                }
                CustomTextWithSizePaddingAlignment(
                    textToDisplay = "Poison",
                    sizeOfFont = 20.sp,
                    paddingToUse = 6.dp
                )
                Row {
                    Button(onClick = { player.poisonCounters++ }, Modifier.padding(4.dp)) {
                        Icon(Icons.Filled.Add, "Add Counter")
                    }
                    Button(onClick = { player.poisonCounters = 0 }, Modifier.padding(4.dp)) {
                        Icon(Icons.Filled.Delete, "Remove Counters")
                    }
                    if(player.poisonCounters >=6) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Poison Counter")
                        Text(text = "x${player.poisonCounters}")
                    } else {
                        for (counterNumber in 1..player.poisonCounters) {
                            Icon(Icons.Default.FavoriteBorder, contentDescription = "Poison Counter $counterNumber")
                        }
                    }
                }
                Button(onClick = { player.reset() }) {
                    Text(text = stringResource(id = R.string.reset))
                }
                Divider(Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun DungeonGameLayout() {
    val player by remember { mutableStateOf(Player(name = viewModel.playerName, startingHealth = viewModel.startingPlayerHealth, startingArmorClass = viewModel.armorClass, startingMovement = viewModel.movement)) }
    Column {
        CustomTextWithSizePaddingAlignment(player.playerName, 24.sp, 8.dp)
        Row(
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(6.dp)) {
            DndStatCard(player.health, "HP")
            Spacer(modifier = Modifier.padding(4.dp))
            DndStatCard(player.armorClass, "AC")
            Spacer(modifier = Modifier.padding(4.dp))
            DndStatCard(player.movement, "ft.")
        }
        Row(
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(6.dp)) {
            Button(onClick = { player.addFiveHealth() } ) {
                Text(text = "+5")
            }
            Button(onClick = { player.addOneHealth() } ) {
                Text(text = "+1")
            }
            Button(onClick = { player.removeOneHealth() } ) {
                Text(text = "-1")
            }
            Button(onClick = { player.removeFiveHealth() } ) {
                Text(text = "-5")
            }
        }
        Divider(Modifier.padding(4.dp))
        Row(Modifier
            .align(Alignment.CenterHorizontally)
            .padding(6.dp)) {
            DndStatCard(player.temporaryHealth, "Temp HP")
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Row(Modifier
            .align(Alignment.CenterHorizontally)
            .padding(6.dp)) {
            Button(onClick = { player.addFiveTempHealth() } ) {
                Text(text = "+5")
            }
            Button(onClick = { player.addOneTempHealth() } ) {
                Text(text = "+1")
            }
            Button(onClick = { player.removeOneTempHealth() } ) {
                Text(text = "-1")
            }
            Button(onClick = { player.removeFiveTempHealth() } ) {
                Text(text = "-5")
            }
        }
    }
}

@Composable
fun SelectGameType() {
    val viewModel = GameViewModel()
    LazyColumn {
        for (game in viewModel.listOfGames) {
                when (game) {
                    "magic" -> {
                        item {
                            MediumPaddingText(R.string.magic)
                            MagicTheGathering()
                        }
                    }
                    "dungeon" -> {
                        item {
                            MediumPaddingText(R.string.dungeons)
                            DungeonAndDragonTracker()
                        }
                    }
                    else -> {
                        item {
                            MediumPaddingText(R.string.in_progress)
                        }
                    }
                }
                item { Divider(Modifier.padding(4.dp)) }
            }
        }
    }

@Composable
fun DungeonAndDragonTracker() {
    var playerName by rememberSaveable { mutableStateOf("") }
    TextField(
        value = playerName,
        onValueChange = { playerName = it },
        label = {SmallPaddingText(R.string.player_name)},
        maxLines = 1,
        singleLine = true,
    )
    var healthInput by rememberSaveable { mutableStateOf("0") }
    TextField(
        value = healthInput,
        onValueChange = { if(checkDigitsOnly(it)){healthInput = it} },
        label = {SmallPaddingText(R.string.starting_health)},
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    var armorClass by rememberSaveable { mutableStateOf("10") }
    TextField(
        value = armorClass,
        onValueChange = { if(checkDigitsOnly(it)){armorClass = it} },
        label = {SmallPaddingText(R.string.armor_class)},
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    var movementSpeed by rememberSaveable { mutableStateOf("30") }
    TextField(
        value = movementSpeed,
        onValueChange = { if(checkDigitsOnly(it)){movementSpeed = it} },
        label = { SmallPaddingText(R.string.movement) },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    Button(
        onClick = {
            viewModel.state = 1
            viewModel.currentGameType = "dungeon"
            viewModel.playerName = playerName
            viewModel.startingPlayerHealth = healthInput.toInt()
            viewModel.armorClass = armorClass.toInt()
            viewModel.movement = movementSpeed.toInt()
        },
        Modifier.padding(4.dp)
    ) {
        SmallPaddingText(R.string.submit)
    }
}

@Composable
fun MagicTheGathering() {
    //number of players
    var playerInput by rememberSaveable { mutableIntStateOf(1) }
    Column {
        SmallPaddingText(R.string.player_count)
        Row {
            Column {
                Button(onClick = {playerInput++}){ Icon(Icons.Filled.Add, "Increase Player Count")}
                Button(onClick = {playerInput = 1}){Icon(Icons.Filled.Refresh, "Reset Player Count")}
            }
            Spacer(modifier = Modifier.size(10.dp))
            Card {
                Text(text = playerInput.toString(), fontSize = 24.sp, modifier = Modifier.padding(8.dp))
            }
        }
    }

    var healthInput by rememberSaveable { mutableStateOf("0") }
    TextField(
        value = healthInput,
        onValueChange = { if(checkDigitsOnly(it)){healthInput = it}},
        label = { SmallPaddingText(R.string.starting_health)},
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

    )
    Button(
        onClick = {
            viewModel.state = 1
            viewModel.currentGameType = "magic"
            viewModel.players = playerInput
            viewModel.startingPlayerHealth = healthInput.toInt()
        },
        Modifier.padding(4.dp)
    ) {
        SmallPaddingText(R.string.submit)
    }
}

@Composable
fun DndStatCard(displayNumber: Int, title: String) {
    Card {
        CustomTextWithSizePaddingAlignment(displayNumber.toString(), 24.sp, 8.dp)
        CustomTextWithSizePaddingAlignment(title, 16.sp, 6.dp)
    }
}

@Composable
fun MediumPaddingText(stringId: Int) {
    Text(text = stringResource(id = stringId),
        Modifier.padding(10.dp))
}

@Composable
fun SmallPaddingText(stringId: Int) {
    Text(text = stringResource(id = stringId),
        Modifier.padding(8.dp))
}

@Composable
fun CustomTextWithSizePaddingAlignment(textToDisplay: String, sizeOfFont: TextUnit, paddingToUse: Dp) {
    Text(
        text = textToDisplay,
        fontSize = sizeOfFont,
        modifier = Modifier.padding(paddingToUse)
    )
}