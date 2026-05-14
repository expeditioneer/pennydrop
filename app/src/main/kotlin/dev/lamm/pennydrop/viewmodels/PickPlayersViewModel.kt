package dev.lamm.pennydrop.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.lamm.pennydrop.types.NewPlayer
import javax.inject.Inject

@HiltViewModel
class PickPlayersViewModel @Inject constructor() : ViewModel() {
    private val _players = MutableLiveData<List<NewPlayer>>(initialPlayers())
    val players: LiveData<List<NewPlayer>> = _players

    fun updatePlayer(index: Int, transform: (NewPlayer) -> NewPlayer) {
        val current = _players.value ?: return
        if (index !in current.indices) return
        _players.value = current.mapIndexed { i, p -> if (i == index) transform(p) else p }
    }

    private fun initialPlayers(): List<NewPlayer> = (1..6).map {
        NewPlayer(
            playerName = "Player $it",
            canBeRemoved = it > 2,
            canBeToggled = it > 1
        )
    }
}
