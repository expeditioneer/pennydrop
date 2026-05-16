package dev.lamm.pennydrop.types

import dev.lamm.pennydrop.data.Game
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SlotTest {

    @Test
    fun `mapFromGame returns six slots`() {
        val slots = Slot.mapFromGame(null)
        assertEquals(6, slots.size)
        slots.forEachIndexed { index, slot ->
            assertEquals(index + 1, slot.number)
        }
    }

    @Test
    fun `slot 6 cannot be filled`() {
        val slots = Slot.mapFromGame(null)
        assertFalse(slots[5].canBeFilled)
        slots.take(5).forEach { assertTrue(it.canBeFilled) }
    }

    @Test
    fun `mapFromGame reflects filledSlots and lastRoll`() {
        val game = Game(filledSlots = listOf(1, 3), lastRoll = 3)
        val slots = Slot.mapFromGame(game)

        assertTrue(slots[0].isFilled)
        assertFalse(slots[1].isFilled)
        assertTrue(slots[2].isFilled)
        assertFalse(slots[3].isFilled)

        assertTrue(slots[2].lastRolled)
        assertFalse(slots[0].lastRolled)
    }

    @Test
    fun `fullSlots counts only fillable filled slots`() {
        val slots = listOf(
            Slot(1, isFilled = true),
            Slot(2, isFilled = false),
            Slot(3, isFilled = true),
            Slot(6, canBeFilled = false, isFilled = true) // unreachable but defensive
        )
        assertEquals(2, slots.fullSlots())
    }
}
