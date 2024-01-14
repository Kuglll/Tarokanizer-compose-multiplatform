import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import ui.homescreen.HomeScreenRoute

@Composable
fun App(
    appModule: AppModule //TODO: Check if this can be somehow injected automatically
) {
    MaterialTheme {

        Navigator(
            screen = HomeScreenRoute(appModule)
        ){
            SlideTransition(navigator = it)
        }

    }
}
