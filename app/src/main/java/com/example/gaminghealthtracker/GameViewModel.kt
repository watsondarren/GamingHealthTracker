package com.example.gaminghealthtracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    var state: Int by mutableIntStateOf(0)
    var players by mutableIntStateOf(1)
    val listOfGames = listOf("magic", "dungeon")
    var currentGameType by mutableStateOf("")
    var playerName by mutableStateOf("")
    var startingPlayerHealth by mutableIntStateOf(0)
    var temporaryHealth by mutableIntStateOf(0)
    var armorClass by mutableIntStateOf(10)
    var movement by mutableIntStateOf(30)
}

class Player(name: String = "", startingHealth: Int = 0, startingArmorClass: Int = 10, startingMovement: Int = 30) {
    var playerName by mutableStateOf(name)
    var startingPlayerHealth by mutableIntStateOf(startingHealth)
    var health by mutableIntStateOf( startingHealth)
    var poisonCounters by mutableIntStateOf(0)
    var temporaryHealth by mutableIntStateOf(0)
    var armorClass by mutableIntStateOf(startingArmorClass)
    var movement by mutableIntStateOf(startingMovement)
    fun addOneHealth() {
        health++
    }
    fun addFiveHealth() {
        health+=5
    }
    fun removeOneHealth() {
        health--
    }
    fun removeFiveHealth() {
        health-=5
    }
    fun reset() {
        health = startingPlayerHealth
        poisonCounters = 0
    }
}