package com.tidyjava.rankingapp

import java.time.LocalDateTime
import java.util.*

class RankingService(val ratings: RatingRepository,
                     val players: PlayerRepository,
                     val rankings: RankingRepository,
                     val matches: MatchRepository,
                     val matchFactory: MatchFactory) {

    fun join(playerName: String,
             rankingName: String) {
        val player = players.byName(playerName)!!
        val ranking = rankings.byName(rankingName)!!

        ratings.add(Rating(player, ranking))
    }

    fun addMatch(dateTime: LocalDateTime,
                 playerOneName: String,
                 playerTwoName: String,
                 result: String,
                 rankingName: String) {

        val player1 = players.byName(playerOneName)!!
        val player2 = players.byName(playerTwoName)!!
        val ranking = rankings.byName(rankingName)!!
        val match = matchFactory.create(dateTime, player1, player2, ranking, MatchResult.valueOf(result))
        matches.add(match)
    }

    fun confirmResult(id: UUID) {
        val match = matches.byId(MatchId(id)) ?: throw MatchNotFoundException(id)
        match.status = MatchStatus.CONFIRMED
    }
}

class MatchNotFoundException(id: UUID) : RuntimeException("Match $id not found!")
