package com.prototype.dialer.ui.dialer

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.prototype.dialer.ui.theme.ApplicationTheme

@Composable
fun Ringer() {
    Column {

    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun RingerPreview() {
    ApplicationTheme {
        Ringer()
    }
}