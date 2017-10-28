package com.tidyjava.rankingapp

import com.tidyjava.rankingapp.MatchResult.PLAYER1_WON
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class RankingServiceTest {
    private val matchTime = LocalDateTime.of(2017, 8, 18, 21, 0)
    private val grzegorz = Player("grzegorz")
    private val james = Player("james")
    private val ranking = Ranking("DZone Smackdown?!", 1200)

    private val ratingRepo = InMemoryRatingRepository()
    private val playerRepo = InMemoryPlayerRepository()
    private val rankingRepo = InMemoryRankingRepository()
    private val matchRepo = InMemoryMatchRepository()
    private val matchFactory = MatchFactory(ratingRepo)

    private val service = RankingService(ratingRepo, playerRepo, rankingRepo, matchRepo, matchFactory)

    @BeforeEach
    fun setUp() {
        playerRepo.players.add(grzegorz)
        playerRepo.players.add(james)
        rankingRepo.rankings.add(ranking)
    }

    @Test
    fun cannotPlayWithoutJoining() {
        assertThrows(PlayerNotRankedException::class.java) {
            service.addMatch(matchTime, grzegorz.name, james.name, PLAYER1_WON.toString(), ranking.name)
        }
    }

    @Test
    fun canPlayAfterJoining() {
        service.join(grzegorz.name, ranking.name)
        service.join(james.name, ranking.name)

        service.addMatch(matchTime, grzegorz.name, james.name, PLAYER1_WON.toString(), ranking.name)

        assertEquals(
                setOf(Match(matchTime, grzegorz, james, ranking, PLAYER1_WON)),
                matchRepo.matches)
    }
}
