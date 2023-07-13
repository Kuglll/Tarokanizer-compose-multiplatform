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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

const val DEFAULT_ANIMATION_DURATION = 300

@Composable
fun AddGameScreen(
    isVisible: Boolean,
    onBackPressed: () -> Unit,
) {
    val isNumberOfPlayersSelectionVisible = remember { mutableStateOf(false) }
    val numberOfPlayersSelected = remember { mutableStateOf(2) }

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

            TextButton(onClick = { isNumberOfPlayersSelectionVisible.value = !isNumberOfPlayersSelectionVisible.value }) {
                Text("Number of players: ${numberOfPlayersSelected.value}")
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
    Popup(
        alignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier,
            color = Color.LightGray,
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp).padding(horizontal = 24.dp)) {
                Text(text = "2 Players", modifier = Modifier.clickable {
                    onNumberOfPlayersChanged(2)
                    onDismiss()
                })
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "3 Players", modifier = Modifier.clickable {
                    onNumberOfPlayersChanged(3)
                    onDismiss()
                })
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "4 Players", modifier = Modifier.clickable {
                    onNumberOfPlayersChanged(4)
                    onDismiss()
                })
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "5 Players", modifier = Modifier.clickable {
                    onNumberOfPlayersChanged(5)
                    onDismiss()
                })
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "6 Players", modifier = Modifier.clickable {
                    onNumberOfPlayersChanged(6)
                    onDismiss()
                })
            }
        }
    }
}