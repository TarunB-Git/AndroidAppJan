package com.example.janapps.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.janapps.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ChessWithButtonAndImage(windowSize: WindowWidthSizeClass, janViewModel: JanViewModel = viewModel(), onNextButtonClicked: () -> Unit = {}, onPrevButtonClicked: () -> Unit = {}, modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf(1) }
    var score by rememberSaveable { mutableStateOf(0) }
    var visible by remember { mutableStateOf(true) }
    var isButtonLocked by remember { mutableStateOf(false) }
    var imageResource = when (result) {
        in 1..10 -> R.drawable.king_white
        in 2..20 -> R.drawable.bishop_white
        in 3..30 -> R.drawable.queen_white
        in 4..40 -> R.drawable.knight_white
        in 5..50 -> R.drawable.rook_white
        in 6..60 -> R.drawable.pawn_white
        in 7..70 -> R.drawable.king_black
        in 8..80 -> R.drawable.bishop_black
        in 9..90 -> R.drawable.queen_black
        in 10..100 -> R.drawable.knight_black
        in 11..110 -> R.drawable.rook_black
        in 12..120 -> R.drawable.pawn_black
        else -> R.drawable.ee
    }
    LaunchedEffect(imageResource){
    when (imageResource){
        R.drawable.ee -> {janViewModel.score(value = 50)
            delay(1000)
            isButtonLocked = true}
        R.drawable.king_white or R.drawable.king_black -> janViewModel.score(value = 10)
        R.drawable.queen_white or R.drawable.queen_black -> janViewModel.score(value = 9)
        R.drawable.rook_white or R.drawable.rook_black -> janViewModel.score(value = 5)
        R.drawable.bishop_white or R.drawable.bishop_black or R.drawable.knight_white or R.drawable.knight_black -> janViewModel.score(value = 3)
        R.drawable.pawn_white or R.drawable.pawn_black -> janViewModel.score(value = 1)
    }}
    if (imageResource == R.drawable.ee){
        Text(
            "WOOO"
        )
        janViewModel.score()
        isButtonLocked = true
    }
    if (isButtonLocked || score == 500){
        imageResource = R.drawable.ee
    }
    Text(
        "Rolls :%d".format(score)
    )
    val coroutineScope = rememberCoroutineScope()  // Remember the coroutine scope

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (windowSize) {
            WindowWidthSizeClass.Compact -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 500))
                    ) {
                        Image(
                            painter = painterResource(imageResource),
                            contentDescription = result.toString()
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = {
                        if(!isButtonLocked){
                        visible = false
                        coroutineScope.launch {
                            delay(200)
                            score ++
                            result = (0..120).random()
                            visible = true
                        }}
                    }) {
                        Text(stringResource(R.string.roll))
                    }
                }
            }

            else -> {
                // Landscape or other orientations
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 500))
                    ) {
                        Image(
                            painter = painterResource(imageResource),
                            contentDescription = result.toString(),
                            modifier = Modifier.weight(1f)  // Allow image to take up available space
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        onClick = {
                            if(!isButtonLocked) {
                                visible = false
                                coroutineScope.launch {
                                    delay(200)
                                    score ++
                                    result = (0..120).random()
                                    visible = true
                                }
                            }
                        }
                    ) {
                        Text(stringResource(R.string.roll))
                    }
                }
            }
        }

        // Navigation buttons
        NavigationButtons(
            windowSize = windowSize,
            onPrevButtonClicked = onPrevButtonClicked,
            onNextButtonClicked = onNextButtonClicked
        )
    }
}