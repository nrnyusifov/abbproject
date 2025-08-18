package com.example.abbproject.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OperationCard(
    title: String,
    subtitle: String,
    time: String,
    amount: String,
    iconBackground: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(100.dp))
                .border(
                    width = 0.5.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(100.dp)
                )
                .background(iconBackground,
                    RoundedCornerShape(100.dp))
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .weight(1f)
                .padding(top = 12.dp, bottom = 12.dp, end = 16.dp, start = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF0A0B0D),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = (-0.2).sp
                ),
            )
            Text(
                text = "$subtitle • $time",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF484A4F),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = (-0.2).sp
                )
            )
        }

        Text(
            text = amount,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF0A0B0D),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = (-0.2).sp
            ),
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OperationCardPreview() {
    OperationCard(
        title = "Bravo",
        subtitle = "Ödəniş",
        time = "09:23",
        amount = "-4.89 ₼",
        iconBackground = Color(0xFF7BBF47)
    )
}
