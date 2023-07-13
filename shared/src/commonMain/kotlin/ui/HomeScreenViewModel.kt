import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeScreenViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())

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

    fun addGame(game: String) {
        val updatedGames = _state.value.games.toMutableList()
        updatedGames.add(game)

        _state.update {
            it.copy(games = updatedGames, isAddGameScreenOpen = false)
        }
    }

}