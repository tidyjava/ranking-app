package com.tidyjava.rankingapp

import com.tidyjava.rankingapp.MatchResult.PLAYER1_WON
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class RankingTest {
    private val matchTime = LocalDateTime.of(2017, 8, 18, 21, 0)
    private val grzegorz = Player("grzegorz")
    private val james = Player("james")
    private val ranking = Ranking("DZone Smackdown?!", 1200)

    private val ratingRepo = InMemoryRatingRepository()
    private val playerRepo = InMemoryPlayerRepository()
    private val rankingRepo = InMemoryRankingRepository()
    private val matchRepo = InMemoryMatchRepository()
    private val service = RankingService(ratingRepo, playerRepo, rankingRepo, matchRepo)

    @BeforeEach
    fun setUp() {
        playerRepo.players.add(grzegorz)
        playerRepo.players.add(james)
        rankingRepo.rankings.add(ranking)
    }

    @Test
    fun cannotPlayWithoutJoining() {
        assertThrows(PlayerNotRankedException::class.java) {
            service.addMatch(AddMatchRequest(matchTime, grzegorz.name, james.name, PLAYER1_WON.toString(), ranking.name))
        }
    }

    @Test
    fun canPlayAfterJoining() {
        service.join(JoinRankingRequest(grzegorz.name, ranking.name))
        service.join(JoinRankingRequest(james.name, ranking.name))

        service.addMatch(AddMatchRequest(matchTime, grzegorz.name, james.name, PLAYER1_WON.toString(), ranking.name))

        assertEquals(setOf(Match(matchTime, grzegorz, james, ranking, PLAYER1_WON)), matchRepo.matches)
    }
}

class InMemoryRatingRepository : RatingRepository {
    val ratings = mutableSetOf<Rating>()

    override fun add(rating: Rating) {
        ratings.add(rating)
    }
}

class InMemoryMatchRepository : MatchRepository {
    val matches = mutableSetOf<Match>()

    override fun add(match: Match) {
        matches.add(match)
    }
}

class InMemoryPlayerRepository : PlayerRepository {
    val players = mutableSetOf<Player>()

    override fun byName(name: String) = players.find { it.name == name }
}

class InMemoryRankingRepository : RankingRepository {
    val rankings = mutableSetOf<Ranking>()

    override fun byName(name: String) = rankings.find { it.name == name }
}