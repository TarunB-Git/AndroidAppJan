package com.example.janapps.ui.theme

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.janapps.R
import com.example.janapps.data.Datasource
import com.example.janapps.model.ListLazy


@Composable
fun ListApp(windowSize: WindowWidthSizeClass, onNextButtonClicked: () -> Unit = {}, Secret : () -> Unit, onPrevButtonClicked: () -> Unit = {}){
    Scaffold(
        content = { paddingValues ->
            LazyList(
                llist = Datasource().loadLists(),
                Secret = {Secret()},
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )

        }
    )
    NavigationButtons(
        windowSize = windowSize,
        onPrevButtonClicked = onPrevButtonClicked,
        onNextButtonClicked = onNextButtonClicked)
}


@Composable
private fun ItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ){
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.secret),
            tint = MaterialTheme.colorScheme.secondary
        )
    }

}

@Composable
fun Hobby(
    @StringRes hobby: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Column(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = stringResource(R.string.about),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(R.string.secret),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun LazyList( llist: List<ListLazy>, Secret : () -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(llist) { listItem ->
            ListCard(
                list = listItem,
                modifier = Modifier.padding(8.dp),
                        Secret = {Secret()}
            )
        }
    }
}

@Composable
fun ListCard(list: ListLazy, modifier: Modifier = Modifier, Secret : () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val color by animateColorAsState(targetValue = if (expanded) MaterialTheme.colorScheme.tertiaryContainer
    else MaterialTheme.colorScheme.primaryContainer)
    Card(modifier = modifier) {
        Column (
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                .background(color = color)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                val painter = rememberImagePainter(list.imageResourceId)
                Image(
                    painter = painter,
                    contentDescription = stringResource(list.stringResourceId),
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.image_size))
                        .padding(dimensionResource(R.dimen.padding_small))
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Fit
                )
                Column {
                    Text(
                        text = LocalContext.current.getString(list.stringResourceId2),
                        modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)),
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        LocalContext.current.getString(list.stringResourceId),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                ItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
            }
            if(expanded) {
                    Hobby(
                        list.hobbies,
                        onClick = {Secret()},
                        modifier = Modifier.padding(
                            start = dimensionResource(R.dimen.padding_medium),
                            top = dimensionResource(R.dimen.padding_small),
                            end = dimensionResource(R.dimen.padding_medium),
                            bottom = dimensionResource(R.dimen.padding_medium)
                        ))
            }

        }
    }
}


