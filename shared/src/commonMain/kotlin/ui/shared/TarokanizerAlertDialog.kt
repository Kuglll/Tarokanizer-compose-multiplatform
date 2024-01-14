package ui.shared

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun TarokanizerAlertDialog(
    title: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
            ) {
                Text(dismissButtonText)
            }
        },
    )
}
