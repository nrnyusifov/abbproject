package com.example.abbproject.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarHistory() {
    Box(
        modifier = Modifier
            .width(390.dp)
            .height(52.dp)
            .padding(start = 16.dp, top = 6.dp, end = 16.dp, bottom = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .width(358.dp)
                .height(40.dp)
                .background(
                    color = Color(0x0A0B0D0A),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp)
        ) {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        text = "Axtarış",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            lineHeight = MaterialTheme.typography.titleMedium.lineHeight,
                            letterSpacing = (-0.5).sp,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily.SansSerif,
                            color = Color(0xFF7A7D82),
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier
                            .width(52.dp)
                            .height(24.dp)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(top = 3.dp, start = 3.dp),
                        tint = Color(0xFF7A7D82)
                    )
                },
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Gray,
                    focusedLeadingIconColor = Color.Gray,
                    unfocusedLeadingIconColor = Color.Gray,
                    disabledLeadingIconColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                singleLine = true,
                enabled = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarHistoryPreview() {
    MaterialTheme {
        SearchBarHistory()
    }
}