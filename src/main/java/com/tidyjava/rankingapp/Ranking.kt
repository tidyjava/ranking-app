package com.tidyjava.rankingapp

class Ranking(val name: String,
              val defaultRating: Int) {
}

interface RankingRepository {
    fun byName(name: String): Ranking?
}




