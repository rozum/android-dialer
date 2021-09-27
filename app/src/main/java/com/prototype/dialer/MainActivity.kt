package com.prototype.dialer

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.net.sip.SipSession
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.prototype.dialer.MainActivity.State.INCOMING_CALL
import com.prototype.dialer.core.extension.TAG
import com.prototype.dialer.ui.dialer.Dialer
import com.prototype.dialer.ui.theme.ApplicationTheme


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
            ApplicationTheme {
                Surface(color = MaterialTheme.colors.background) {
                    when {
                        intent.getStringExtra(STATE_KEY) == INCOMING_CALL.name -> Dialer()
                        else -> Dialer()
                    }
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

    enum class State {
        INCOMING_CALL,
        DIALER
    }

    companion object {
        const val STATE_KEY = "STATE_KEY"
    }
}

