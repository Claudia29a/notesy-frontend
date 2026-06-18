package com.example.notesy.viewmodel

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.notesy.ui.viewmodel.NotesViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotesViewModelTest {

    private val application: Application = ApplicationProvider.getApplicationContext()
    private val viewModel = NotesViewModel(application)

    @Test
    fun whenItemNotSelected_toggleAddsIt() {
        // arrange
        val key = "item-1"
        assertFalse(viewModel.groceryDialogState.value.selectedKeys.contains(key))

        // act
        viewModel.onToggleSuggestedItem(key)

        // assert
        assertTrue(viewModel.groceryDialogState.value.selectedKeys.contains(key))
    }

    @Test
    fun whenItemAlreadySelected_toggleRemovesIt() {
        // arrange
        val key = "item-1"

        // first add it
        viewModel.onToggleSuggestedItem(key)
        assertTrue(viewModel.groceryDialogState.value.selectedKeys.contains(key))

        // act
        viewModel.onToggleSuggestedItem(key)

        // assert
        assertFalse(viewModel.groceryDialogState.value.selectedKeys.contains(key))
    }

    @Test
    fun onSuggestGroceriesClicked_whenNoteNotFound_showsEmptyDialog() {
        // arrange
        val unknownId = "non-existent-id"

        // act
        viewModel.onSuggestGroceriesClicked(unknownId)

        // give the coroutine some time to run
        Thread.sleep(200)

        // assert
        val state = viewModel.groceryDialogState.value
        assertTrue(state.visible)
        assertTrue(state.suggestions.isEmpty())
        assertTrue(state.selectedKeys.isEmpty())
    }
}