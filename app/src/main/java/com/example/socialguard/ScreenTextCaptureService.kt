package com.example.socialguard

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ScreenTextCaptureService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // Vérifie si l'événement est lié à un changement de texte sur l'écran
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || event.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            // Récupère la vue racine de l'application actuelle
            val rootNode = rootInActiveWindow
            if (rootNode != null) {
                // Parcours les éléments de l'interface utilisateur et extrait le texte
                val screenText = extractTextFromNode(rootNode)
                if (screenText.isNotEmpty()) {
                    Log.d("ScreenTextCapture", "Texte capturé : $screenText")
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
}
