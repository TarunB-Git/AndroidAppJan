package com.example.janapps.ui.theme

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.janapps.R

@Preview
@Composable
fun ReplyPreview() {
    JanAppsTheme (darkTheme = true) {
        ReplyDetailsScreen(
            doSumn = {},
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        )    }
}

@Composable
fun ReplyDetailsScreen(
    doSumn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyColumn(
            contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            item {
                ReplyDetailsScreenTopBar(
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = dimensionResource(R.dimen.padding_small),
                            top = dimensionResource(R.dimen.padding_medium)
                        )
                )
                ReplyEmailDetailsCard(
                    doSumn = {doSumn()
                        Log.d("ReplyDetailsScreen", "doSumn invoked from Card")
                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(horizontal = dimensionResource(R.dimen.padding_small))
                )
            }
        }
    }
}

@Composable
private fun ReplyDetailsScreenTopBar(
    modifier: Modifier = Modifier
) {
    var expanded = remember { mutableStateOf(false) }
    val iconSize = animateDpAsState(
        targetValue = if (expanded.value) 100000.dp else 50.dp, // change size between 50dp and 100dp
        animationSpec = tween(durationMillis = 300000) // smooth animation
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {  expanded.value = !expanded.value },
            modifier = Modifier
                .size(iconSize.value)
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                .background(MaterialTheme.colorScheme.surface, shape = CircleShape),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.hobby4)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                text = stringResource(R.string.Desc),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ReplyEmailDetailsCard(
    modifier: Modifier = Modifier,
    doSumn: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            DetailsScreenHeader(
                Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.buttons),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.padding_small),
                    bottom = dimensionResource(R.dimen.padding_medium)
                ),
            )
            Text(
                text = stringResource(R.string.three_buttons),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            DetailsScreenButtonBar(doSumn = {doSumn()})
        }
    }
}

@Composable
private fun DetailsScreenButtonBar(
    doSumn : () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(modifier = modifier) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = dimensionResource(R.dimen.padding_small)
                        ),
                    horizontalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.padding_medium)
                    ),
                ) {
                    ActionButton(
                        text = "Bug",
                        onButtonClicked = doSumn,
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        text = "Out",
                        onButtonClicked = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    )
        }
    }
}

@Composable
private fun DetailsScreenHeader( modifier: Modifier = Modifier) {
    Row(modifier = modifier) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_small),
                    vertical = dimensionResource(R.dimen.padding_small)
                ),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.one_button),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = stringResource(R.string.risk),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    containIrreversibleAction: Boolean = false,
) {
    Box(modifier = modifier) {
        Button(
            onClick = { onButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.image_size)),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (containIrreversibleAction) {
                    MaterialTheme.colorScheme.onErrorContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                }
            )
        ) {
            Text(
                text = text,
                color =
                if (containIrreversibleAction) {
                    MaterialTheme.colorScheme.onError
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}