package di

import org.koin.dsl.module
import ui.addgame.AddGameViewModel
import ui.addround.AddRoundViewModel
import ui.gamedetails.GameDetailsViewModel
import ui.homescreen.HomeScreenViewModel

actual fun getViewModelModule() = module {
    single {
        HomeScreenViewModel(get())
    }
    single { AddGameViewModel(get()) }
    single { (gameId: String) ->
        GameDetailsViewModel(get(), gameId)
    }
    single { (gameId: String) ->
        AddRoundViewModel(get(), gameId)
    }
}
