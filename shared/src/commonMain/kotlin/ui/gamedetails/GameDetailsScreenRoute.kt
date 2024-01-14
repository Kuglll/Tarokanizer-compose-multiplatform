package ui.gamedetails

import AppModule
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

data class GameDetailsScreenRoute(
    val appModule: AppModule,
    val id: String,
) : Screen {

    @Composable
    override fun Content() {
        GameDetailsScreen(
            appModule = appModule,
            id = id,
        )
    }

}

@Composable
fun GameDetailsScreen(
    appModule: AppModule,
    id: String,
) {
    val viewModel = getViewModel(
        key = "home-screen",
        factory = viewModelFactory {
            GameDetailsViewModel(appModule.gameDataSource, id)
        }
    )

    val state by viewModel.state.collectAsState()

    GameDetailsScreenContent()
}

@Composable
fun GameDetailsScreenContent() {
    val navigator = LocalNavigator.currentOrThrow

    Text("Game details")
}
