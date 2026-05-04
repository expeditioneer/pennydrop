package dev.lamm.pennydrop.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.lamm.pennydrop.R
import dev.lamm.pennydrop.types.Slot

@Composable
fun GameScreen(
    slots: List<Slot>,
    currentPlayerName: String,
    coinsLeft: Int,
    turnInfo: String,
    standings: String,
    canRoll: Boolean,
    canPass: Boolean,
    onRoll: () -> Unit,
    onPass: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 5.dp),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentPlayerName,
                fontSize = 32.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 32.dp)
            ) {
                Text(text = coinsLeft.toString(), fontSize = 24.sp)
                Text(
                    text = stringResource(R.string.coins_left),
                    fontSize = 12.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
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
                fontSize = 24.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .verticalScroll(rememberScrollState())
        )

        Text(
            text = standings,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun CoinSlotView(
    slot: Slot,
    modifier: Modifier = Modifier
) {
    val highlight = MaterialTheme.colorScheme.primary
    val baseLine = Color.Black
    val baseText = Color.Black

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(36.dp),
            contentAlignment = Alignment.Center
        ) {
            if (slot.canBeFilled && slot.isFilled) {
                Icon(
                    painter = painterResource(R.drawable.mdi_coin_black_24dp),
                    contentDescription = stringResource(R.string.coin_icon),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .background(if (slot.lastRolled) highlight else baseLine)
        )

        Text(
            text = slot.number.toString(),
            fontSize = 24.sp,
            color = if (slot.lastRolled) highlight else baseText,
            fontWeight = if (slot.lastRolled) FontWeight.Bold else FontWeight.Normal
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
            slots = slots,
            currentPlayerName = "Michael",
            coinsLeft = 7,
            turnInfo = "Michael rolled a 3.\nMichael rolled a 4!",
            standings = "Current Standings:\n  Michael - 7 pennies\n  Hazel - 9 pennies",
            canRoll = true,
            canPass = true,
            onRoll = {},
            onPass = {}
        )
    }
}
