package com.example.janapps.ui.theme
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.update

class JanViewModel :ViewModel() {

    private val _uiState = MutableStateFlow(JanUiState())
    val uiState: StateFlow<JanUiState> = _uiState.asStateFlow()
    var userGuess by mutableStateOf("")
        private set
    var isSubmitted by mutableStateOf(false)
    private val _isDarkMode = MutableStateFlow(0)
    val isDarkMode: StateFlow<Int> = _isDarkMode.asStateFlow()
    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
        if (guessedWord.isNotBlank()) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = !guessedWord.equals("Tarun", ignoreCase = true)
                )
            }
        }

    }

    fun checkUserGuess() {
        isSubmitted = true
        if (userGuess.equals("Tarun", ignoreCase = true)) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    isGuessed = true
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = true,
                    isGuessed = false
                )
            }
        }
    }
    fun score(value : Int = 100) {
        _uiState.update { currentState ->
            if(currentState.currentScore<1000){
            currentState.copy(currentScore = currentState.currentScore + value)
        }
            else {currentState}}
}
    fun toggleDarkMode() {
        _isDarkMode.value = (_isDarkMode.value+1)%4
    }

}
/* fun next() {
        _uiState.update { currentState ->
            currentState.copy(currentState = currentState.currentState + 1)
        }
    }

    fun prev() {
        _uiState.update { currentState ->
            currentState.copy(currentState = currentState.currentState - 1)
        }
    }*/