import data.Game

data class HomeScreenState(
    val games: List<Game> = emptyList(),
    val isAddGameScreenOpen: Boolean = false,
)