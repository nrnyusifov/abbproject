package com.example.abbproject.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OperationsCard() {
    val market = listOf(
        "Bravo" to Color(0xFF7BBF47),
        "Araz" to Color(0xFF98C432),
        "Neptun" to Color(0xFFFF8808),
        "Grandmart" to Color(0xFF0898DA),
        "Bolmart" to Color(0xFFE4131D)
    )

    val restaurant = listOf(
        "McDonald's" to Color(0xFFFFC72C),
        "KFC" to Color(0xFFED1C24),
        "Burger King" to Color(0xFFD62300),
        "Papa John's" to Color(0xFF007042)
    )

    val clothing = listOf(
        "LC Waikiki" to Color(0xFF003399),
        "Colin's" to Color(0xFFE30613),
        "Defacto" to Color(0xFF003366),
        "Zara" to Color(0xFF000000)
    )

    val bank = listOf(
        "Kapital Bank" to Color(0xFF9B1B30),
        "Pasha Bank" to Color(0xFF007F5F),
        "ABB" to Color(0xFF004B87),
        "Bank Respublika" to Color(0xFF005BAC),
        "Yelo Bank" to Color(0xFFFFEB3B)
    )

    val mobileOperators = listOf(
        "Nar" to Color(0xFFA20067),
        "Bakcell" to Color(0xFFED1C24),
        "Azercell" to Color(0xFF74177F)
    )

    // Merge everything into one list with their subtitles
    val allOperations = mutableListOf<Triple<String, Color, String>>()

    allOperations += market.map { Triple(it.first, it.second, "Ödəniş") }
    allOperations += restaurant.map { Triple(it.first, it.second, "Ödəniş") }
    allOperations += clothing.map { Triple(it.first, it.second, "Ödəniş") }
    allOperations += bank.map { Triple(it.first, it.second, "Köçürmə") }
    allOperations += mobileOperators.map { Triple(it.first, it.second, "Mobil Ödənişlər") }

    // Shuffle for randomness
    val randomList = allOperations.shuffled()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(Color(0xFFFCFDFF), shape = RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        randomList.take(5).forEach { (title, color, subtitle) ->
            val randomHour = (0..23).random().toString().padStart(2, '0')
            val randomMinute = (0..59).random().toString().padStart(2, '0')
            val randomAmount = "-${String.format("%.2f", (1..200).random() + Math.random())} ₼"

            OperationCard(
                title = title,
                subtitle = subtitle,
                time = "$randomHour:$randomMinute",
                amount = randomAmount,
                iconBackground = color
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OperationsCardPreview() {
    MaterialTheme { OperationsCard() }
}