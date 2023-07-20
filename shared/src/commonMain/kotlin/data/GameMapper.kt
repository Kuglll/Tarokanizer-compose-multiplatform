package data

import com.tarokanizer.GameEntity

fun GameEntity.toGame() = Game(id = id, title = title, players = emptyList())

fun Game.toGameEntity() = GameEntity(id = id, title = title)