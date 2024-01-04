package ui.addgame

import GameDataSource
import data.Game
import data.Player
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch
import utils.generateRandomId

class AddGameViewModel(
    private val gameDataSource: GameDataSource,
) : ViewModel() {

    fun addGame(
        gameTitle: String,
        playerNames: List<String>,
    ) {
        val game = createNewGame(gameTitle, playerNames)

        viewModelScope.launch {
            gameDataSource.storeGame(Game(game.id, game.title, game.players))
        }
        //TODO: Trigger navigation back to HomeScreen
    }

    private fun createNewGame(
        gameTitle: String,
        playerNames: List<String>,
    ) = Game(
            id = generateRandomId(),
            title = gameTitle,
            players = playerNames.map {
                Player(it)
            }
        )

}
