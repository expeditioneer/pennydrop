package dev.lamm.pennydrop.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.lamm.pennydrop.data.PennyDropRepository
import dev.lamm.pennydrop.types.PlayerSummary
import javax.inject.Inject

@HiltViewModel
class RankingsViewModel @Inject constructor(
    application: Application,
    private val repository: PennyDropRepository
) : AndroidViewModel(application) {

    val playerSummaries: LiveData<List<PlayerSummary>> = Transformations.map(
        this.repository.getCompletedGameStatusesWithPlayers()
    ) { statusesWithPlayers ->
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