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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Player
import data.Round
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

    GameDetailsScreenContent(
        title = "Test",
        players = listOf("Player 1", "Player 2", "Player 3", "Playeeeeeeer 3", "Player 3", "Player 3", "Player 3", "Player 3").map {
            Player(
                it
            )
        },
        rounds = listOf(
            Round(listOf(0,0,0,0,0,0,0,0)),
            Round(listOf(0,0,0,0,0,0,0,0)),
            Round(listOf(0,0,0,0,0,0,0,0)),
            Round(listOf(0,0,0,0,0,0,0,0)),
        ),
    )
}

@Composable
fun GameDetailsScreenContent(
    title: String,
    players: List<Player>,
    rounds: List<Round>,
) {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    //TODO: Add round somehow
                },
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

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(scroll),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                players.forEachIndexed { index, player ->
                    Row {
                        Column {
                            Text(
                                text = player.name,
                                modifier = Modifier.width(80.dp).padding(4.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                            )
                            Divider(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(2.dp),
                                color = Color.Black,
                            )
                        }
                        if(index != players.size - 1){
                            Divider(
                                modifier = Modifier
                                    .width(2.dp)
                                    .fillMaxHeight(),
                                color = Color.Black,
                            )
                        }
                    }
                }
            }
            //TODO: Add UI for rounds
        }
        //TODO: Check how horizontal scroll works together with vertical scroll
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
