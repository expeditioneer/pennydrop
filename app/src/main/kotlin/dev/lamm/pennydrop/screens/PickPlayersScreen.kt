package dev.lamm.pennydrop.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.lamm.pennydrop.R
import dev.lamm.pennydrop.game.AI
import dev.lamm.pennydrop.types.NewPlayer

@Composable
fun PickPlayersScreen(
    players: List<NewPlayer>,
    onUpdatePlayer: (Int, (NewPlayer) -> NewPlayer) -> Unit,
    onPlayClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            players.forEachIndexed { index, player ->
                PlayerRow(
                    player = player,
                    onPlayerChanged = { transform -> onUpdatePlayer(index, transform) }
                )
            }
        }

        FloatingActionButton(
            onClick = onPlayClicked,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_play_arrow_24),
                contentDescription = stringResource(R.string.play_button)
            )
        }
    }
}

@Composable
private fun PlayerRow(
    player: NewPlayer,
    onPlayerChanged: ((NewPlayer) -> NewPlayer) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = player.isIncluded,
            onCheckedChange = { checked ->
                onPlayerChanged { it.copy(isIncluded = checked) }
            },
            enabled = player.canBeRemoved,
            modifier = Modifier.alpha(if (player.canBeRemoved) 1f else 0f)
        )

        Box(modifier = Modifier.weight(1f)) {
            if (player.isHuman) {
                NameField(player = player, onPlayerChanged = onPlayerChanged)
            } else {
                AiDropdown(player = player, onPlayerChanged = onPlayerChanged)
            }
        }

        Switch(
            checked = player.isHuman,
            onCheckedChange = { checked ->
                onPlayerChanged { it.copy(isHuman = checked) }
            },
            enabled = player.isIncluded && player.canBeToggled,
            modifier = Modifier
                .padding(start = 8.dp)
                .alpha(if (player.canBeToggled) 1f else 0f)
        )
    }
}

@Composable
private fun NameField(
    player: NewPlayer,
    onPlayerChanged: ((NewPlayer) -> NewPlayer) -> Unit
) {
    OutlinedTextField(
        value = player.playerName,
        onValueChange = { value ->
            onPlayerChanged { it.copy(playerName = value) }
        },
        enabled = player.isIncluded,
        placeholder = { Text(stringResource(R.string.player_name)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AiDropdown(
    player: NewPlayer,
    onPlayerChanged: ((NewPlayer) -> NewPlayer) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedAiName = AI.basicAI.getOrNull(player.selectedAIPosition)?.name.orEmpty()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (player.isIncluded) expanded = it }
    ) {
        OutlinedTextField(
            value = selectedAiName,
            onValueChange = {},
            readOnly = true,
            enabled = player.isIncluded,
            placeholder = { Text(stringResource(R.string.player_name)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = player.isIncluded)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            AI.basicAI.forEachIndexed { index, ai ->
                DropdownMenuItem(
                    text = { Text(ai.name) },
                    onClick = {
                        onPlayerChanged { it.copy(selectedAIPosition = index) }
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PickPlayersScreenPreview() {
    val players = listOf(
        NewPlayer(playerName = "Dennis", canBeRemoved = false, canBeToggled = false, isIncluded = true),
        NewPlayer(playerName = "", isHuman = false, canBeRemoved = false, isIncluded = true, selectedAIPosition = 0),
        NewPlayer(canBeRemoved = true, canBeToggled = true),
        NewPlayer(canBeRemoved = true, canBeToggled = true),
        NewPlayer(canBeRemoved = true, canBeToggled = true),
        NewPlayer(canBeRemoved = true, canBeToggled = true)
    )
    MaterialTheme {
        PickPlayersScreen(
            players = players,
            onUpdatePlayer = { _, _ -> },
            onPlayClicked = {}
        )
    }
}
