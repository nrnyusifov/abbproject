package com.example.abbproject.ui.screens.history

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random
import com.example.abbproject.R
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.SouthEast
import androidx.compose.material3.Icon
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun ExpenseSummaryCard() {
    val categoryTitles = listOf("Maliyyə", "Restoran", "YDM", "Supermarket")
    val categoryColors = listOf(
        Color(0xFF22A889), Color(0xFF18A81D), Color(0xFFC68A00), Color(0xFFE61554)
    )

    var previousTotal by remember { mutableStateOf(0.0) }

    val categoryValues = remember { List(4) { Random.nextDouble(200.0, 2000.0) } }
    val total = categoryValues.sum()

    val animatedTotal by animateFloatAsState(
        targetValue = total.toFloat(),
        animationSpec = tween(800),
        label = "TotalAnimation"
    )

    val increasePercent = remember(total) {
        if (previousTotal == 0.0) 0 else (((total - previousTotal) / previousTotal) * 100).toInt()
    }

    LaunchedEffect(Unit) { previousTotal = total }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color(0xFFFCFDFF), shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Xərclər",
                    color = Color(0xFF484A4F),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    letterSpacing = (-0.5).sp,
                    lineHeight = 20.sp
                )

                if (previousTotal > 0.0) {
                    val isUp = increasePercent >= 0
                    val textColor = if (isUp) Color(0xFF18A81D) else Color(0xFFDC2626)
                    val chipBg = if (isUp) Color(0xFFDEF7DA) else Color(0xFFFEE2E2)

                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(chipBg, RoundedCornerShape(100.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isUp) Icons.Filled.NorthEast else Icons.Filled.SouthEast,
                                contentDescription = if (isUp) "Increase" else "Decrease",
                                tint = textColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "${kotlin.math.abs(increasePercent)}%",
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Medium,
                                    fontStyle = FontStyle.Normal,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    letterSpacing = 0.sp,
                                    color = textColor
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            TextButton(
                onClick = { /* TODO: handle click */ },
                modifier = Modifier.height(40.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp) // control internal padding
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bu ay",
                        color = Color(0xFF1B63ED),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 0.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_down),
                        contentDescription = "Select month",
                        tint = Color(0xFF1B63ED),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(start = 4.dp)
                    )
                }
            }
        }

        Text(
            buildAnnotatedString
            { withStyle(
                style = SpanStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Normal,
                    fontSize = 28.sp,
                    letterSpacing = 0.4.sp,
                    color = Color(0xFF0A0B0D)
                )
            ) {
                append(String.format("%.0f", animatedTotal))
            }
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Normal,
                        fontSize = 18.sp,
                        letterSpacing = 0.sp,
                        color = Color(0xFF0A0B0D)
                    )
                ) {
                    append(String.format(".%02d ₼", ((animatedTotal % 1) * 100).toInt()))
                }
            }
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            categoryValues.forEachIndexed { index, value ->
                val weight = (value / total).toFloat()
                Box(
                    modifier = Modifier
                        .weight(weight)
                        .fillMaxHeight()
                        .background(categoryColors[index])
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categoryValues.size) { index ->
                val value = categoryValues[index]
                val percent = (value / total) * 100

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "${categoryTitles[index]}  ${percent.toInt()}%",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            letterSpacing = (-0.2).sp,
                            color = Color(0xFF7A7D82)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(categoryColors[index])
                        )
                        Text(
                            text = String.format("%.2f ₼", value),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontStyle = FontStyle.Normal,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = (-0.2).sp,
                                color = Color(0xFF0A0B0D)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        TextButton(
            onClick = { /* TODO */ },
            contentPadding = PaddingValues(vertical = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFAFBFD)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lifebuoy),
                            contentDescription = "Lifebuoy Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Daha ətraflı",
                        color = Color(0xFF1B63ED),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 0.sp,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontStyle = FontStyle.Normal
                        )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = Color(0xFF1B63ED),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseSummaryCardPreview() {
    MaterialTheme { ExpenseSummaryCard() }
}

