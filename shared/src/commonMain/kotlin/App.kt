import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import ui.homescreen.HomeScreenRoute

@Composable
fun App() {
    MaterialTheme {

        Navigator(
            screen = HomeScreenRoute
        ){
            SlideTransition(navigator = it)
        }

    }
}
