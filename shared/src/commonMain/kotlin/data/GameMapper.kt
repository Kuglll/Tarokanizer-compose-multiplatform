import com.tarokanizer.GameEntity

fun GameEntity.toGame() = Game(id = id, title = title)