package ui.gamedetails

import AppModule
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Player
import data.Round
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import ui.addround.AddRoundScreenRoute
import ui.shared.getScreenWidth

data class GameDetailsScreenRoute(
    val appModule: AppModule,
    val gameId: String,
) : Screen {

    @Composable
    override fun Content() {
        GameDetailsScreen(
            appModule = appModule,
            gameId = gameId,
        )
    }

}

@Composable
fun GameDetailsScreen(
    appModule: AppModule,
    gameId: String,
) {
    val navigator = LocalNavigator.currentOrThrow

    val viewModel = getViewModel(
        key = "game-details",
        factory = viewModelFactory {
            GameDetailsViewModel(appModule.gameDataSource, gameId)
        }
    )

    val state by viewModel.state.collectAsState()

    GameDetailsScreenContent(
        title = state.title,
        players = state.players,
        rounds = state.rounds,
        onAddRoundClicked = {
            navigator.push(AddRoundScreenRoute(appModule, gameId))
        }
    )
}

@Composable
fun GameDetailsScreenContent(
    title: String,
    players: List<Player>,
    rounds: List<Round>,
    onAddRoundClicked: () -> Unit,
) {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRoundClicked,
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "Add round",
                )
            }
        },
        topBar = {
            GameDetailsScreenTopBar(
                title = title,
                onBackPressed = {
                    navigator.pop()
                }
            )
        }
    ) {
        val scroll = rememberScrollState()

        val screenWidth = getScreenWidth()
        val columnMinWidth = remember { 80.dp }

        val columnWidth = if (canAllPlayersBeDisplayedOnScreen(
                players = players,
                screenWidth = screenWidth.dp,
                playerRowMinWidth = columnMinWidth,
            )
        ) {
            screenWidth.dp / players.size
        } else {
            columnMinWidth
        }


        Column(
            modifier = Modifier.fillMaxSize().horizontalScroll(scroll),
        ) {
            PlayerNamesRow(
                players = players,
                columnWidth = columnWidth,
            )
            RoundsContent(
                rounds = rounds,
                columnWidth = columnWidth,
            )
        }
    }

}

@Composable
private fun GameDetailsScreenTopBar(
    title: String,
    onBackPressed: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .defaultMinSize(minHeight = 64.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Navigate back",
            modifier = Modifier.clickable {
                onBackPressed()
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PlayerNamesRow(
    players: List<Player>,
    columnWidth: Dp,
) {
    val playerRowHeight = remember { mutableStateOf(0.dp) }

    val density = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        players.forEachIndexed { index, player ->
            Row(
                modifier = Modifier.onGloballyPositioned {
                    with(density) {
                        playerRowHeight.value = it.size.height.toDp()
                    }
                }
            ) {
                Column {
                    Text(
                        text = player.name,
                        modifier = Modifier.width(columnWidth).padding(4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                    Divider(
                        modifier = Modifier.width(columnWidth).height(2.dp),
                        color = Color.Black,
                    )
                }
                if (index != players.size - 1) {
                    Divider(
                        modifier = Modifier
                            .width(2.dp)
                            .height(playerRowHeight.value),
                        color = Color.Black,
                    )
                }
            }
        }
    }
}

private fun canAllPlayersBeDisplayedOnScreen(
    players: List<Player>,
    screenWidth: Dp,
    playerRowMinWidth: Dp,
): Boolean = playerRowMinWidth.value * players.size <= screenWidth.value

@Composable
private fun RoundsContent(
    rounds: List<Round>,
    columnWidth: Dp,
) {
    val singleRoundRowHeight = remember { mutableStateOf(0.dp) }

    val density = LocalDensity.current

    LazyColumn {
        items(rounds) { round ->
            Row(
                modifier = Modifier.onGloballyPositioned {
                    with(density) {
                        singleRoundRowHeight.value = it.size.height.toDp()
                    }
                }
            ) {
                round.points.forEachIndexed { index, it ->
                    Row {
                        Column {
                            Text(
                                text = it.toString(),
                                modifier = Modifier.width(columnWidth),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                            )
                            Divider(
                                modifier = Modifier
                                    .width(columnWidth)
                                    .height(2.dp),
                                color = Color.Black,
                            )
                        }
                        if (index != round.points.size - 1) {
                            Divider(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(singleRoundRowHeight.value),
                                color = Color.Black,
                            )
                        }
                    }
                }
            }
        }
    }
}
