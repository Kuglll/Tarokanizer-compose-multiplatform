actual class AppModule {

    actual val gameDataSource: GameDataSource by lazy {
        GameDataSourceImpl(database = createDatabase(DriverFactory()))
    }
}