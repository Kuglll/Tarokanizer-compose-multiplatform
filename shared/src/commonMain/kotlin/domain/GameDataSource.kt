import com.tarokanizer.Database
import data.Game
import data.Player
import data.toGame
import data.toGameEntity
import data.toPlayer

interface GameDataSource {
    fun getGames(): List<Game>
    suspend fun storeGame(game: Game)
    suspend fun deleteGameById(id: Long)
    fun getPlayersByGameId(gameId: Long): List<Player>
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
        database.gameQueries.transaction {
            game.players.forEach { player ->
                database.gameQueries.insertPlayer(
                    gameId = game.id,
                    playerName = player.name,
                )
            }
        }
    }

    override suspend fun deleteGameById(id: Long) {
        database.gameQueries.deleteGameById(id)
    }

    override fun getPlayersByGameId(gameId: Long): List<Player> =
        database.gameQueries.selectPlayersByGameId(gameId = gameId).executeAsList().map {
            it.toPlayer()
        }

}
