package ui.gamedetails

import data.Player
import data.Round

data class GameDetailsState(
    val title: String = "",
    val players: List<Player> = listOf(),
    val rounds: List<Round> = listOf(),
    val sums: List<Int> = listOf(),
)
