package com.tidyjava.rankingapp

class InMemoryRatingRepository : RatingRepository {
    val ratings = mutableSetOf<Rating>()

    override fun add(rating: Rating) {
        ratings.add(rating)
    }

    override fun existsByPlayerAndRanking(player: Player, ranking: Ranking) =
            ratings.any { it.player == player && it.ranking == ranking }
}

class InMemoryMatchRepository : MatchRepository {
    val matches = mutableSetOf<Match>()

    override fun add(match: Match) {
        matches.add(match)
    }

    override fun byId(id: MatchId) = matches.find { it.id == id }
}

class InMemoryPlayerRepository : PlayerRepository {
    val players = mutableSetOf<Player>()

    override fun byName(name: String) = players.find { it.name == name }
}

class InMemoryRankingRepository : RankingRepository {
    val rankings = mutableSetOf<Ranking>()

    override fun byName(name: String) = rankings.find { it.name == name }
}