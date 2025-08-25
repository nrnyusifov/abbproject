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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHistory(
    isSearching: Boolean,
    onEyeClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isSearching) "Axtarış" else "Tarixçə",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Normal,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            letterSpacing = (-0.4).sp,
                            color = Color(0xFF0A0B0D)
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 10.dp, start = 9.dp)
                    )
                }

                if (!isSearching) {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp, top = 6.dp)
                            .size(32.dp)
                            .background(color = Color(0xFFD3DEF1), shape = CircleShape)
                            .clickable(onClick = onEyeClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "Show",
                            tint = Color(0xFF1B63ED),
                            modifier = Modifier
                                .size(18.dp)
                        )

                    }
                }
            }
        },
        navigationIcon = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
    )
}
