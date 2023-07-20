import com.tarokanizer.GameEntity

fun GameEntity.toGame() = Game(id = id, title = title)

fun Game.toGameEntity() = GameEntity(id = id, title = title)