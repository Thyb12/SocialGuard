package com.example.socialguard

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ProfanityFilterService : Service() {

    override fun onCreate() {
        super.onCreate()
        // Démarre le service en tant que Foreground Service avec une notification
        startForegroundServiceWithNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Le service reste en arrière-plan même lorsque l'application est fermée
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Arrêter proprement le service
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "profanity_filter_service_channel"
        val channelName = "Filtre de Grossièretés"

        // Créer un canal de notification pour Android 8.0+
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(notificationChannel)

        // Créer une notification pour le service en premier plan
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Service de Filtre de Grossièretés")
            .setContentText("Le filtre est activé.")
            .setSmallIcon(R.drawable.eye) // Utiliser l'icône appropriée
            .build()

        // Démarrer le service en premier plan
        startForeground(1, notification)
    }
}
