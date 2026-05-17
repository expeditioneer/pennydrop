package dev.lamm.pennydrop.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.lamm.pennydrop.types.NewPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PickPlayersViewModel @Inject constructor() : ViewModel() {
    private val _players = MutableStateFlow(initialPlayers())
    val players: StateFlow<List<NewPlayer>> = _players.asStateFlow()

    fun updatePlayer(index: Int, transform: (NewPlayer) -> NewPlayer) {
        _players.update { current ->
            if (index !in current.indices) current
            else current.mapIndexed { i, p -> if (i == index) transform(p) else p }
        }
    }

    private fun initialPlayers(): List<NewPlayer> = (1..6).map {
        NewPlayer(
            playerName = "Player $it",
            canBeRemoved = it > 2,
            canBeToggled = it > 1
        )
    }
}
