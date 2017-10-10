package com.tidyjava.rankingapp

import java.time.LocalDateTime
import java.util.*

class Ranking(val name: String,
              val defaultRating: Rating,
              val ratings: MutableMap<Player, Rating> = mutableMapOf(),
              val matches: MutableList<Match> = mutableListOf()) {

    fun join(player: Player) {
        ratings.put(player, defaultRating)
    }

    fun addMatch(match: Match) {
        requireRanked(match.player1)
        requireRanked(match.player2)
        matches.add(match)
    }

    private fun requireRanked(player: Player) {
        if (!ratings.containsKey(player))
            throw PlayerNotRankedException(player)
    }

    fun confirmResult(id: MatchId) {
        val match = matches.find { it.id == id } ?: throw MatchNotFoundException(id)
        match.status = MatchStatus.CONFIRMED
    }
}

class PlayerNotRankedException(player: Player) : RuntimeException("Player $player has not joined this ranking!")

class MatchNotFoundException(id: MatchId) : RuntimeException("Match $id not found!")

class Match(val dateTime: LocalDateTime,
            val player1: Player,
            val player2: Player,
            var result: MatchResult,
            var status: MatchStatus = MatchStatus.WAITING_FOR_PLAYER2,
            val id: MatchId = MatchId()) {


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

class Player {

}

data class Rating(val value: Int)
