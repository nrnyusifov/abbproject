package com.example.abbproject.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = Color(0xFFEDEFF2),
        topBar = {
            TopAppBarHistory(
                isSearching = viewModel.isSearching,
                onEyeClick = { }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                SearchBarHistory(
                    query = viewModel.query,
                    isSearching = viewModel.isSearching,
                    onQueryChange = viewModel::updateQuery,
                    onFocused = { viewModel.startSearch() },
                    onCancel = {
                        viewModel.cancelSearch()
                        focusManager.clearFocus()
                    }
                )
            }
            item { HistoryFilters() }
            if (!viewModel.isSearching) {
                item { ExpenseSummaryCard() }
            }
            item { OperationsCard(query = viewModel.query) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    MaterialTheme {
        HistoryScreen(navController = NavController(LocalContext.current))
    }
}
