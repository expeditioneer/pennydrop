package dev.lamm.pennydrop.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.lamm.pennydrop.R

@Preview
@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CreatedByText()
        DescriptionText()
        IconsText()
        Images()
    }
}

@Composable
private fun CreatedByText() {
    Text(
        text = AnnotatedString.fromHtml(
            htmlString = stringResource(id = R.string.penny_drop_created_by),
            linkStyles = TextLinkStyles(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    fontStyle = FontStyle.Italic
                )
            )
        ),
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun DescriptionText() {
    Text(
        text = AnnotatedString.fromHtml(
            htmlString = stringResource(id = R.string.penny_drop_description)
        ),
        style = MaterialTheme.typography.bodyLarge,
        fontStyle = FontStyle.Italic,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun IconsText() {
    Text(
        text = AnnotatedString.fromHtml(
            htmlString = stringResource(id = R.string.penny_drop_icons),
            linkStyles = TextLinkStyles(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    fontStyle = FontStyle.Italic
                )
            )
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun Images() {
    val tint = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Image(
            painter = painterResource(id = R.drawable.mdi_coin_black_24dp),
            contentDescription = stringResource(id = R.string.coin_icon),
            contentScale = ContentScale.FillHeight,
            colorFilter = tint,
            modifier = Modifier
                .defaultMinSize(minHeight = 52.dp)
                .weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.mdi_dice_6_black_24dp),
            contentDescription = stringResource(id = R.string.dice_icon),
            contentScale = ContentScale.FillHeight,
            colorFilter = tint,
            modifier = Modifier
                .defaultMinSize(minHeight = 52.dp)
                .weight(1f)
        )
    }
}
