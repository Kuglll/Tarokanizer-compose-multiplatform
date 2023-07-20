package data

import com.tarokanizer.PlayerEntity

fun PlayerEntity.toListOfPlayers() = listOfNotNull(player1, player2, player3, player4, player5, player6).map {
    Player(it)
}