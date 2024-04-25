package di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ui.addgame.AddGameViewModel
import ui.addround.AddRoundViewModel
import ui.gamedetails.GameDetailsViewModel
import ui.homescreen.HomeScreenViewModel

actual fun getViewModelModule() = module {
    viewModel { HomeScreenViewModel(get()) }
    viewModel { AddGameViewModel(get()) }
    viewModel { (gameId: String) ->
        GameDetailsViewModel(get(), gameId)
    }
    viewModel { (gameId: String) ->
        AddRoundViewModel(get(), gameId)
    }
}
