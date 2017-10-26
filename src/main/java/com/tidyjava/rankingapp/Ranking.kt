package com.tidyjava.rankingapp

import java.time.LocalDateTime
import java.util.*

class Ranking(val name: String,
              val defaultRating: Int,
              val ratings: Set<Rating> = mutableSetOf(),
              val matches: MutableList<Match> = mutableListOf()) {

    fun confirmResult(id: MatchId) {
        val match = matches.find { it.id == id } ?: throw MatchNotFoundException(id)
        match.status = MatchStatus.CONFIRMED
    }

    fun isRanked(player: Player) = ratings.any { it.player == player }
}

class RankingService(val ratings: RatingRepository,
                     val players: PlayerRepository,
                     val rankings: RankingRepository,
                     val matches: MatchRepository) {

    fun join(request: JoinRankingRequest) {
        val player = players.byName(request.player)!!
        val ranking = rankings.byName(request.ranking)!!

        ratings.add(Rating(player, ranking))
    }

    fun addMatch(request: AddMatchRequest) {
        val player1 = players.byName(request.player1)!!
        val player2 = players.byName(request.player2)!!
        val ranking = rankings.byName(request.ranking)!!

        matches.add(Match(
                request.dateTime,
                player1,
                player2,
                ranking,
                MatchResult.valueOf(request.result)))
    }
}

data class JoinRankingRequest(val player: String,
                              val ranking: String)

data class AddMatchRequest(val dateTime: LocalDateTime,
                           val player1: String,
                           val player2: String,
                           var result: String,
                           var ranking: String)

interface RatingRepository {
    fun add(rating: Rating)
}

interface RankingRepository {
    fun byName(name: String): Ranking?
}

interface PlayerRepository {
    fun byName(name: String): Player?
}

interface MatchRepository {
    fun add(match: Match)
}

class PlayerNotRankedException(player: Player) : RuntimeException("Player $player has not joined this ranking!")

class MatchNotFoundException(id: MatchId) : RuntimeException("Match $id not found!")

class Match(val dateTime: LocalDateTime,
            val player1: Player,
            val player2: Player,
            val ranking: Ranking,
            var result: MatchResult,
            var status: MatchStatus = MatchStatus.WAITING_FOR_PLAYER2,
            val id: MatchId = MatchId()) {

    init {
        requireRanked(player1, ranking)
        requireRanked(player2, ranking)
    }

    private fun requireRanked(player: Player, ranking: Ranking) {
        if (!ranking.isRanked(player))
            throw PlayerNotRankedException(player)
    }
}

data class MatchId(val value: UUID = UUID.randomUUID())

enum class MatchResult {
    PLAYER1_WON,
    PLAYER2_WON,
    DRAW
}

enum class MatchStatus {
    WAITING_FOR_PLAYER1, WAITING_FOR_PLAYER2, CONFIRMED
}

class Player(val name: String) {
}

class Rating(val player: Player,
             val ranking: Ranking,
             val value: Int = ranking.defaultRating) {

}
