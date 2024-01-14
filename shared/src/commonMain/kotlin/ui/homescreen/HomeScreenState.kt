package ui.homescreen

import data.Game

data class HomeScreenState(
    val games: List<Game> = emptyList(),
)
