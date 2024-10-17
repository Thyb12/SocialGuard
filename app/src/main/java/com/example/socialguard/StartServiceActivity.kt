package com.example.socialguard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class StartServiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Démarrer le service en mode foreground
        val intent = Intent(this, ProfanityFilterService::class.java)
        startForegroundService(intent)

        // Fermer l'activité après avoir démarré le service
        finish()
    }
}