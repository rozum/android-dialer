package com.prototype.dialer

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prototype.dialer.ui.theme.CallerExampleTheme

import android.app.role.RoleManager
import android.content.Context

import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import com.prototype.dialer.core.extension.TAG


class MainActivity : ComponentActivity() {

    private val requestRole =
        registerForActivityResult(GetRole()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "App is now the default dialer app")
            } else {
                Log.d(TAG, "App is not the default dialer app")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CallerExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Dialer()
                }
            }
        }


        requestRole.launch(null)
    }

    class GetRole : ActivityResultContract<Nothing?, ActivityResult>() {

        override fun createIntent(context: Context, input: Nothing?): Intent {
            return if (Build.VERSION.SDK_INT >= 29) {
                val roleManager = context.getSystemService(ROLE_SERVICE) as RoleManager
                roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
            } else {
                Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult {
            return ActivityResult(resultCode, intent)
        }
    }

}

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
fun DefaultPreview() {
    CallerExampleTheme {
        Dialer()
    }
}