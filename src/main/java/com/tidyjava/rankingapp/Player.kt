package com.tidyjava.rankingapp

class Player(val name: String) {
}

interface PlayerRepository {
    fun byName(name: String): Player?
}
