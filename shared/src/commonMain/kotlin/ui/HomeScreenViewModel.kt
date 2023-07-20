package ui

import Game
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

    fun addGame(gameTitle: String) {
        viewModelScope.launch {
            gameDataSource.storeGame(Game(_state.value.games.size.toLong(), gameTitle))
            _state.update {
                it.copy(games = gameDataSource.getGames(), isAddGameScreenOpen = false)
            }
        }
    }

}