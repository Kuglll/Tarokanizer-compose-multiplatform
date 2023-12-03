import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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

const val FIRST_PLAYER = 0
const val SECOND_PLAYER = 1
const val THIRD_PLAYER = 2
const val FOURTH_PLAYER = 3
const val FIFTH_PLAYER = 4
const val SIXTH_PLAYER = 5

@Composable
fun AddGameScreen(
    isVisible: Boolean,
    onBackPressed: () -> Unit,
    onGameSaved: (Game) -> Unit,
) {
    val isNumberOfPlayersSelectionVisible = remember { mutableStateOf(false) }
    val numberOfPlayersSelected = remember { mutableStateOf(2) }
    val gameTitle = remember { mutableStateOf("") }

    //TODO: Implement state clearing

    val player1Name = remember { mutableStateOf("") }
    val player2Name = remember { mutableStateOf("") }
    val player3Name = remember { mutableStateOf("") }
    val player4Name = remember { mutableStateOf("") }
    val player5Name = remember { mutableStateOf("") }
    val player6Name = remember { mutableStateOf("") }

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
            modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Navigate back",
                modifier = Modifier.clickable {
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
                    value = when (playerIndex) {
                        FIRST_PLAYER -> player1Name.value
                        SECOND_PLAYER -> player2Name.value
                        THIRD_PLAYER -> player3Name.value
                        FOURTH_PLAYER -> player4Name.value
                        FIFTH_PLAYER -> player5Name.value
                        SIXTH_PLAYER -> player6Name.value
                        else -> ""
                    },
                    onValueChange = when (playerIndex) {
                        FIRST_PLAYER -> {
                            { player1Name.value = it }
                        }

                        SECOND_PLAYER -> {
                            { player2Name.value = it }
                        }

                        THIRD_PLAYER -> {
                            { player3Name.value = it }
                        }

                        FOURTH_PLAYER -> {
                            { player4Name.value = it }
                        }

                        FIFTH_PLAYER -> {
                            { player5Name.value = it }
                        }

                        SIXTH_PLAYER -> {
                            { player6Name.value = it }
                        }

                        else -> {
                            {}
                        }
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
                        players = listOf(
                            if (player1Name.value.isNotEmpty()) Player(player1Name.value) else Player(""),
                            if (player2Name.value.isNotEmpty()) Player(player2Name.value) else Player(""),
                            if (player3Name.value.isNotEmpty()) Player(player3Name.value) else Player(""),
                            if (player4Name.value.isNotEmpty()) Player(player4Name.value) else Player(""),
                            if (player5Name.value.isNotEmpty()) Player(player5Name.value) else Player(""),
                            if (player6Name.value.isNotEmpty()) Player(player6Name.value) else Player(""),
                        )
                    )
                )
            }) {
                Text("Create game")
            }

            if (isNumberOfPlayersSelectionVisible.value) {
                NumberOfPlayersPopup(
                    onNumberOfPlayersChanged = {
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
