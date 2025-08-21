package com.example.abbproject.ui.screens.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModel() {
    var isSearching by mutableStateOf(false)
        private set

    var query by mutableStateOf("")
        private set

    fun startSearch() { isSearching = true }

    fun updateQuery(newQuery: String) { query = newQuery }

    fun cancelSearch() {
        query = ""
        isSearching = false
    }
}

