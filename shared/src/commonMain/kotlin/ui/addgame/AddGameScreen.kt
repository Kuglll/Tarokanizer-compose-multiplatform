import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import ui.addgame.AddGameViewModel

data class AddGameScreenRoute(
    val appModule: AppModule,
) : Screen {

    @Composable
    override fun Content() {
        AddGameScreen(
            appModule = appModule,
        )
    }
}

@Composable
fun AddGameScreen(
    appModule: AppModule,
) {
    val isNumberOfPlayersSelectionVisible = remember { mutableStateOf(false) }
    val gameTitle = remember { mutableStateOf("") }

    val playerNames = remember { mutableListOf(mutableStateOf(""), mutableStateOf("")) }
    val numberOfPlayersSelected = remember { mutableStateOf(playerNames.size) }

    val scrollState = rememberScrollState()

    val viewModel = getViewModel(
        key = "add-game-screen",
        factory = viewModelFactory {
            AddGameViewModel(appModule.gameDataSource)
        }
    )

    val navigator = LocalNavigator.currentOrThrow

    val state by viewModel.state.collectAsState()

    if (state.isGameAdded) {
        LaunchedEffect(state) {
            navigator.pop()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp).verticalScroll(scrollState),
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Navigate back", //TODO: See if this can be extracted to string resources
            modifier = Modifier.clickable {
                // Clear state
                gameTitle.value = ""
                navigator.pop()
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TarokanizerTextField(
            value = gameTitle.value,
            onValueChange = {
                gameTitle.value = it
            },
            labelText = "Title",
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { isNumberOfPlayersSelectionVisible.value = !isNumberOfPlayersSelectionVisible.value }) {
            //TODO: Replace with stringResource(SharedRes.strings.number_of_players) when https://github.com/icerockdev/moko-resources/issues/613 is fixed
            Text("Number of players: ${numberOfPlayersSelected.value}")
        }

        (0 until numberOfPlayersSelected.value).forEach { playerIndex ->
            TarokanizerTextField(
                value = playerNames[playerIndex].value,
                onValueChange = {
                    playerNames[playerIndex].value = it
                },
                labelText = "Player ${playerIndex + 1}"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.addGame(
                    gameTitle = gameTitle.value,
                    playerNames = playerNames.map { it.value },
                )
            },
        ) {
            Text("Create game")
        }

        if (isNumberOfPlayersSelectionVisible.value) {
            NumberOfPlayersPopup(
                initialNumberOfPlayer = playerNames.size,
                onNumberOfPlayersChanged = {
                    playerNames.clear()
                    (0 until it).forEach { _ ->
                        playerNames.add(
                            mutableStateOf("")
                        )
                    }
                    numberOfPlayersSelected.value = it
                },
                onDismiss = {
                    isNumberOfPlayersSelectionVisible.value = false
                }
            )
        }
    }

}

@Composable
fun NumberOfPlayersPopup(
    initialNumberOfPlayer: Int,
    onNumberOfPlayersChanged: (Int) -> Unit,
    onDismiss: () -> Unit,
) {

    val numberOfPlayers = remember { mutableStateOf(initialNumberOfPlayer) }

    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier.background(Color.White).padding(16.dp).padding(horizontal = 24.dp),
            ) {
                Text(text = "Choose number of players: ")
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "${numberOfPlayers.value}",
                        fontSize = 20.sp,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            numberOfPlayers.value--
                        }
                    ) {
                        Text(
                            text = "-",
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            numberOfPlayers.value++
                        }
                    ) {
                        Text(
                            text = "+",
                            fontSize = 20.sp,
                        )
                    }
                }
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            try {
                                onNumberOfPlayersChanged(numberOfPlayers.value)
                            } catch (e: NumberFormatException) {
                                //TODO: Show error
                            } finally {
                                onDismiss()
                            }
                        }
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    )
}
