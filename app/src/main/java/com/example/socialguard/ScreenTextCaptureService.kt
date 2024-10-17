package com.example.socialguard

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Locale

class ScreenTextCaptureService : AccessibilityService() {
    private lateinit var profanityFilterManager: ProfanityFilterManager

    override fun onServiceConnected() {
        super.onServiceConnected()
        profanityFilterManager = ProfanityFilterManager(this)
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!profanityFilterManager.isProfanityFilterEnabled) {
            // return
        }

        // Vérifie si l'événement est lié à un changement de texte sur l'écran
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || event.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            // Récupère la vue racine de l'application actuelle
            val rootNode = rootInActiveWindow
            if (rootNode != null) {
                // Parcours les éléments de l'interface utilisateur et extrait le texte
                val screenText = extractTextFromNode(rootNode)
                if (screenText.isNotEmpty()) {
                    Log.d("ScreenTextCapture", "Texte capturé : $screenText")
                    checkForViolentWords(screenText)
                }
            }
        }
    }

    override fun onInterrupt() {
        // Ce que fait ton service quand il est interrompu (par exemple lorsqu'il est arrêté).
    }

    // Fonction récursive pour extraire le texte de tous les nœuds de l'interface utilisateur
    private fun extractTextFromNode(node: AccessibilityNodeInfo?): String {
        if (node == null) return ""

        val stringBuilder = StringBuilder()
        if (node.text != null) {
            stringBuilder.append(node.text).append("\n")
        }

        // Parcours les enfants de ce nœud pour extraire également leur texte
        for (i in 0 until node.childCount) {
            stringBuilder.append(extractTextFromNode(node.getChild(i)))
        }

        return stringBuilder.toString()
    }

    // Fonction pour vérifier la présence de mots violents dans le texte capturé
    private fun checkForViolentWords(text: String) {
        val lowerCaseText = text.toLowerCase(Locale.ROOT)
        for (word in ViolentWords.words) {
            if (lowerCaseText.contains(word)) {
                sendNotification() // Envoie la notification si un mot violent est trouvé
                break
            }
        }
    }

    // Fonction pour envoyer une notification en cas de détection de mots violents
    private fun sendNotification() {
        val channelId = "social_guard_channel"
        val notificationId = 1

        // Créer le canal de notification si nécessaire (Android O+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SocialGuard Channel"
            val descriptionText = "Canal pour les alertes de comportements grossiers"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Enregistrer le canal de notification
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Créer une intention pour rediriger l'utilisateur vers l'application lorsqu'il clique sur la notification
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        // Créer la notification
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.eye) // Remplace avec ton propre icône
            .setContentTitle("Attention SocialGuard")
            .setContentText("Un comportement grossier a été détecté.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Envoyer la notification
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    baseContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(notificationId, builder.build())
        }
    }
}
