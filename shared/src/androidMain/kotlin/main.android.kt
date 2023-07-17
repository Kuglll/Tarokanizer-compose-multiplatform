import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.tarokanizer.Database

actual fun getPlatformName(): String = "Android"

@Composable fun MainView() = App(appModule = AppModule(context = LocalContext.current.applicationContext))

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(Database.Schema, context, "tarokanizer.db")
    }
}