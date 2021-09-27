package com.prototype.dialer.ui.dialer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prototype.dialer.ui.theme.ApplicationTheme

private val symbols = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, "*", 0, "#")
    .map { it.toString() }

@Composable
fun Dialer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp, 0.dp)
    ) {
        for (row in 0..2) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                for (col in 0..3) {
                    DialerCell(symbols[row + col * 3])
                }
            }
        }
    }
}

@Composable
fun DialerCell(symbol: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(50.dp)
            .border(
                width = 1.dp,
                color = Color(0XFFEAEBEE),
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = symbol)
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun DialerPreview() {
    ApplicationTheme {
        Dialer()
    }
}