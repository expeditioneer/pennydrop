package dev.lamm.pennydrop.game

import dev.lamm.pennydrop.types.Slot
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AITest {

    private fun slotsWithFilled(filledCount: Int): List<Slot> =
        (1..6).map { number ->
            Slot(
                number = number,
                canBeFilled = number != 6,
                isFilled = number != 6 && number <= filledCount
            )
        }

    private fun ai(name: String) =
        AI.basicAI.first { it.name == name }

    @Test
    fun `No Go Noah only rolls when no slots are filled`() {
        val noah = ai("No Go Noah")

        assertTrue(noah.rollAgain(slotsWithFilled(0)))
        assertFalse(noah.rollAgain(slotsWithFilled(1)))
        assertFalse(noah.rollAgain(slotsWithFilled(5)))
    }

    @Test
    fun `Bail Out Beulah rolls at 0 or 1 filled, not above`() {
        val beulah = ai("Bail Out Beulah")

        assertTrue(beulah.rollAgain(slotsWithFilled(0)))
        assertTrue(beulah.rollAgain(slotsWithFilled(1)))
        assertFalse(beulah.rollAgain(slotsWithFilled(2)))
        assertFalse(beulah.rollAgain(slotsWithFilled(5)))
    }

    @Test
    fun `Fearful Fred rolls at 0 to 2 filled`() {
        val fred = ai("Fearful Fred")

        assertTrue(fred.rollAgain(slotsWithFilled(0)))
        assertTrue(fred.rollAgain(slotsWithFilled(2)))
        assertFalse(fred.rollAgain(slotsWithFilled(3)))
    }

    @Test
    fun `Even Steven rolls at 0 to 3 filled`() {
        val steven = ai("Even Steven")

        assertTrue(steven.rollAgain(slotsWithFilled(3)))
        assertFalse(steven.rollAgain(slotsWithFilled(4)))
    }

    @Test
    fun `Riverboat Ron rolls at 0 to 4 filled`() {
        val ron = ai("Riverboat Ron")

        assertTrue(ron.rollAgain(slotsWithFilled(4)))
        assertFalse(ron.rollAgain(slotsWithFilled(5)))
    }

    @Test
    fun `Sammy Sixes rolls at 0 to 5 filled`() {
        val sammy = ai("Sammy Sixes")

        assertTrue(sammy.rollAgain(slotsWithFilled(5)))
        // there are only 5 fillable slots (slot 6 isn't fillable), so we
        // can't reach 6, but the threshold is documented as <=5
    }

    @Test
    fun `TwoFace always rolls when fewer than 3 filled`() {
        val twoFace = ai("TwoFace")

        assertTrue(twoFace.rollAgain(slotsWithFilled(0)))
        assertTrue(twoFace.rollAgain(slotsWithFilled(2)))
    }

    @Test
    fun `TwoFace never rolls when more than 3 filled`() {
        val twoFace = ai("TwoFace")

        assertFalse(twoFace.rollAgain(slotsWithFilled(4)))
        assertFalse(twoFace.rollAgain(slotsWithFilled(5)))
    }

    @Test
    fun `toPlayer maps AI fields onto Player`() {
        val ai = ai("Fearful Fred")

        val player = ai.toPlayer()

        assertFalse(player.isHuman)
        assert(player.playerName == ai.name)
        assert(player.playerId == ai.aiId)
        assert(player.selectedAI === ai)
    }
}
