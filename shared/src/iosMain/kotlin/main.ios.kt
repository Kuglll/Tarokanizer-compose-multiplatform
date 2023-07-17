import androidx.compose.ui.window.ComposeUIViewController
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.tarokanizer.Database

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App(appModule = AppModule()) }

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(Database.Schema, "tarokanizer.db")
    }
}