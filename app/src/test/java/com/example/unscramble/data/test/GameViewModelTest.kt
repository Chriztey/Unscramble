package com.example.unscramble.data.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Test

class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value

        // Assert that checkUserGuess() method updates isGuessedWordWrong is updated correctly.
        assertFalse(currentGameUiState.isGuessedWordWrong)
        // Assert that score is updated correctly.
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)

    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val incorrectPlayerWord = "and"

        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        var currentGameUiState = viewModel.uiState.value

        assertTrue(currentGameUiState.isGuessedWordWrong)
        assertEquals(0, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val gameUiState = viewModel.uiState.value
        val unScrambleWord = getUnscrambledWord(gameUiState.currentScrambledWord)

        assertNotEquals(unScrambleWord, gameUiState.currentScrambledWord)
        assertEquals(1, gameUiState.currentWordCount)
        assertTrue(gameUiState.score == 0)
        assertFalse(gameUiState.isGuessedWordWrong)
        assertFalse(gameUiState.isGameOver)

    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentGameState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameState.currentScrambledWord)

        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameState.currentScrambledWord)
            assertEquals(expectedScore, currentGameState.score)
        }

        assertEquals(MAX_NO_OF_WORDS, currentGameState.currentWordCount)
        assertTrue(currentGameState.isGameOver)


    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased(){
        var currentGameState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        currentGameState = viewModel.uiState.value
        var lastWordCount = currentGameState.currentWordCount
        viewModel.skipWord()
        currentGameState = viewModel.uiState.value
        assertEquals(lastWordCount + 1 , currentGameState.currentWordCount)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameState.score)


    }


    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }

    /***
    Note: The code above uses the thingUnderTest_TriggerOfTest_ResultOfTest format to name the test function name:

    thingUnderTest = gameViewModel
    TriggerOfTest = CorrectWordGuessed
    ResultOfTest = ScoreUpdatedAndErrorFlagUnset
     ***/




}