package di

import DriverFactory
import GameDataSource
import GameDataSourceImpl
import createDatabase
import org.koin.dsl.module
import ui.addgame.AddGameViewModel
import ui.addround.AddRoundViewModel
import ui.gamedetails.GameDetailsViewModel
import ui.homescreen.HomeScreenViewModel

actual fun getGameDataSourceModule() = module {
    single<GameDataSource> {
        GameDataSourceImpl(database = createDatabase(DriverFactory()))
    }
}
