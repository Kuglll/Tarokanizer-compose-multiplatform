import com.tarokanizer.Database

interface GameDataSource{
    fun getGames(): List<Game>
    suspend fun storeGame(game: Game)
    suspend fun deleteGameById(id: Long)
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

    override suspend fun deleteGameById(id: Long) {
        database.gameQueries.deleteGameById(id)
    }

}