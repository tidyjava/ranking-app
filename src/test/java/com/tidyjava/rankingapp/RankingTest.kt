package com.tidyjava.rankingapp

import com.tidyjava.rankingapp.MatchResult.PLAYER1_WON
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class RankingTest {
    private val matchTime = LocalDateTime.of(2017, 8, 18, 21, 0)
    private val grzegorz = Player()
    private val james = Player()
    private val smackdown = Match(matchTime, grzegorz, james, PLAYER1_WON)

    private val ranking = Ranking("DZone Smackdown?!", Rating(1200))

    @Test
    fun cannotPlayWithoutJoining() {
        assertThrows(PlayerNotRankedException::class.java) {
            ranking.addMatch(smackdown)
        }
    }

    @Test
    fun canPlayAfterJoining() {
        ranking.join(grzegorz)
        ranking.join(james)

        ranking.addMatch(smackdown)

        assertEquals(listOf(smackdown), ranking.matches)
    }
}