package com.example.socialguard
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class ProfanityFilterManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("filters_prefs", Context.MODE_PRIVATE)

    // Sauvegarder et récupérer l'état du filtre de grossièreté
    var isProfanityFilterEnabled: Boolean
        get() = sharedPreferences.getBoolean("profanity_filter_enabled", false)
        set(value) {
            sharedPreferences.edit().putBoolean("profanity_filter_enabled", value).apply()
            if (value) {
                startProfanityFilterService()
            } else {
                stopProfanityFilterService()
            }
        }

    // Démarrer le service en premier plan
    private fun startProfanityFilterService() {
        val intent = Intent(context, ProfanityFilterService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }


    // Arrêter le service en arrière-plan
    private fun stopProfanityFilterService() {
        val intent = Intent(context, ProfanityFilterService::class.java)
        context.stopService(intent)
    }
}
