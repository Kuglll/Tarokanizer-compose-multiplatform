import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import data.Game
import data.Player

const val DEFAULT_ANIMATION_DURATION = 300

@Composable
fun AddGameScreen(
    isVisible: Boolean,
    onBackPressed: () -> Unit,
    onGameSaved: (Game) -> Unit,
) {
    val isNumberOfPlayersSelectionVisible = remember { mutableStateOf(false) }
    val numberOfPlayersSelected = remember { mutableStateOf(2) }
    val gameTitle = remember { mutableStateOf("") }

    val playerNames = mutableListOf(mutableStateOf(""), mutableStateOf(""))

    val scrollState = rememberScrollState()

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = DEFAULT_ANIMATION_DURATION),
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = DEFAULT_ANIMATION_DURATION),
            targetOffsetY = { it }
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp).verticalScroll(scrollState),
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Navigate back", //TODO: See if this can be extracted to string resources
                modifier = Modifier.clickable {
                    // Clear state
                    numberOfPlayersSelected.value = 2
                    gameTitle.value = ""
                    onBackPressed()
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

            Button(onClick = {
                onGameSaved(
                    Game(
                        id = 0,
                        title = gameTitle.value,
                        players = playerNames.map {
                            Player(it.value)
                        }
                    )
                )
            }) {
                Text("Create game")
            }

            if (isNumberOfPlayersSelectionVisible.value) {
                NumberOfPlayersPopup(
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
}

@Composable
fun NumberOfPlayersPopup(
    onNumberOfPlayersChanged: (Int) -> Unit,
    onDismiss: () -> Unit,
) {

    val numberOfPlayers = remember { mutableStateOf("0") }

    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier.background(Color.White).padding(16.dp).padding(horizontal = 24.dp),
            ) {
                Text(text = "Choose number of players: ")
                Spacer(modifier = Modifier.height(8.dp))
                TarokanizerTextField(
                    value = numberOfPlayers.value,
                    onValueChange = {
                        numberOfPlayers.value = it
                    },
                    labelText = "Number of players",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    ),
                )
                TextButton(
                    onClick = {
                        try {
                            onNumberOfPlayersChanged(numberOfPlayers.value.trim().toInt())
                        } catch (e: NumberFormatException) {
                            //TODO: Show error
                        } finally {
                            onDismiss()
                        }
                    }
                ) {
                    Text("Ok")
                }
            }
        }
    )
}
