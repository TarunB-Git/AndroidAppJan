package com.example.janapps.ui.theme

import android.icu.text.NumberFormat
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.janapps.R

@Composable
fun CalcTimeLayout(windowSize: WindowWidthSizeClass, onNextButtonClicked: () -> Unit = {}, onPrevButtonClicked: () -> Unit = {}, modifier: Modifier = Modifier
    .fillMaxSize()
    .background(Color.Black)) {
    var amountInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    var amountInput2 by remember { mutableStateOf("") }
    val amount2 = amountInput2.toDoubleOrNull() ?: 0.0
    var roundUp by remember { mutableStateOf(false) }
    var operate by remember { mutableStateOf("perc") }
    val result = calculate(amount, amount2, roundUp, operate)
    Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 40.dp)
                .verticalScroll(state = rememberScrollState())
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = stringResource(R.string.simple_calculator),
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 40.dp)
                    .align(alignment = Alignment.Start)
            )
            EditNumberField(
                label = R.string.value_1,
                leadingIcon = R.drawable.icon2,
                value = amountInput,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { amountInput = it },
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )
            EditNumberField(
                label = R.string.value_2,
                leadingIcon = R.drawable.icon3,
                value = amountInput2,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { amountInput2 = it },
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )
            Row {
                Buttoner(onClick = { operate = "add" },
                    content = { Text("+") })
                Spacer(modifier = Modifier.width(15.dp))
                Buttoner(onClick = { operate = "sub" },
                    content = { Text("-") })
                Spacer(modifier = Modifier.width(15.dp))
                Buttoner(onClick = { operate = "mul" },
                    content = { Text("*") })
                Spacer(modifier = Modifier.width(15.dp))
                Buttoner(onClick = { operate = "div" },
                    content = { Text("/") })
            }
            Spacer(modifier = Modifier.height(20.dp))
            RoundRow(
                roundUp = roundUp,
                onRoundUpChanged = { roundUp = it },
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(40.dp))
            Texto(
                label = when (operate) {
                    "add" -> R.string.amount2
                    "sub" -> R.string.amount3
                    "mul" -> R.string.amount4
                    "div" -> R.string.amount5
                    else -> R.string.amount1
                },
                result = result

            )

            Spacer(modifier = Modifier.height(100.dp))

        }
        NavigationButtons(
            windowSize = windowSize,
            onPrevButtonClicked = onPrevButtonClicked,
            onNextButtonClicked = onNextButtonClicked)
}


@Composable
fun EditNumberField(@StringRes label: Int,
                    @DrawableRes leadingIcon: Int,
                    keyboardOptions : KeyboardOptions,
                    value: String,
                    onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        label = {Text(stringResource(label))}
    )
}

@Composable
fun RoundRow(roundUp: Boolean,
             onRoundUpChanged: (Boolean) -> Unit,
             modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.round_up))
        Switch(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
        )
    }
}

@Composable
fun Texto(@StringRes label: Int,
          result: String,
          modifier: Modifier = Modifier
){
    Text(
        text = stringResource(label, result),
        style = MaterialTheme.typography.displaySmall
    )
}
@Composable
fun Buttoner(onClick: () -> Unit, content: @Composable () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier,
    )
    {
        content()
    }
}

@VisibleForTesting
internal fun calculate(amount: Double, amount2: Double , roundUp: Boolean, operate: String ): String {
    var res  = when (operate) {
        "add" -> amount + amount2
        "sub" -> amount - amount2
        "mul" -> amount * amount2
        "div" -> amount / amount2
        else -> amount /100 * amount2
    }
    if (roundUp) {
        res = kotlin.math.ceil(res)
    }
    return NumberFormat.getInstance().format(res)
}
