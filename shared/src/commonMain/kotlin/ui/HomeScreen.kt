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
import androidx.compose.material.TextButton
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
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import ui.HomeScreenViewModel

@Composable
fun HomeScreen(appModule: AppModule) {

    val viewModel = getViewModel(
        key = "home-screen",
        factory = viewModelFactory {
            HomeScreenViewModel(appModule.gameDataSource)
        }
    )

    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.openAddGameScreen()
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "Add game",
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
            Spacer(modifier = Modifier.height(24.dp))
            state.games.forEach {
                HomeScreenItem(
                    game = it,
                    onDeleteClicked = { id ->
                        viewModel.deleteGame(id)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    AddGameScreen(
        isVisible = state.isAddGameScreenOpen,
        onBackPressed = {
            viewModel.hideAddGameScreen()
        },
        onGameSaved = { gameTitle ->
            viewModel.addGame(gameTitle)
        }
    )
}

@Composable
fun HomeScreenItem(
    game: Game,
    onDeleteClicked: (Long) -> Unit,
) {

    val isDeleteDialogShown = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.LightGray,
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().defaultMinSize(minHeight = 56.dp)
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
        AreYouSureYouWantToDeleteDialog(
            gameTitle = game.title,
            onDismiss = { isDeleteDialogShown.value = false },
            onConfirm = {
                onDeleteClicked(game.id)
            }
        )
    }
}

@Composable
fun AreYouSureYouWantToDeleteDialog(
    gameTitle: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(
        onDismiss = onDismiss,
        content = {
            Column {
                Text(
                    text = "Are you sure you want to delete game: $gameTitle",
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
                )
                Row {
                    Modifier.weight(1f)
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("No")
                    }
                    TextButton(
                        onClick = onConfirm
                    ) {
                        Text("Yes")
                    }
                }
            }
        }
    )
}