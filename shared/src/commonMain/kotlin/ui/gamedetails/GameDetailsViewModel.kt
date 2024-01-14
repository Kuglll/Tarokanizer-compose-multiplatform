package ui.gamedetails

import GameDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import ui.homescreen.HomeScreenState

class GameDetailsViewModel(
    private val gameDataSource: GameDataSource,
    private val id: String,
) : ViewModel() {

    private val _state = MutableStateFlow(GameDetailsState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), GameDetailsState())

    init {

        //TODO: Get game details - name, players, points, etc.
    }

}
