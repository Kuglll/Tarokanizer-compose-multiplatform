package ui.addgame

import GameDataSource
import data.Game
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch

class AddGameViewModel(
    private val gameDataSource: GameDataSource,
) : ViewModel() {

    fun addGame(
        game: Game,
        gameId: Long,
    ) {
        viewModelScope.launch {
            gameDataSource.storeGame(Game(gameId, game.title, game.players))
        }
        //TODO: Trigger navigation back to HomeScreen
    }

}
