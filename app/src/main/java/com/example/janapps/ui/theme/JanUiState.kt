package com.example.janapps.ui.theme

data class JanUiState (
    val currentState: Int = 0,
    val currentScore: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGuessed : Boolean = false,
    val isSubmitted : Boolean = false
)