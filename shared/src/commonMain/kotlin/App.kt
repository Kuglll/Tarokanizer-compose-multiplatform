import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun App(
    appModule: AppModule
) {
    MaterialTheme {

        Navigator(
            HomeScreen(appModule)
        )

    }
}
