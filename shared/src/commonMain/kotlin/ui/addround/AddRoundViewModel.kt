package ui.addround

import GameDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddRoundViewModel(
    private val gameDataSource: GameDataSource,
    private val gameId: String,
) : ViewModel() {

    private val _state = MutableStateFlow(AddRoundState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), AddRoundState())

    init {
        viewModelScope.launch {
            gameDataSource.getPlayersByGameId(gameId).collect { players ->
                _state.update {
                    it.copy(players = players)
                }
            }
        }
    }

    fun storeRound(
        points: List<Int>
    ){
        viewModelScope.launch {
            gameDataSource.storeRound(
                gameId = gameId,
                points = points,
            )
            _state.update {
                it.copy(isRoundAdded = true)
            }
        }
    }

}
