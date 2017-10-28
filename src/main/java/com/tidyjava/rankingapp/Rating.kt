package com.tidyjava.rankingapp

class Rating(val player: Player,
             val ranking: Ranking,
             val value: Int = ranking.defaultRating) {

}

interface RatingRepository {
    fun add(rating: Rating)
    fun existsByPlayerAndRanking(player: Player, ranking: Ranking): Boolean
}