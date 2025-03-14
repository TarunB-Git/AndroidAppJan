package com.example.janapps.ui.theme

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.janapps.R


@Composable
fun CardText(janViewModel: JanViewModel = viewModel(),  name: String, message: String,
             from:String, modifier: Modifier = Modifier, onUserGuessChanged: (String) -> Unit, onKeyboardDone: () -> Unit,
             isGuessWrong: Boolean, isGuess : Boolean, isSubmitted : Boolean             ) {
    Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        Text(
            text = if (isSubmitted && isGuessWrong) stringResource(R.string.oops) else if(isSubmitted&& isGuess) stringResource(R.string.outro) else stringResource(R.string.intro),
            fontSize = 15.sp,
            lineHeight = 25.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 4.dp
                )
                .align(alignment = Alignment.End)
        )
        Text(
            text = if (isGuess) "~Tarun" else from,
            fontSize = 10.sp,
            modifier = Modifier
                .padding(8.dp, top = 2.dp)
                .align(alignment = Alignment.End)
        )
        EditNumberField(
            label = when {
                isSubmitted && isGuessWrong -> (R.string.wrong_guess)
                isGuess -> (R.string.right_guess)
                else -> (R.string.guess)
            },
            isError = isGuessWrong,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            ),
            value = message, // The current value to be displayed
            onValueChange = onUserGuessChanged, // Function to handle value change
            onKeyboardDone = { janViewModel.checkUserGuess()
                if(message == "Tarun"){ janViewModel.score()}                },
            modifier = Modifier
                .padding(start = 16.dp, top = 12.dp, end = 16.dp)
                .fillMaxWidth(),
                    enabled = !isSubmitted        )
        Button(
            modifier = modifier
                .align(Alignment.CenterHorizontally),
            onClick = { janViewModel.checkUserGuess()
                if(isGuess){ janViewModel.score()}
            }
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun CardImage(windowSize: WindowWidthSizeClass, outsideModel : JanViewModel, janViewModel: JanViewModel = viewModel(), currentScore :Int, message: String, from: String, onNextButtonClicked: () -> Unit = {}, onPrevButtonClicked: () -> Unit = {},
              modifier: Modifier = Modifier) {
    val uiState = janViewModel.uiState.collectAsState().value
    val image = painterResource(R.drawable.ann)
    var hasRunEffect = remember { mutableStateOf(true) }
    LaunchedEffect(uiState.currentScore){
    if(uiState.currentScore >100 && hasRunEffect.value) {outsideModel.score()
        hasRunEffect.value = false }}
    Box() {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.5F,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "Score: (lil buggy) %d".format(currentScore),
            fontSize = 10.sp,
            modifier = Modifier
                .padding(8.dp, top = 2.dp)
        )
        CardText(
            name = "Hello",
            message = janViewModel.userGuess,
            from = stringResource(R.string.sign_text),
            onUserGuessChanged = { janViewModel.updateUserGuess(it) },
            onKeyboardDone = {},
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            isGuessWrong = uiState.isGuessedWordWrong,
            isGuess =uiState.isGuessed,
            isSubmitted = janViewModel.isSubmitted,

            )
        NavigationButtons(
            onPrevButtonClicked = onPrevButtonClicked,
            windowSize = windowSize,
            onNextButtonClicked = onNextButtonClicked)
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onKeyboardDone: () -> Unit,
    isError: Boolean,
    enabled: Boolean = true
    ) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        isError = isError,
        textStyle = TextStyle(
            fontSize = 30.sp,
            lineHeight = 30.sp
        ),
        enabled = enabled,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorScheme.surface,
            unfocusedContainerColor = colorScheme.surface,
            disabledContainerColor = colorScheme.surface),
        keyboardActions = KeyboardActions(
            onDone = { onKeyboardDone() } ),
        label = { Text(stringResource(id = label)) }
    )
}
