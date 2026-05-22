package dev.lamm.pennydrop.data

import dev.lamm.pennydrop.types.Player

class PennyDropRepository(private val pennyDropDao: PennyDropDao) {
    fun getCurrentGameWithPlayers() =
        pennyDropDao.getCurrentGameWithPlayers()

    fun getCurrentGameStatuses() =
        pennyDropDao.getCurrentGameStatuses()

    fun getCompletedGameStatusesWithPlayers() =
        pennyDropDao.getCompletedGameStatusesWithPlayers()

    suspend fun startGame(
        players: List<Player>,
        pennyCount: Int? = null,
        initialTurnText: String
    ) = pennyDropDao.startGame(players, pennyCount, initialTurnText)

    suspend fun updateGameAndStatuses(
        game: Game,
        statuses: List<GameStatus>
    ) = pennyDropDao.updateGameAndStatuses(game, statuses)
}