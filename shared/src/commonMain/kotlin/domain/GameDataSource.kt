import com.tarokanizer.Database
import data.Game
import data.Player
import data.toGame
import data.toGameEntity
import data.toListOfPlayers

interface GameDataSource {
    fun getGames(): List<Game>
    suspend fun storeGame(game: Game)
    suspend fun deleteGameById(id: Long)
    fun getPlayers(id: Long): List<Player>
}

class GameDataSourceImpl(
    val database: Database
) : GameDataSource {

    override fun getGames(): List<Game> {
        return database.gameQueries.selectAllGames().executeAsList().map {
            it.toGame()
        }
    }

    override suspend fun storeGame(game: Game) {
        database.gameQueries.insertGame(game.toGameEntity())
        database.gameQueries.insertPlayers(
            game.id,
            game.players[0].name,
            game.players[1].name,
            game.players[2].name,
            game.players[3].name,
            game.players[4].name,
            game.players[5].name,
        )
    }

    override suspend fun deleteGameById(id: Long) {
        database.gameQueries.deleteGameById(id)
        database.gameQueries.deletePlayersById(id)
    }

    override fun getPlayers(id: Long): List<Player> = database.gameQueries.selectPlayersById(id).executeAsOne().toListOfPlayers()

}