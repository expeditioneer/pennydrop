package dev.lamm.pennydrop.game

import dev.lamm.pennydrop.types.Player
import dev.lamm.pennydrop.types.Slot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameHandlerTest {

    private fun player(id: Long, name: String = "P$id", pennies: Int = 10) =
        Player(playerId = id, playerName = name).apply { this.pennies = pennies }

    private fun emptySlots(): List<Slot> =
        (1..6).map { Slot(number = it, canBeFilled = it != 6) }

    private fun allFilledSlots(): List<Slot> =
        (1..6).map { Slot(number = it, canBeFilled = it != 6, isFilled = true) }

    @Test
    fun `pass rotates to next player and ends turn`() {
        val p1 = player(1)
        val p2 = player(2)

        val result = GameHandler.pass(listOf(p1, p2), p1)

        assertEquals(p1, result.previousPlayer)
        assertEquals(p2, result.currentPlayer)
        assertTrue(result.playerChanged)
        assertEquals(TurnEnd.Pass, result.turnEnd)
        assertTrue(result.canRoll)
        assertFalse(result.canPass)
    }

    @Test
    fun `pass wraps around from last player to first`() {
        val p1 = player(1)
        val p2 = player(2)
        val p3 = player(3)

        val result = GameHandler.pass(listOf(p1, p2, p3), p3)

        assertEquals(p1, result.currentPlayer)
    }

    @Test
    fun `roll into already-filled slot busts and clears`() {
        val p1 = player(1)
        val p2 = player(2)

        // every slot filled -> every possible roll lands on a filled slot
        val result = GameHandler.roll(listOf(p1, p2), p1, allFilledSlots())

        assertEquals(TurnEnd.Bust, result.turnEnd)
        assertTrue(result.clearSlots)
        assertEquals(p1, result.previousPlayer)
        assertEquals(p2, result.currentPlayer)
        assertTrue(result.playerChanged)
        assertTrue(result.coinChangeCount!! > 0)
    }

    @Test
    fun `roll into empty slot drops one penny and continues turn`() {
        val p1 = player(1)
        val p2 = player(2)

        val result = GameHandler.roll(listOf(p1, p2), p1, emptySlots())

        assertEquals(-1, result.coinChangeCount)
        assertEquals(p1, result.currentPlayer)
        assertFalse(result.playerChanged)
        assertTrue(result.canRoll)
        assertTrue(result.canPass)
        assertFalse(result.isGameOver)
    }

    @Test
    fun `last penny dropping into empty slot wins the game`() {
        val p1 = player(1, pennies = 1)
        val p2 = player(2)

        val result = GameHandler.roll(listOf(p1, p2), p1, emptySlots())

        assertTrue(result.isGameOver)
        assertEquals(TurnEnd.Win, result.turnEnd)
        assertEquals(p1, result.currentPlayer)
        assertFalse(result.canRoll)
        assertFalse(result.canPass)
    }

    @Test
    fun `playAITurn passes when AI says don't roll again and canPass`() {
        val ai = AI(99, "Always Passes") { false }
        val aiPlayer = player(1).apply { isHuman = false; selectedAI = ai }
        val human = player(2)

        val result = GameHandler.playAITurn(
            players = listOf(aiPlayer, human),
            currentPlayer = aiPlayer,
            slots = emptySlots(),
            canPass = true
        )

        assertNotNull(result)
        assertEquals(TurnEnd.Pass, result!!.turnEnd)
    }

    @Test
    fun `playAITurn rolls when AI says roll again`() {
        val ai = AI(99, "Always Rolls") { true }
        val aiPlayer = player(1).apply { isHuman = false; selectedAI = ai }
        val human = player(2)

        val result = GameHandler.playAITurn(
            players = listOf(aiPlayer, human),
            currentPlayer = aiPlayer,
            slots = emptySlots(),
            canPass = true
        )

        assertNotNull(result)
        // empty slots -> guaranteed -1 coinChangeCount, never bust
        assertEquals(-1, result!!.coinChangeCount)
    }

    @Test
    fun `playAITurn must roll when canPass is false regardless of AI`() {
        val ai = AI(99, "Wants To Pass") { false }
        val aiPlayer = player(1).apply { isHuman = false; selectedAI = ai }
        val human = player(2)

        val result = GameHandler.playAITurn(
            players = listOf(aiPlayer, human),
            currentPlayer = aiPlayer,
            slots = emptySlots(),
            canPass = false
        )

        // even though AI prefers pass, canPass=false forces a roll
        assertNotNull(result)
        assertEquals(-1, result!!.coinChangeCount)
    }

    @Test
    fun `playAITurn returns null when current player has no AI`() {
        val human1 = player(1)
        val human2 = player(2)

        val result = GameHandler.playAITurn(
            players = listOf(human1, human2),
            currentPlayer = human1,
            slots = emptySlots()
        )

        assertEquals(null, result)
    }
}
