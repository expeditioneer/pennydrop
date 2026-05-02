package dev.lamm.pennydrop.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.sp
import dev.lamm.pennydrop.R

@Preview
@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CreatedByText()

        DescriptionText()

        IconsText()

        Images()
    }
}

@Composable
fun CreatedByText() {
    return Text(
        AnnotatedString.fromHtml(
            htmlString = stringResource(id = R.string.penny_drop_created_by),
            linkStyles = TextLinkStyles(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    fontStyle = FontStyle.Italic
                )
            )
        ),
        fontSize = 24.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}

@Composable
fun DescriptionText() {
    Text(
        text = AnnotatedString.fromHtml(
            htmlString = stringResource(id = R.string.penny_drop_description)
        ),
        fontStyle = FontStyle.Italic,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    )
}


@Composable
fun IconsText() {
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
        fontSize = 18.sp
    )
}

@Composable
fun Images() {
    Row {
        Image(
            painter = painterResource(id = R.drawable.mdi_coin_black_24dp),
            contentDescription = stringResource(id = R.string.coin_icon),
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .padding(top = 16.dp)
                .defaultMinSize(minHeight = 52.dp)
                .weight(1f)
        )

        Image(
            painter = painterResource(id = R.drawable.mdi_dice_6_black_24dp),
            contentDescription = stringResource(id = R.string.dice_icon),
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .padding(top = 16.dp)
                .defaultMinSize(minHeight = 52.dp)
                .weight(1f)
        )
    }
}
