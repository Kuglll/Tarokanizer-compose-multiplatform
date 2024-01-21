import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.tarokanizer.Database
import data.Game
import data.Player
import data.toGame
import data.toGameEntity
import data.toPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GameDataSource {
    fun getGames(): Flow<List<Game>>
    fun getGameTitleById(id: String): String
    suspend fun storeGame(game: Game)
    suspend fun deleteGameById(id: String)
    fun getPlayersByGameId(gameId: String): Flow<List<Player>>
}

class GameDataSourceImpl(
    val database: Database
) : GameDataSource {

    override fun getGames(): Flow<List<Game>> {
        return database.gameQueries.selectAllGames().asFlow().mapToList(Dispatchers.IO).map {
            it.map { it.toGame() }
        }
    }

    override fun getGameTitleById(id: String): String {
        return database.gameQueries.getGameTitleById(id).executeAsOne()
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

    override suspend fun deleteGameById(id: String) {
        database.gameQueries.deleteGameById(id)
    }

    override fun getPlayersByGameId(gameId: String): Flow<List<Player>> =
        database.gameQueries.selectPlayersByGameId(gameId = gameId).asFlow().mapToList(Dispatchers.IO).map {
            it.map { playerEntity ->
                playerEntity.toPlayer()
            }
        }

}
