package edu.put.and_test.models

import java.io.Serializable

data class Game(val name: String, val yearPublished: Int?, val id: Int, val thumbnail: String?): Serializable
data class SingleGame(val game: Game, val description: String?, val image: String?, val rank: String?):
    Serializable
