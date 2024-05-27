package com.example.gaminghealthtracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    var state: Int by mutableStateOf(0)
    var currentStep by mutableStateOf("")
    var players by mutableIntStateOf(1)
    val listOfGames = listOf("magic", "dungeon")
    var currentGameType by mutableStateOf("")
    var startingHealth: Int = 20
    var health by mutableIntStateOf( 20)
    var poisonCounters by mutableIntStateOf(0)
    var temporaryHealth by mutableIntStateOf(0)

    fun selectGameType(gameTypeSelected: String) {
        currentGameType = gameTypeSelected
    }

    fun setPlayerStartingHealth(healthValueToSet: Int) {
        startingHealth = healthValueToSet
        health = healthValueToSet
    }
}