package com.example.abbproject.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarHistory(
            onEyeClick = { }
        )
        Spacer(modifier = Modifier.height(4.dp))

        SearchBarHistory()

        Spacer(modifier = Modifier.height(8.dp))

        HistoryFilters()

        ExpenseSummaryCard()

        Spacer(modifier = Modifier.height(12.dp))

        OperationsCard()
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    MaterialTheme {
        HistoryScreen(navController = NavController(LocalContext.current))
    }
}
