package dev.lamm.pennydrop.types

import dev.lamm.pennydrop.game.AI

data class NewPlayer(
    val playerName: String = "",
    val isHuman: Boolean = true,
    val canBeRemoved: Boolean = true,
    val canBeToggled: Boolean = true,
    val isIncluded: Boolean = !canBeRemoved,
    val selectedAIPosition: Int = -1
) {
    fun selectedAI(): AI? = if (!isHuman) {
        AI.basicAI.getOrNull(selectedAIPosition)
    } else {
        null
    }

    fun toPlayer() = Player(
        playerName =
            if (this.isHuman) this.playerName
            else (this.selectedAI()?.name ?: "AI"),
        isHuman = this.isHuman,
        selectedAI = this.selectedAI()
    )
}
