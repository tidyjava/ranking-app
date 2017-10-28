package com.tidyjava.rankingapp

import java.time.LocalDateTime
import java.util.*

class Match(val dateTime: LocalDateTime,
            val player1: Player,
            val player2: Player,
            val ranking: Ranking,
            var result: MatchResult,
            var status: MatchStatus = MatchStatus.WAITING_FOR_PLAYER2,
            val id: MatchId = MatchId()) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Match

        if (dateTime != other.dateTime) return false
        if (player1 != other.player1) return false
        if (player2 != other.player2) return false
        if (ranking != other.ranking) return false
        if (result != other.result) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result1 = dateTime.hashCode()
        result1 = 31 * result1 + player1.hashCode()
        result1 = 31 * result1 + player2.hashCode()
        result1 = 31 * result1 + ranking.hashCode()
        result1 = 31 * result1 + result.hashCode()
        result1 = 31 * result1 + status.hashCode()
        return result1
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

interface MatchRepository {
    fun add(match: Match)
    fun byId(id: MatchId): Match?
}

class MatchFactory(val ratings: RatingRepository) {

    fun create(dateTime: LocalDateTime, player1: Player, player2: Player, ranking: Ranking, result: MatchResult): Match {
        requireRanked(player1, ranking)
        requireRanked(player2, ranking)
        return Match(dateTime, player1, player2, ranking, result)
    }

    private fun requireRanked(player: Player, ranking: Ranking) {
        if (!ratings.existsByPlayerAndRanking(player, ranking)) {
            throw PlayerNotRankedException(player)
        }
    }
}

class PlayerNotRankedException(player: Player) : RuntimeException("Player $player has not joined this ranking!")