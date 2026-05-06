package dev.lamm.pennydrop.screens

import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.lamm.pennydrop.R

@Composable
fun SettingsScreen(
    prefs: SharedPreferences,
    onThemeModeChanged: (String) -> Unit,
    onCreditsClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp)
    ) {
        CategoryHeader(stringResource(R.string.category_game_settings))
        SliderPreference(
            title = stringResource(R.string.starting_penny_count),
            summary = stringResource(R.string.starting_penny_count_new_game),
            min = 1,
            max = 20,
            initial = prefs.getInt("pennyCount", 10),
            onChange = { prefs.edit().putInt("pennyCount", it).apply() }
        )
        SwitchPreference(
            title = stringResource(R.string.fast_ai),
            summary = stringResource(R.string.fast_ai_summary),
            initial = prefs.getBoolean("fastAI", false),
            onChange = { prefs.edit().putBoolean("fastAI", it).apply() }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        CategoryHeader(stringResource(R.string.category_theme_settings))
        DropdownPreference(
            title = stringResource(R.string.theme_mode),
            entries = stringArrayResource(R.array.theme_modes).toList(),
            values = stringArrayResource(R.array.theme_mode_values).toList(),
            initialValue = prefs.getString("themeMode", "System") ?: "System",
            onChange = { value ->
                prefs.edit().putString("themeMode", value).apply()
                onThemeModeChanged(value)
            }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        CategoryHeader(stringResource(R.string.category_about))
        ClickablePreference(
            title = stringResource(R.string.about_penny_drop),
            summary = stringResource(R.string.about_app_summary),
            onClick = onCreditsClicked
        )
    }
}

@Composable
private fun CategoryHeader(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SliderPreference(
    title: String,
    summary: String,
    min: Int,
    max: Int,
    initial: Int,
    onChange: (Int) -> Unit
) {
    var value by remember { mutableFloatStateOf(initial.toFloat()) }
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = value.toInt().toString(), style = MaterialTheme.typography.titleMedium)
        }
        Text(text = summary, style = MaterialTheme.typography.bodySmall)
        Slider(
            value = value,
            onValueChange = { value = it },
            onValueChangeFinished = { onChange(value.toInt()) },
            valueRange = min.toFloat()..max.toFloat(),
            steps = (max - min) - 1
        )
    }
}

@Composable
private fun SwitchPreference(
    title: String,
    summary: String,
    initial: Boolean,
    onChange: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(initial) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                checked = !checked
                onChange(checked)
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = summary, style = MaterialTheme.typography.bodySmall)
        }
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                onChange(it)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownPreference(
    title: String,
    entries: List<String>,
    values: List<String>,
    initialValue: String,
    onChange: (String) -> Unit
) {
    var selected by remember { mutableStateOf(initialValue) }
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = entries.getOrNull(values.indexOf(selected)) ?: selected

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedLabel,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                values.forEachIndexed { index, value ->
                    DropdownMenuItem(
                        text = { Text(entries.getOrNull(index) ?: value) },
                        onClick = {
                            selected = value
                            expanded = false
                            onChange(value)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ClickablePreference(
    title: String,
    summary: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(text = summary, style = MaterialTheme.typography.bodySmall)
    }
}
