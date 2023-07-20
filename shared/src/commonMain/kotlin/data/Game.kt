package data

data class Game(
    val id: Long,
    val title: String,
    val players: List<Player>
)