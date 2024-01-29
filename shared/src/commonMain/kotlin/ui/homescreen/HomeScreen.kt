package ui.homescreen

import AddGameScreenRoute
import AppModule
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Game
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import ui.gamedetails.GameDetailsScreenRoute
import ui.shared.TarokanizerAlertDialog

data class HomeScreenRoute(val appModule: AppModule) : Screen {

    @Composable
    override fun Content() {
        HomeScreen(appModule)
    }

}

@Composable
fun HomeScreen(
    appModule: AppModule,
) {
    val viewModel = getViewModel(
        key = "home-screen",
        factory = viewModelFactory {
            HomeScreenViewModel(appModule.gameDataSource)
        }
    )

    val state by viewModel.state.collectAsState()

    HomeScreenContent(
        appModule = appModule,
        games = state.games,
        onDeleteGame = viewModel::deleteGame
    )
}

@Composable
private fun HomeScreenContent(
    appModule: AppModule,
    games: List<Game>,
    onDeleteGame: (String) -> Unit,
) {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.push(AddGameScreenRoute(appModule))
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "Add game",
                )
            }
        },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .defaultMinSize(minHeight = 64.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Tarokanizer",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            games.forEach {
                HomeScreenItem(
                    game = it,
                    onDeleteClicked = { id ->
                        onDeleteGame(id)
                    },
                    onGameClicked = { id ->
                        navigator.push(GameDetailsScreenRoute(appModule, id))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun HomeScreenItem(
    game: Game,
    onDeleteClicked: (String) -> Unit,
    onGameClicked: (String) -> Unit
) {

    val isDeleteDialogShown = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.LightGray,
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().defaultMinSize(minHeight = 56.dp).clickable {
            onGameClicked(game.id)
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = game.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.size(24.dp).clickable {
                    isDeleteDialogShown.value = true
                }
            )
        }
    }

    if (isDeleteDialogShown.value) {
        TarokanizerAlertDialog(
            title = "Are you sure you want to delete game: ${game.title}",
            confirmButtonText = "Yes",
            dismissButtonText = "No",
            onDismiss = {
                isDeleteDialogShown.value = false
            },
            onConfirm = {
                isDeleteDialogShown.value = false
                onDeleteClicked(game.id)
            }
        )
    }
}
