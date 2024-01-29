package ui.addround

import data.Player

data class AddRoundState(
    val players: List<Player> = listOf(),
    val isRoundAdded: Boolean = false,
)
