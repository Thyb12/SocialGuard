package com.example.socialguard

import android.content.Intent
import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log

class ProfanityFilterTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTileState() // Mettre à jour l'état de la tuile
    }

    override fun onClick() {
        super.onClick()

        val profanityFilterManager = ProfanityFilterManager(this)
        val currentState = profanityFilterManager.isProfanityFilterEnabled

        // Basculer l'état du filtre et démarrer/arrêter le service en conséquence
        profanityFilterManager.isProfanityFilterEnabled = !currentState

        if (profanityFilterManager.isProfanityFilterEnabled) {
            val intent = Intent(this, ProfanityFilterService::class.java)
            startForegroundService(intent) // Démarrer le service en premier plan
        } else {
            val intent = Intent(this, ProfanityFilterService::class.java)
            stopService(intent) // Arrêter le service
        }

        Log.d("ProfanityFilterTile", if (profanityFilterManager.isProfanityFilterEnabled) "Filtre activé" else "Filtre désactivé")

        updateTileState() // Mettre à jour la tuile après chaque clic
    }

    private fun updateTileState() {
        val profanityFilterManager = ProfanityFilterManager(this)
        val tile = qsTile

        if (profanityFilterManager.isProfanityFilterEnabled) {
            tile.state = Tile.STATE_ACTIVE
            tile.label = "Filtre Activé"
            tile.icon = Icon.createWithResource(this, R.drawable.eye)
        } else {
            tile.state = Tile.STATE_INACTIVE
            tile.label = "Filtre Désactivé"
            tile.icon = Icon.createWithResource(this, R.drawable.eye)
        }

        tile.updateTile()
    }
}
