package com.example.socialguard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ProfanityFilterReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val profanityFilterManager = ProfanityFilterManager(context)
        val action = intent.action

        if (action == "com.example.socialguard.ACTION_TOGGLE_PROFANITY_FILTER") {
            val isEnabled = profanityFilterManager.isProfanityFilterEnabled
            profanityFilterManager.isProfanityFilterEnabled = !isEnabled
            Toast.makeText(context, if (!isEnabled) "Filtre activé" else "Filtre désactivé", Toast.LENGTH_SHORT).show()
        }
    }
}
