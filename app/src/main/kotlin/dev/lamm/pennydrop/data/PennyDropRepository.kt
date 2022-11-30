package dev.lamm.pennydrop.data

import dev.lamm.pennydrop.types.Player

class PennyDropRepository(private val pennyDropDao: PennyDropDao) {
    fun getCurrentGameWithPlayers() =
        pennyDropDao.getCurrentGameWithPlayers()

    fun getCurrentGameStatuses() =
        pennyDropDao.getCurrentGameStatuses()

    fun getCompletedGameStatusesWithPlayers() =
        pennyDropDao.getCompletedGameStatusesWithPlayers()

    suspend fun startGame(players: List<Player>, pennyCount: Int? = null) =
        pennyDropDao.startGame(players, pennyCount)

    suspend fun updateGameAndStatuses(
        game: Game,
        statuses: List<GameStatus>
    ) = pennyDropDao.updateGameAndStatuses(game, statuses)

    companion object {
        @Volatile
        private var instance: PennyDropRepository? = null

        fun getInstance(pennyDropDao: PennyDropDao) =
            this.instance ?: synchronized(this) {
                instance ?: PennyDropRepository(pennyDropDao).also {
                    instance = it
                }
            }
    }
}