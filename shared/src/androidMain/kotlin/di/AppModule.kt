import android.content.Context

actual class AppModule(
    private val context: Context
) {

    actual val gameDataSource: GameDataSource by lazy {
        GameDataSourceImpl(
            database = createDatabase(DriverFactory(context))
        )
    }

}