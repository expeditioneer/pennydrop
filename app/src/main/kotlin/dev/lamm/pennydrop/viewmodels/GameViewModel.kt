package dev.lamm.pennydrop.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.lamm.pennydrop.R
import dev.lamm.pennydrop.data.GameState
import dev.lamm.pennydrop.data.GameStatus
import dev.lamm.pennydrop.data.GameWithPlayers
import dev.lamm.pennydrop.data.PennyDropRepository
import dev.lamm.pennydrop.game.GameHandler
import dev.lamm.pennydrop.game.TurnEnd
import dev.lamm.pennydrop.game.TurnResult
import dev.lamm.pennydrop.types.Player
import dev.lamm.pennydrop.types.Slot
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    application: Application,
    private val repository: PennyDropRepository
) : AndroidViewModel(application) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(application)
    private var clearText = false

    private val sharing = SharingStarted.WhileSubscribed(5_000)

    private val currentGameStatuses: StateFlow<List<GameStatus>> =
        repository.getCurrentGameStatuses()
            .stateIn(viewModelScope, sharing, emptyList())

    val currentGame: StateFlow<GameWithPlayers?> =
        combine(
            repository.getCurrentGameWithPlayers(),
            currentGameStatuses
        ) { gameWithPlayers, statuses ->
            gameWithPlayers?.updateStatuses(statuses)
        }.stateIn(viewModelScope, sharing, null)

    val currentPlayer: StateFlow<Player?> = currentGame
        .map { gameWithPlayers -> gameWithPlayers?.players?.firstOrNull { it.isRolling } }
        .stateIn(viewModelScope, sharing, null)

    val currentStandingsText: StateFlow<String?> = currentGame
        .map { gameWithPlayers ->
            gameWithPlayers?.players?.let { generateCurrentStandings(it) }
        }
        .stateIn(viewModelScope, sharing, null)

    val slots: StateFlow<List<Slot>> = currentGame
        .map { Slot.mapFromGame(it?.game) }
        .stateIn(viewModelScope, sharing, Slot.mapFromGame(null))

    val canRoll: StateFlow<Boolean> = currentGame
        .map { it?.game?.canRoll == true && it.players.firstOrNull { p -> p.isRolling }?.isHuman == true }
        .stateIn(viewModelScope, sharing, false)

    val canPass: StateFlow<Boolean> = currentGame
        .map { it?.game?.canPass == true && it.players.firstOrNull { p -> p.isRolling }?.isHuman == true }
        .stateIn(viewModelScope, sharing, false)

    private fun pennies(count: Int): String =
        getApplication<Application>().resources
            .getQuantityString(R.plurals.pennies, count, count)

    private fun generateCurrentStandings(
        players: List<Player>,
        headerText: String = getApplication<Application>().getString(R.string.current_standings)
    ) = players.sortedBy { it.pennies }
        .joinToString(separator = "\n", prefix = "$headerText\n") { player ->
            getApplication<Application>().getString(
                R.string.standings_row,
                player.playerName,
                pennies(player.pennies)
            )
        }

    private fun updateFilledSlots(
        result: TurnResult,
        filledSlots: List<Int>,
    ) = when {
        result.clearSlots -> emptyList()
        result.lastRoll != null && result.lastRoll != 6 -> filledSlots + result.lastRoll
        else -> filledSlots
    }

    private fun generateGameOverText(): String {
        val statuses = currentGameStatuses.value
        val players = currentGame.value?.players?.map { player ->
            player.apply {
                this.pennies = statuses
                    .firstOrNull { it.playerId == playerId }
                    ?.pennies
                    ?: Player.defaultPennyCount
            }
        }

        val winningPlayer = players
            ?.firstOrNull { it.penniesLeft() || it.isRolling }
            ?.apply { this.pennies = 0 }

        if (players == null || winningPlayer == null) return "N/A"

        val app = getApplication<Application>()
        return """${app.getString(R.string.game_over)}
            |${app.getString(R.string.game_winner, winningPlayer.playerName)}
            |
            |${generateCurrentStandings(players, "${app.getString(R.string.final_scores)}\n")}
        """.trimMargin()
    }

    private fun generateTurnText(result: TurnResult): String {
        val currentText =
            if (clearText) "" else currentGame.value?.game?.currentTurnText ?: ""

        clearText = result.turnEnd != null

        val currentPlayerName = result.currentPlayer?.playerName ?: "???"
        val app = getApplication<Application>()

        return when {
            result.isGameOver -> generateGameOverText()
            result.turnEnd == TurnEnd.Bust -> app.getString(
                R.string.turn_busted,
                result.previousPlayer?.playerName.orEmpty(),
                result.lastRoll ?: 0,
                pennies(result.coinChangeCount ?: 0),
                pennies(result.previousPlayer?.pennies ?: 0)
            ) + currentText
            result.turnEnd == TurnEnd.Pass -> app.getString(
                R.string.turn_passed,
                result.previousPlayer?.playerName.orEmpty(),
                pennies(result.previousPlayer?.pennies ?: 0)
            ) + currentText
            result.lastRoll != null -> app.getString(
                R.string.turn_rolled,
                currentPlayerName,
                result.lastRoll
            ) + currentText
            else -> ""
        }
    }

    private suspend fun playAITurn() {
        delay(if (prefs.getBoolean("fastAI", false)) 100 else 1000)
        val game = currentGame.value?.game
        val players = currentGame.value?.players
        val currentPlayer = currentPlayer.value
        val slots = slots.value

        if (game != null && players != null && currentPlayer != null) {
            GameHandler
                .playAITurn(players, currentPlayer, slots, game.canPass)?.let { result ->
                    updateFromGameHandler(result)
                }
        }
    }

    private fun updateFromGameHandler(result: TurnResult) {
        val game = currentGame.value?.let { currentGameWithPlayers ->
            currentGameWithPlayers.game.copy(
                gameState =
                if (result.isGameOver) GameState.Finished else GameState.Started,
                lastRoll = result.lastRoll,
                filledSlots = updateFilledSlots(result, currentGameWithPlayers.game.filledSlots),
                currentTurnText = generateTurnText(result),
                canPass = result.canPass,
                canRoll = result.canRoll,
                endTime = if (result.isGameOver) OffsetDateTime.now() else null
            )
        } ?: return

        val statuses = currentGameStatuses.value.map { status ->
            when (status.playerId) {
                result.previousPlayer?.playerId -> {
                    status.copy(
                        isRolling = false,
                        pennies = status.pennies + (result.coinChangeCount ?: 0)
                    )
                }

                result.currentPlayer?.playerId -> {
                    status.copy(
                        isRolling = !result.isGameOver,
                        pennies = status.pennies +
                                if (!result.playerChanged) {
                                    result.coinChangeCount ?: 0
                                } else 0
                    )
                }

                else -> status
            }
        }

        viewModelScope.launch {
            repository.updateGameAndStatuses(game, statuses)
            if (result.currentPlayer?.isHuman == false) {
                playAITurn()
            }
        }
    }

    suspend fun startGame(playersForNewGame: List<Player>) {
        repository.startGame(
            playersForNewGame,
            prefs?.getInt("pennyCount", Player.defaultPennyCount),
            getApplication<Application>().getString(R.string.game_started)
        )
    }

    fun roll() {
        val game = currentGame.value?.game
        val players = currentGame.value?.players
        val currentPlayer = currentPlayer.value
        val slots = slots.value

        if (game != null && players != null && currentPlayer != null && game.canRoll) {
            updateFromGameHandler(
                GameHandler.roll(players, currentPlayer, slots)
            )
        }
    }

    fun pass() {
        val game = currentGame.value?.game
        val players = currentGame.value?.players
        val currentPlayer = currentPlayer.value

        if (game != null && players != null && currentPlayer != null && game.canPass) {
            updateFromGameHandler(
                GameHandler.pass(players, currentPlayer)
            )
        }
    }
}
