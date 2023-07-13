import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TarokanizerTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(labelText)
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
        ),
    )
}