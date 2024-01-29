package ui.addround

import AppModule
import TarokanizerTextField
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Player
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

data class AddRoundScreenRoute(
    val appModule: AppModule,
    val gameId: String,
) : Screen {

    @Composable
    override fun Content() {
        AddRoundScreen(
            appModule = appModule,
            gameId = gameId,
        )
    }

}

@Composable
fun AddRoundScreen(
    appModule: AppModule,
    gameId: String,
) {

    val navigator = LocalNavigator.currentOrThrow

    val viewModel = getViewModel(
        key = "add-round",
        factory = viewModelFactory {
            AddRoundViewModel(appModule.gameDataSource, gameId)
        }
    )

    val state by viewModel.state.collectAsState()

    if (state.isRoundAdded) {
        LaunchedEffect(state) {
            navigator.pop()
        }
    }

    AddRoundScreenContent(
        players = state.players,
        onStoreRoundClicked = viewModel::storeRound,
    )

}

@Composable
private fun AddRoundScreenContent(
    players: List<Player>,
    onStoreRoundClicked: (List<Int>) -> Unit,
) {
    val navigator = LocalNavigator.currentOrThrow

    val playerPoints = remember(players) {
        players.map { _ ->
            mutableStateOf("")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Navigate back",
            modifier = Modifier.clickable {
                navigator.pop()
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Add points for players:")

        Spacer(modifier = Modifier.height(8.dp))

        players.forEachIndexed { index, player ->
            TarokanizerTextField(
                value = playerPoints[index].value,
                onValueChange = {
                    playerPoints[index].value = it
                },
                labelText = player.name,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onStoreRoundClicked(
                    playerPoints.map {
                        if(it.value.isEmpty()){
                            0
                        } else {
                            it.value.toInt() //TODO: Somehow prevent storing floats and doubles
                        }
                    }
                )
            },
        ) {
            Text(text = "Add round")
        }
    }
}
