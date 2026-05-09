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

        val themeId = when (prefs.getString("theme", "AppTheme")) {
            "Crew" -> R.style.Crew
            "FTD" -> R.style.FTD
            "GPG" -> R.style.GPG
            "Hazel" -> R.style.Hazel
            "Kotlin" -> R.style.Kotlin
            else -> R.style.AppTheme
        }

        val nightMode = when (prefs.getString("themeMode", "")) {
            "Light" -> AppCompatDelegate.MODE_NIGHT_NO
            "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        setTheme(themeId)
        AppCompatDelegate.setDefaultNightMode(nightMode)

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            PennyDropApp()
        }
    }
}
