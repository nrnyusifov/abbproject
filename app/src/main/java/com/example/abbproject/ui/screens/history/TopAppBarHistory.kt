package com.example.abbproject.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHistory(
    onEyeClick: () -> Unit
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tarixçə",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier.padding(end = 16.dp, top = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color(0x1F63ED1F),
                            shape = CircleShape
                        )
                        .clickable(onClick = onEyeClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "Show history",
                        tint = Color(0xFF1B63ED),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        navigationIcon = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TopAppBarHistoryPreview() {
    MaterialTheme {
        TopAppBarHistory(
            onEyeClick = { /* Preview only */ }
        )
    }
}