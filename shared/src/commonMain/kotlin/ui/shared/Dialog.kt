import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

@Composable
fun Dialog(
    content: @Composable () -> Unit,
    onDismiss: () -> Unit,
) {
    Popup(
        alignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.background(color = transparent_40).fillMaxSize().clickable {
                onDismiss()
            },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.padding(16.dp),
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                content()
            }
        }
    }
}