package ui.gamedetails

import GameDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameDetailsViewModel(
    private val gameDataSource: GameDataSource,
    private val id: String,
) : ViewModel() {

    private val _state = MutableStateFlow(GameDetailsState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), GameDetailsState())

    init {
        viewModelScope.launch {
            gameDataSource.getPlayersByGameId(id).collect { players ->
                _state.update {
                    it.copy(players = players)
                }
            }
        }

        viewModelScope.launch {
            _state.update {
                it.copy(title = gameDataSource.getGameTitleById(id))
            }
        }


    }

}
