package com.example.abbproject.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    onEyeClick: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedSheet by remember { mutableStateOf<String?>(null) }

    var selectedDirection by remember { mutableStateOf<String?>(null) }
    var selectedPeriod by remember { mutableStateOf<String?>(null) }
    var selectedCalculation by remember { mutableStateOf<String?>(null) }
    var selectedType by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                                color = Color(0xFFFCF5FD),
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

        Spacer(modifier = Modifier.height(4.dp))

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
                                color = Color(0xFF7A7D82)
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

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedSheet != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedSheet = null },
                sheetState = sheetState,
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                Text(
                    text = selectedSheet ?: "",
                    modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                when (selectedSheet) {
                    "İstiqamət" -> {
                        val options = listOf("Gəlir", "Xərc")
                        options.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedDirection = option }
                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = option, style = MaterialTheme.typography.bodyLarge)
                                RadioButton(
                                    selected = selectedDirection == option,
                                    onClick = { selectedDirection = option }
                                )
                            }
                        }
                    }

                    "Dövr" -> {
                        val options = listOf("Bu gün", "Son 7 gün", "Son 30 gün", "Son 90 gün", "Tarix üzrə")
                        options.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedPeriod = option }
                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = option, style = MaterialTheme.typography.bodyLarge)
                                RadioButton(
                                    selected = selectedPeriod == option,
                                    onClick = { selectedPeriod = option }
                                )
                            }
                        }
                    }

                    "Hesablama" -> {
                        val options = listOf("Hesablanan əməliyyatlar", "Hesablanmayan əməliyyatlar")
                        options.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedCalculation = option }
                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = option, style = MaterialTheme.typography.bodyLarge)
                                RadioButton(
                                    selected = selectedCalculation == option,
                                    onClick = { selectedCalculation = option }
                                )
                            }
                        }
                    }

                    "Növ" -> {
                        val types = listOf(
                            "Mobil ödənişlər",
                            "Ödəniş",
                            "Köçürmə",
                            "Nağdlaşdırma",
                            "Komissiya",
                            "Cash-in",
                            "Kredit limiti"
                        )

                        types.forEach { type ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedType = type }
                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = type, style = MaterialTheme.typography.bodyLarge)
                                RadioButton(
                                    selected = selectedType == type,
                                    onClick = { selectedType = type }
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = { selectedSheet = null },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Təsdiq et")
                }
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 4.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chipLabels = listOf("İstiqamət", "Dövr", "Növ", "Kartlar", "Hesablama")

            items(chipLabels) { label ->
                Box(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 36.dp)
                        .wrapContentWidth()
                        .background(color = Color(0xFFF3F5F7), shape = RoundedCornerShape(20.dp))
                        .clickable { selectedSheet = label }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label,
                            fontSize = 14.sp,
                            color = Color(0xFF1C1C1E),
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = Color(0xFF1C1C1E),
                            modifier = Modifier
                                .size(18.dp)
                                .padding(start = 4.dp)
                        )
                    }
                }
            }
        }
        ExpenseSummaryCard()
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    MaterialTheme {
        HistoryScreen(navController = NavController(LocalContext.current))
    }
}
