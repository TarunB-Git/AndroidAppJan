package com.example.janapps.ui.theme

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.janapps.R

enum class JanScreens(@StringRes val title: Int) {
    Card(title = R.string.app_name),
    Chess(title = R.string.chess),
    Calc(title = R.string.calc),
    List(title = R.string.List1),
    ListDesc(title = R.string.ListDesc),
    Race(title = R.string.run_a_race)
}

@Preview(showBackground = true)
@Composable
fun ReplyAppCompactPreview() {
    JanAppsTheme {
        SwitchScreen(
                windowSize = WindowWidthSizeClass.Compact,
            )
    }
}
@Preview(showBackground = true)
@Composable
fun Medium() {
    JanAppsTheme {
        SwitchScreen(
            windowSize = WindowWidthSizeClass.Medium,
        )
    }
}
@Preview(showBackground = true)
@Composable
fun Big() {
    JanAppsTheme {
        SwitchScreen(
            windowSize = WindowWidthSizeClass.Expanded
        )
    }
}



@Composable
fun SwitchScreen(janViewModel: JanViewModel = viewModel(),
                 windowSize: WindowWidthSizeClass,
                 navController: NavHostController = rememberNavController()
) {
    val uiState by janViewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = JanScreens.valueOf(
        backStackEntry?.destination?.route ?: JanScreens.Card.name
    )
    val isDarkMode = janViewModel.isDarkMode.collectAsState().value
    JanAppsTheme(darkTheme = when(isDarkMode){
        0,2 -> false
        1,3-> true
        else->false
    }, dynamicColor = when(isDarkMode){
        0,1 -> false
        2,3-> true
        else->false
    } ){
        Scaffold(
            topBar = {
                AppBar(
                    janViewModel = janViewModel,
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    currentScore = uiState.currentScore
                )
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = JanScreens.Card.name,
                modifier = Modifier.padding(innerPadding)
            ) {

                composable(route = JanScreens.Card.name) {
                    val context = LocalContext.current
                    CardImage(
                        message = stringResource(R.string.card_text),
                        outsideModel = janViewModel,
                        from = stringResource(R.string.sign_text),
                        modifier = Modifier.padding(8.dp),
                        currentScore = uiState.currentScore,
                        windowSize = windowSize,
                        onNextButtonClicked = {
                            navController.navigate(JanScreens.Chess.name)
                        },
                        onPrevButtonClicked = {
                            navController.navigate(JanScreens.Race.name)
                        })
                }
                composable(route = JanScreens.Chess.name) {
                    ChessWithButtonAndImage(janViewModel = janViewModel,
                        windowSize = windowSize,
                        onNextButtonClicked = {
                        navController.navigate(JanScreens.Calc.name)
                    },
                        onPrevButtonClicked = {
                            navController.popBackStack(JanScreens.Card.name, inclusive = false)
                        })
                }
                composable(route = JanScreens.Calc.name) {
                    CalcTimeLayout(
                        windowSize = windowSize,
                        onNextButtonClicked = {
                        navController.navigate(JanScreens.List.name)
                    },
                        onPrevButtonClicked = {
                            navController.navigate(JanScreens.Chess.name)
                        })
                }
                composable(route = JanScreens.List.name) {
                    ListApp(
                        windowSize = windowSize,
                        onNextButtonClicked = {
                            navController.navigate(JanScreens.Race.name)
                    },
                        Secret = {navController.navigate(JanScreens.ListDesc.name)},
                        onPrevButtonClicked = {
                            navController.navigate(JanScreens.Calc.name)
                        }
                    )
                }
                composable(route = JanScreens.ListDesc.name) {
                    ReplyDetailsScreen(
                        doSumn = {
                            navController.popBackStack(
                                JanScreens.Card.name,
                                inclusive = true)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                    )
                }
                composable(route = JanScreens.Race.name) {
                    RaceTrackerApp(windowSize = windowSize,
                        onNextButtonClicked = {
                            navController.popBackStack(JanScreens.Card.name, inclusive = false)
                        },
                        onPrevButtonClicked = {
                            navController.navigate(JanScreens.List.name)
                        })
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    janViewModel: JanViewModel,
    currentScreen: JanScreens,
    currentScore : Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    TopAppBar(
        title = { Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(currentScreen.title))
        }},
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },                actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp), // Adjust the spacing between icons
                modifier = Modifier.padding(end = 16.dp) // Optional: add some padding to the end
            ) {
                IconButton(
                    onClick = { janViewModel.toggleDarkMode() }
                ) {
                    Icon(imageVector = Icons.Filled.Brightness6, contentDescription = "Toggle Theme")
                }
                IconButton(
                    onClick = {
                        shareApp(context, subject = "Sharing Jan Apps by Tarun", summary = "My Current Score is %d, What's yours?".format(currentScore))
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = "Share")
                }
            }
        }
            )
        }

private fun shareApp(context: Context, subject: String, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.app_name)
        )
    )
}

@Composable
fun NavigationButtons(
    onPrevButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    when (windowSize) {
        WindowWidthSizeClass.Compact  -> {
            if (isPortrait) {
                Box(modifier = modifier) {
                    Button(
                        onClick = onPrevButtonClicked,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(28.dp, 48.dp)
                    ) {
                        Text("Prev")
                    }
                    Button(
                        onClick = onNextButtonClicked,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(28.dp, 48.dp)
                    ) {
                        Text("Next")
                    }
                }
            }
            else{
                Column(modifier = modifier) {
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onPrevButtonClicked
                        ) {
                            Text("Prev")
                        }
                        Button(
                            onClick = onNextButtonClicked
                        ) {
                            Text("Next")
                        }
                    }
                    }

            }

        }
        else -> {
            Column(modifier = modifier) {
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onPrevButtonClicked
                    ) {
                        Text("Prev")
                    }
                    Button(
                        onClick = onNextButtonClicked
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}


/*Box(modifier = Modifier.fillMaxSize()) {
        var id by remember { mutableStateOf(1) }
        val gameUiState by janViewModel.uiState.collectAsState()
        when (gameUiState.currentState%3) {
            1 -> ChessWithButtonAndImage()
            0  -> CardImage(
                message = stringResource(R.string.card_text),
                from = stringResource(R.string.sign_text),
                modifier = Modifier.padding(8.dp),
                isGuessWrong = gameUiState.isGuessedWordWrong,
                onNextButtonClicked = {},
                        isGuess = gameUiState.isGuessed
            )
            else -> CalcTimeLayout()
        }
        Button(
            onClick = { janViewModel.next() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(28.dp, 48.dp)
        ) {
            Text("Next")
        }
        Button(
            onClick = { janViewModel.prev() },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(28.dp, 48.dp)
        ) {
            Text("Previous")
        }
    }*/