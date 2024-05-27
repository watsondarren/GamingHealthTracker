package com.example.gaminghealthtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.Button
import androidx.compose.material3.Card

import androidx.compose.material3.Tab
import androidx.compose.material3.Scaffold

import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gaminghealthtracker.ui.theme.GamingHealthTrackerTheme

val viewModel = GameViewModel()
val numbersOnly = Regex("^\\d+\$")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamingHealthTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        Modifier
                            .padding(16.dp)
                            .padding(top = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween) {
                        TabRow(selectedTabIndex = viewModel.state,
                            Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Tab(
                                selected = viewModel.state == 0,
                                onClick = { viewModel.state = 0 },
                                Modifier.padding(10.dp)
                            ) {
                                Text(text = "Setup")
                            }
                            Tab(
                                selected = viewModel.state == 1,
                                onClick = { viewModel.state = 1 },
                                Modifier.padding(10.dp)
                            ) {
                                Text(text = "Game")
                            }
                        }
                        if (viewModel.state == 0) {
                            SelectGameType()
                        } else if(viewModel.state == 1) {
                            Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                if(viewModel.currentGameType == "magic") {
                                    Text(text = viewModel.health.toString(), fontSize = 30.sp, modifier = Modifier
                                        .padding(8.dp)
                                        .align(Alignment.CenterHorizontally))
                                    Column {
                                        PlayerControls()
                                        ResetHealth()
                                    }
                                } else {
                                    Text(text = stringResource(id = R.string.no_game), fontSize = 30.sp, modifier = Modifier
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

@Composable
fun PlayerControls() {
    Row {
        AddFiveHealth()
        AddOneHealth()
        RemoveOneHealth()
        RemoveFiveHealth()
    }
}

@Composable
fun Health() {
}

@Composable
fun AddOneHealth() {
    Button(onClick = { viewModel.health++ } ) {
        Text(text = "+1")
    }
}
@Composable
fun RemoveOneHealth() {
    Button(onClick = { viewModel.health-- } ) {
        Text(text = "-1")
    }
}
@Composable
fun RemoveFiveHealth() {
    Button(onClick = { viewModel.health -= 5 } ) {
        Text(text = "-5")
    }
}
@Composable
fun AddFiveHealth() {
    Button(onClick = { viewModel.health += 5 } ) {
        Text(text = "+5")
    }
}

@Composable
fun ResetHealth() {
    Button(onClick = { viewModel.health = viewModel.startingHealth }) {
        Text(text = stringResource(id = R.string.reset))
    }
}

@Composable
fun SelectGameType() {
    val viewModel = GameViewModel()
    Column {
        for (game in viewModel.listOfGames) {
            if (game == "magic") {
                Card {
                    Text(text = stringResource(id = R.string.magic), modifier = Modifier.padding(8.dp))
                }
                MagicTheGathering()
            } else if(game == "dungeon") {
                Card {
                    Text(text = stringResource(id = R.string.dungeons), modifier = Modifier.padding(8.dp))
                }
                DungeonAndDragonTracker()
            } else {
                Card {
                    Text(text = stringResource(id = R.string.in_progress), modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun DungeonAndDragonTracker() {
    // Starting Health
    // Temp Health
    // Spell Slots
}

@Composable
fun MagicTheGathering() {
    //number of players
    var playerInput by rememberSaveable { mutableStateOf("0") }
    TextField(
        value = playerInput,
        onValueChange = { playerInput = it },
        label = {Text(text = stringResource(id = R.string.player_count))},
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    //starting health
    var healthInput by rememberSaveable { mutableStateOf("0") }
    TextField(
        value = healthInput,
        onValueChange = { healthInput = it},
        label = { Text(text = stringResource(id = R.string.starting_health))},
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

    )

    // Poison Counters
    Button(onClick = { viewModel.startingHealth = healthInput.toInt(); viewModel.health = healthInput.toInt(); viewModel.players = playerInput.toInt(); viewModel.state = 1; viewModel.currentGameType = "magic"}) {
        Text(text = stringResource(id = R.string.submit))
    }
}






