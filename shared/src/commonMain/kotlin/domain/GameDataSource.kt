import com.tarokanizer.Database

interface GameDataSource{
    fun getGames(): List<Game>
}

class GameDataSourceImpl(
    val database: Database
): GameDataSource{

    override fun getGames(): List<Game> {
        return database.gameQueries.selectAllGames().executeAsList().map {
            it.toGame()
        }
    }

}