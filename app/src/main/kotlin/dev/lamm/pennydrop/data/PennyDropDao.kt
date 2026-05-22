package dev.lamm.pennydrop.data

import androidx.room.*
import dev.lamm.pennydrop.types.Player
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

@Dao
abstract class PennyDropDao {
    @Query("SELECT * FROM players WHERE playerName = :playerName")
    abstract fun getPlayer(playerName: String): Player?

    @Insert
    abstract suspend fun insertGame(game: Game): Long

    @Insert
    abstract suspend fun insertPlayer(player: Player): Long

    @Insert
    abstract suspend fun insertPlayers(players: List<Player>): List<Long>

    @Update
    abstract suspend fun updateGame(game: Game)

    @Transaction
    @Query("SELECT * FROM games ORDER BY startTime DESC LIMIT 1")
    abstract fun getCurrentGameWithPlayers(): Flow<GameWithPlayers?>

    @Transaction
    @Query(
        """
            SELECT * FROM game_statuses
            WHERE gameId = (
                SELECT gameId FROM games
                WHERE endTime is NULL
                ORDER BY startTime DESC
                LIMIT 1
            )
            ORDER BY gamePlayerNumber
    """
    )
    abstract fun getCurrentGameStatuses(): Flow<List<GameStatus>>

    @Query(
        """
        UPDATE games
        SET endTime = :endDate, gameState = :gameState
        WHERE endTime IS NULL
    """
    )
    abstract suspend fun closeOpenGames(
        endDate: OffsetDateTime = OffsetDateTime.now(),
        gameState: GameState = GameState.Cancelled
    )

    @Insert
    abstract suspend fun insertGameStatuses(gameStatuses: List<GameStatus>)

    @Transaction
    open suspend fun startGame(
        players: List<Player>,
        pennyCount: Int? = null,
        initialTurnText: String
    ): Long {
        this.closeOpenGames()

        val gameId = this.insertGame(
            Game(
                gameState = GameState.Started,
                currentTurnText = initialTurnText,
                canRoll = true
            )
        )

        val usedIds = mutableSetOf<Long>()
        val playerIds = players.map { player ->
            val existing = getPlayer(player.playerName)?.playerId
            val id = if (existing != null && existing !in usedIds) existing
                else insertPlayer(player)
            usedIds += id
            id
        }

        this.insertGameStatuses(
            playerIds.mapIndexed { index, playerId ->
                GameStatus(
                    gameId,
                    playerId,
                    index,
                    index == 0,
                    pennyCount ?: Player.defaultPennyCount
                )
            }
        )

        return gameId
    }

    @Update
    abstract suspend fun updateGameStatuses(gameStatuses: List<GameStatus>)

    @Update
    open suspend fun updateGameAndStatuses(
        game: Game,
        gameStatuses: List<GameStatus>
    ) {
        this.updateGame(game)
        this.updateGameStatuses(gameStatuses)
    }

    @Transaction
    @Query(
        """
            SELECT * FROM game_statuses gs
            WHERE gs.gameId IN (
                SELECT gameId FROM games
                WHERE gameState = :finishedGameState 
            )
        """
    )
    abstract fun getCompletedGameStatusesWithPlayers(
        finishedGameState: GameState = GameState.Finished
    ): Flow<List<GameStatusWithPlayer>>
}