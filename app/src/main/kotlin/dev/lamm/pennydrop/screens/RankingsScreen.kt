package dev.lamm.pennydrop.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.lamm.pennydrop.R
import dev.lamm.pennydrop.components.EmptyState
import dev.lamm.pennydrop.types.PlayerSummary

@Composable
fun RankingsScreen(summaries: List<PlayerSummary>) {
    if (summaries.isEmpty()) {
        EmptyState(
            icon = painterResource(R.drawable.ic_baseline_format_list_numbered_24),
            title = stringResource(R.string.no_rankings_title),
            description = stringResource(R.string.no_rankings_hint)
        )
        return
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(summaries, key = { it.id }) { summary ->
            PlayerSummaryRow(summary)
            HorizontalDivider()
        }
    }
}

@Composable
private fun PlayerSummaryRow(summary: PlayerSummary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                id = if (summary.isHuman) R.drawable.ic_baseline_face_24
                else R.drawable.ic_baseline_android_24
            ),
            contentDescription = stringResource(R.string.player_type_image),
            tint = if (summary.isHuman) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .size(56.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = summary.name,
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "${summary.gamesPlayed} ${stringResource(R.string.games_played)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Text(
                text = summary.winsString,
                fontSize = 32.sp
            )
            Text(
                text = pluralStringResource(R.plurals.wins, summary.wins).uppercase(),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview
@Composable
private fun RankingsScreenPreview() {
    RankingsScreen(
        summaries = listOf(
            PlayerSummary(1, "Michael", gamesPlayed = 23, wins = 10, isHuman = true),
            PlayerSummary(2, "Eddie", gamesPlayed = 18, wins = 7, isHuman = false),
            PlayerSummary(3, "TwoFace", gamesPlayed = 12, wins = 3, isHuman = false)
        )
    )
}
