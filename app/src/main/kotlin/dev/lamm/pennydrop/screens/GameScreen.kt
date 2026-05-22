package dev.lamm.pennydrop.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.lamm.pennydrop.R
import dev.lamm.pennydrop.types.Slot

@Composable
fun GameScreen(
    isGameActive: Boolean,
    slots: List<Slot>,
    currentPlayerName: String,
    coinsLeft: Int,
    turnInfo: String,
    standings: String,
    canRoll: Boolean,
    canPass: Boolean,
    onRoll: () -> Unit,
    onPass: () -> Unit,
    onPickPlayers: () -> Unit
) {
    if (!isGameActive) {
        NoActiveGame(onPickPlayers = onPickPlayers)
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            slots.take(5).forEach { slot ->
                CoinSlotView(slot = slot, modifier = Modifier.weight(1f))
            }
        }

        slots.getOrNull(5)?.let { slot ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CoinSlotView(slot = slot)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentPlayerName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = coinsLeft.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = stringResource(R.string.coins_left),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onRoll,
                enabled = canRoll,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.weight(3f)
            ) {
                Text(text = stringResource(R.string.roll))
                Icon(
                    painter = painterResource(R.drawable.mdi_dice_6_black_24dp),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(
                text = stringResource(R.string.or),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = onPass,
                enabled = canPass,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier.weight(3f)
            ) {
                Text(text = stringResource(R.string.pass))
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_arrow_forward_24),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Text(
            text = turnInfo,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        )

        Text(
            text = standings,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun NoActiveGame(onPickPlayers: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.mdi_dice_6_black_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(72.dp)
        )
        Text(
            text = stringResource(R.string.no_game_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = stringResource(R.string.no_game_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        FilledTonalButton(onClick = onPickPlayers) {
            Text(text = stringResource(R.string.pick_players))
        }
    }
}

@Composable
private fun CoinSlotView(
    slot: Slot,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val filled = slot.canBeFilled && slot.isFilled
    val highlighted = slot.lastRolled

    val containerColor = if (filled) cs.primaryContainer else cs.surfaceVariant
    val borderColor = if (highlighted) cs.primary else cs.outlineVariant
    val borderWidth = if (highlighted) 2.dp else 1.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = containerColor,
            border = BorderStroke(borderWidth, borderColor),
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (filled) {
                    Icon(
                        painter = painterResource(R.drawable.mdi_coin_black_24dp),
                        contentDescription = stringResource(R.string.coin_icon),
                        tint = cs.onPrimaryContainer
                    )
                }
            }
        }
        Text(
            text = slot.number.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = if (highlighted) cs.primary else cs.onSurfaceVariant,
            fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview
@Composable
private fun GameScreenPreview() {
    val slots = listOf(
        Slot(1, canBeFilled = true, isFilled = false, lastRolled = false),
        Slot(2, canBeFilled = true, isFilled = true, lastRolled = false),
        Slot(3, canBeFilled = true, isFilled = false, lastRolled = true),
        Slot(4, canBeFilled = true, isFilled = true, lastRolled = false),
        Slot(5, canBeFilled = true, isFilled = false, lastRolled = false),
        Slot(6, canBeFilled = false, isFilled = false, lastRolled = false)
    )
    MaterialTheme {
        GameScreen(
            isGameActive = true,
            slots = slots,
            currentPlayerName = "Michael",
            coinsLeft = 7,
            turnInfo = "Michael rolled a 3.\nMichael rolled a 4!",
            standings = "Current Standings:\n  Michael - 7 pennies\n  Hazel - 9 pennies",
            canRoll = true,
            canPass = true,
            onRoll = {},
            onPass = {},
            onPickPlayers = {}
        )
    }
}
