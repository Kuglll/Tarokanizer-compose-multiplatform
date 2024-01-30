package ui.gamedetails

import GameDataSource
import data.Round
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameDetailsViewModel(
    private val gameDataSource: GameDataSource,
    private val gameId: String,
) : ViewModel() {

    private val _state = MutableStateFlow(GameDetailsState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), GameDetailsState())

    init {
        viewModelScope.launch {
            gameDataSource.getPlayersByGameId(gameId).collect { players ->
                _state.update {
                    it.copy(players = players)
                }
            }
        }

        viewModelScope.launch {
            gameDataSource.getRoundsByGameId(gameId).collect { rounds ->
                _state.update {
                    val sums = if(rounds.isNotEmpty()){
                        calculateSums(rounds)
                    } else {
                        emptyList()
                    }
                    it.copy(
                        rounds = rounds,
                        sums = sums,
                    )
                }
            }
        }

        viewModelScope.launch {
            _state.update {
                it.copy(title = gameDataSource.getGameTitleById(gameId))
            }
        }

    }

    private fun calculateSums(
        rounds: List<Round>,
    ): List<Int>{
        val numberOfPlayers = rounds[0].points.size
        val sums = MutableList(size = numberOfPlayers, init = {0})
        rounds.forEach {
            it.points.forEachIndexed { index, points ->
                sums[index] += points
            }
        }
        return sums
    }

}
