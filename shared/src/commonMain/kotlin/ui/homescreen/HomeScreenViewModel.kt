package ui.homescreen

import GameDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val gameDataSource: GameDataSource,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), HomeScreenState())

    init {
        viewModelScope.launch {
            gameDataSource.getGames().collect { games ->
                _state.update {
                    it.copy(games = games)
                }
            }
        }
    }

    fun deleteGame(id: String){
        viewModelScope.launch {
            gameDataSource.deleteGameById(id)
        }
    }

}
