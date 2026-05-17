package dev.lamm.pennydrop.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.lamm.pennydrop.data.GameStatusWithPlayer
import dev.lamm.pennydrop.data.PennyDropRepository
import dev.lamm.pennydrop.types.PlayerSummary
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RankingsViewModel @Inject constructor(
    repository: PennyDropRepository
) : ViewModel() {

    val playerSummaries: StateFlow<List<PlayerSummary>> =
        repository.getCompletedGameStatusesWithPlayers()
            .map(::toSummaries)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private companion object {
        fun toSummaries(statusesWithPlayers: List<GameStatusWithPlayer>): List<PlayerSummary> =
            statusesWithPlayers
                .groupBy { it.player }
                .map { (player, statuses) ->
                    PlayerSummary(
                        player.playerId,
                        player.playerName,
                        statuses.count(),
                        statuses.count { it.gameStatus.pennies == 0 },
                        player.isHuman
                    )
                }
                .sortedWith(compareBy({ -it.wins }, { -it.gamesPlayed }))
    }
}
