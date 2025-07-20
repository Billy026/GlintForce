package com.billy.glintforce.viewModel.main

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel()
    }

    @Test
    fun toggleLaunch_initial_isFalse() = runTest {
        assertEquals(false, viewModel.uiState.value.launch)
    }

    @Test
    fun toggleLaunch_executeOnce_isTrue() = runTest {
        viewModel.toggleLaunch()
        assertEquals(true, viewModel.uiState.value.launch)
    }

    @Test
    fun toggleLaunch_executeTwice_isFalse() = runTest {
        viewModel.toggleLaunch()
        viewModel.toggleLaunch()
        assertEquals(false, viewModel.uiState.value.launch)
    }
}