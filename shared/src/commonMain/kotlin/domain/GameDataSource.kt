import com.tarokanizer.Database

interface GameDataSource{
    fun getGames(): List<Game>
    suspend fun storeGame(game: Game)
}

class GameDataSourceImpl(
    val database: Database
): GameDataSource{

    override fun getGames(): List<Game> {
        return database.gameQueries.selectAllGames().executeAsList().map {
            it.toGame()
        }
    }

    override suspend fun storeGame(game: Game) {
        database.gameQueries.insertGame(game.toGameEntity())
    }

}