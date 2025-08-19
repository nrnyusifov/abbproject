package com.example.abbproject.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryFilters() {
    var selectedSheet by remember { mutableStateOf<String?>(null) }
    var selectedDirection by remember { mutableStateOf<String?>(null) }
    var selectedPeriod by remember { mutableStateOf<String?>(null) }
    var selectedCalculation by remember { mutableStateOf<String?>(null) }
    var selectedType by remember { mutableStateOf<String?>(null) }

    val sheetState = rememberModalBottomSheetState()

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
                        imageVector = Icons.Filled.ExpandMore,
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
                        "Mobil ödənişlər", "Ödəniş", "Köçürmə",
                        "Nağdlaşdırma", "Komissiya", "Cash-in", "Kredit limiti"
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

                "Kartlar" -> {
                    Text(
                        text = "Kartlar üçün seçimlər tezliklə.",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
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
}

@Preview(showBackground = true)
@Composable
fun HistoryFiltersPreview() {
    MaterialTheme {
        HistoryFilters()
    }
}