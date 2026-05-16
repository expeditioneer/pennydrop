package dev.lamm.pennydrop.types

import dev.lamm.pennydrop.data.Game

data class Slot(
    val number: Int,
    val canBeFilled: Boolean = true,
    val isFilled: Boolean = false,
    val lastRolled: Boolean = false
) {
    companion object {
        fun mapFromGame(game: Game?) =
            (1..6).map { slotNum ->
                Slot(
                    number = slotNum,
                    canBeFilled = slotNum != 6,
                    isFilled = game?.filledSlots?.contains(slotNum) ?: false,
                    lastRolled = game?.lastRoll == slotNum
                )
            }
    }
}

fun List<Slot>.fullSlots(): Int =
    this.count { it.canBeFilled && it.isFilled }
