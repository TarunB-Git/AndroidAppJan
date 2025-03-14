package com.example.janapps.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.janapps.R
import com.example.janapps.data.RaceParticipant
import com.example.janapps.data.pauseForOneSecond
import com.example.janapps.data.progressFactor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class Player {
    PLAYER_ONE, PLAYER_TWO
}

@Composable
fun RaceTrackerApp(windowSize: WindowWidthSizeClass, onNextButtonClicked: () -> Unit = {}, onPrevButtonClicked: () -> Unit = {}) {
    val playerOne = remember {
        RaceParticipant(name = "Player 1", progressIncrement = 2)
    }
    val playerTwo = remember {
        RaceParticipant(name = "Player 2", progressIncrement = 5)
    }
    var raceInProgress by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }

    if (raceInProgress) {
        LaunchedEffect(playerOne, playerTwo) {
            coroutineScope {
                launch { playerOne.run() }
                launch { playerTwo.run() }
            }
            raceInProgress = false
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (selectedPlayer == null) {
            Text("Select a Player to Race")
            Button(onClick = { selectedPlayer = Player.PLAYER_ONE }) {
                Text("Select Player 1")
            }
            Button(onClick = { selectedPlayer = Player.PLAYER_TWO
                playerOne.progressIncrement = 6}) {
                Text("Select Player 2")
            }
        } else {
            RaceTrackerScreen(
                playerOne = playerOne,
                playerTwo = playerTwo,
                isRunning = raceInProgress,
                onRunStateChange = { raceInProgress = it },
                selectedPlayer = selectedPlayer,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
    NavigationButtons(
        windowSize = windowSize,
        onPrevButtonClicked = onPrevButtonClicked,
        onNextButtonClicked = onNextButtonClicked
    )
}

@Composable
private fun RaceTrackerScreen(
    playerOne: RaceParticipant,
    playerTwo: RaceParticipant,
    isRunning: Boolean,
    onRunStateChange: (Boolean) -> Unit,
    selectedPlayer: Player?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.run_a_race),
            style = MaterialTheme.typography.headlineSmall,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(R.drawable.download),
                contentDescription = null,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            )
            StatusIndicator(
                participantName = playerOne.name,
                currentProgress = playerOne.currentProgress,
                maxProgress = stringResource(
                    R.string.progress_percentage,
                    playerOne.maxProgress
                ),
                progressFactor = playerOne.progressFactor,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
            StatusIndicator(
                participantName = playerTwo.name,
                currentProgress = playerTwo.currentProgress,
                maxProgress = stringResource(
                    R.string.progress_percentage,
                    playerTwo.maxProgress
                ),
                progressFactor = playerTwo.progressFactor,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
            RaceControls(
                isRunning = isRunning,
                onRunStateChange = onRunStateChange,
                onReset = {
                    playerOne.reset()
                    playerTwo.reset()
                    if(selectedPlayer == Player.PLAYER_ONE) {
                        playerOne.progressIncrement = 2
                        playerTwo.progressIncrement = 5
                    }
                    else{
                        playerOne.progressIncrement = 6
                        playerTwo.progressIncrement = 5
                    }
                    onRunStateChange(false)
                },
                selectedPlayer = selectedPlayer,
                playerOne = playerOne,
                playerTwo = playerTwo
            )
            SpeedControl(
                playerOne = playerOne,
                playerTwo = playerTwo,
                selectedPlayer = selectedPlayer,
                raceInProgress = isRunning
            )
        }
    }
}


@Composable
private fun SpeedControl(
    playerOne: RaceParticipant,
    playerTwo: RaceParticipant,
    selectedPlayer: Player?,
    raceInProgress: Boolean
) {
    var buffDegree by remember { mutableStateOf(1.0f) }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (raceInProgress) {
                    when (selectedPlayer) {
                        Player.PLAYER_ONE -> {
                            buffDegree = playerOne.applyRandomSpeedMultiplier(2.0F, 0.8F)
                        }
                        Player.PLAYER_TWO -> {
                            buffDegree = playerTwo.applyRandomSpeedMultiplier(1.2F, 0.9F)
                        }
                        null -> {}
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = raceInProgress
        ) {
            Text("Buff Selected Player")
        }

        // Display the degree of the buff applied
        Text("Buff Degree: %.2f".format(buffDegree))
    }
}


fun RaceParticipant.applyRandomSpeedMultiplier(start: Float, end: Float, increase: Boolean = true): Float {
    val multiplier = Random.nextFloat() * (start - end) + end
    val newProgressIncrement = if (increase) progressIncrement * multiplier else progressIncrement / multiplier
    this.progressIncrement = newProgressIncrement.toInt()
    return multiplier
}



@Composable
private fun StatusIndicator(
    participantName: String,
    currentProgress: Int,
    maxProgress: String,
    progressFactor: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = participantName,
            modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_small))
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            LinearProgressIndicator(
                progress = progressFactor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.padding_small))
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium)))
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.progress_percentage, currentProgress),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = maxProgress,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RaceControls(
    onRunStateChange: (Boolean) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
    isRunning: Boolean = true,
    selectedPlayer: Player?,
    playerOne: RaceParticipant,
    playerTwo: RaceParticipant
) {
    var hasPausedOnce by remember { mutableStateOf(false) }
    var pauseTrigger by remember { mutableStateOf(false) }

    LaunchedEffect(pauseTrigger) {
        if (pauseTrigger) {
            if (selectedPlayer == Player.PLAYER_ONE) {
                playerTwo.pauseForOneSecond()
            } else {
                playerOne.pauseForOneSecond()
            }
        }
    }

    Column(
        modifier = modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        Button(
            onClick = {
                if (isRunning) {
                    onRunStateChange(false)
                    if (selectedPlayer != null && !hasPausedOnce) {
                        pauseTrigger = true
                        hasPausedOnce = true
                    }
                } else {
                    onRunStateChange(true)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (isRunning) stringResource(R.string.pause) else stringResource(R.string.start))
        }

        OutlinedButton(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.reset))
        }
    }
}




