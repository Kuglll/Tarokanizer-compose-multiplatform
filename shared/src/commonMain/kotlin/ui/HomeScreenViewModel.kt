package ui

import data.Game
import GameDataSource
import HomeScreenState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val gameDataSource: GameDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())

    init {
        _state.update {
            it.copy(games = gameDataSource.getGames())
        }
    }

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), HomeScreenState())

    fun openAddGameScreen(){
        _state.update {
            it.copy(isAddGameScreenOpen = true)
        }
    }

    fun hideAddGameScreen(){
        _state.update {
            it.copy(isAddGameScreenOpen = false)
        }
    }

    fun addGame(game: Game) {
        viewModelScope.launch {
            gameDataSource.storeGame(Game(_state.value.games.size.toLong(), game.title, game.players))
            _state.update {
                it.copy(games = gameDataSource.getGames(), isAddGameScreenOpen = false)
            }
        }
    }

    fun deleteGame(id: Long){
        viewModelScope.launch {
            gameDataSource.deleteGameById(id)
            _state.update {
                it.copy(games = gameDataSource.getGames())
            }
        }
    }

    fun showGameDetails(id: Long){
        //TODO: Implement
    }

}