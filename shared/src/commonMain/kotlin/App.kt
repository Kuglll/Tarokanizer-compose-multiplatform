import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun App(
    appModule: AppModule //TODO: Check if this can be somehow injected automatically
) {
    MaterialTheme {

        Navigator(
            HomeScreen(appModule)
        )

    }
}
