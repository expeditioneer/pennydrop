package dev.lamm.pennydrop

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val nightMode = when (prefs.getString("themeMode", "")) {
            "Light" -> AppCompatDelegate.MODE_NIGHT_NO
            "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            PennyDropApp()
        }
    }
}
