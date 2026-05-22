package dev.lamm.pennydrop.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.lamm.pennydrop.R
import dev.lamm.pennydrop.game.AI
import dev.lamm.pennydrop.types.NewPlayer

private enum class PlayerMode { Off, Human, Ai }

private val NewPlayer.mode: PlayerMode
    get() = when {
        !isIncluded -> PlayerMode.Off
        isHuman -> PlayerMode.Human
        else -> PlayerMode.Ai
    }

@Composable
fun PickPlayersScreen(
    players: List<NewPlayer>,
    onUpdatePlayer: (Int, (NewPlayer) -> NewPlayer) -> Unit,
    onPlayClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            players.forEachIndexed { index, player ->
                PlayerCard(
                    index = index,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerCard(
    index: Int,
    player: NewPlayer,
    onPlayerChanged: ((NewPlayer) -> NewPlayer) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.player_default_name, index + 1),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = player.mode == PlayerMode.Off,
                    onClick = { onPlayerChanged { it.copy(isIncluded = false) } },
                    enabled = player.canBeRemoved,
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
                ) { Text(stringResource(R.string.player_off)) }

                SegmentedButton(
                    selected = player.mode == PlayerMode.Human,
                    onClick = { onPlayerChanged { it.copy(isIncluded = true, isHuman = true) } },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
                ) { Text(stringResource(R.string.player_human)) }

                SegmentedButton(
                    selected = player.mode == PlayerMode.Ai,
                    onClick = {
                        onPlayerChanged {
                            it.copy(
                                isIncluded = true,
                                isHuman = false,
                                selectedAIPosition = if (it.selectedAIPosition >= 0) it.selectedAIPosition else 0
                            )
                        }
                    },
                    enabled = player.canBeToggled,
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                ) { Text(stringResource(R.string.player_ai)) }
            }

            AnimatedVisibility(visible = player.isIncluded) {
                when (player.mode) {
                    PlayerMode.Human -> NameField(player = player, onPlayerChanged = onPlayerChanged)
                    PlayerMode.Ai -> AiDropdown(player = player, onPlayerChanged = onPlayerChanged)
                    PlayerMode.Off -> Unit
                }
            }
        }
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
        label = { Text(stringResource(R.string.player_name)) },
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
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedAiName,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.player_ai)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
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
