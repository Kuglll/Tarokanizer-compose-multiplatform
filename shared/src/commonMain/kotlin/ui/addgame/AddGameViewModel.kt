package ui.addgame

import GameDataSource
import data.Game
import data.Player
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.generateRandomId

class AddGameViewModel(
    private val gameDataSource: GameDataSource,
) : ViewModel() {

    private val _state = MutableStateFlow(AddGameState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), AddGameState())

    fun addGame(
        gameTitle: String,
        playerNames: List<String>,
    ) {
        val game = createNewGame(gameTitle, playerNames)

        viewModelScope.launch {
            //TODO: Check how to handle errors in compose multiplatform
            gameDataSource.storeGame(Game(game.id, game.title, game.players))
            _state.update {
                it.copy(isGameAdded = true)
            }
        }
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
