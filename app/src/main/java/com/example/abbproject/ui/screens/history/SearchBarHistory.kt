package com.example.abbproject.ui.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarHistory(
    query: String,
    isSearching: Boolean,
    onQueryChange: (String) -> Unit,
    onFocused: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 6.dp, end = 16.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 40.dp)
                .onFocusChanged { if (it.isFocused) onFocused() },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            placeholder = {
                Text(
                    text = "Axtarış",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF7A7D82),
                    textAlign = TextAlign.Start
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFF7A7D82)
                )
            },
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0x0A0B0D0A),
                unfocusedContainerColor = Color(0x0A0B0D0A),
                disabledContainerColor = Color(0x0A0B0D0A),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedPlaceholderColor = Color(0xFF7A7D82),
                unfocusedPlaceholderColor = Color(0xFF7A7D82),
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        if (isSearching) {
            Text(
                text = "Ləğv et",
                color = Color(0xFF1B63ED),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onCancel() }
            )
        }
    }
}

@Preview(showBackground = true, name = "SearchBar – Searching")
@Composable
fun SearchBarHistoryPreview_Searching() {
    var query by remember { mutableStateOf("Za") }
    var isSearching by remember { mutableStateOf(true) }
    MaterialTheme {
        SearchBarHistory(
            query = query,
            isSearching = isSearching,
            onQueryChange = { query = it },
            onFocused = { isSearching = true },
            onCancel = {
                query = ""
                isSearching = false
            }
        )
    }
}