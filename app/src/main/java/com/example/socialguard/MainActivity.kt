package com.example.socialguard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: FilterViewModel

    // Demande de permission de notification avec un lanceur de résultat
    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission accordée
        } else {
            // Gérer le cas où la permission est refusée (par exemple, montrer un message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filterLocalDataSource = FilterLocalDataSource(this)
        val getFiltersUseCase = GetFiltersUseCase(filterLocalDataSource)
        val profanityFilterManager = ProfanityFilterManager(this)

        viewModel = ViewModelProvider(this, ViewModelFactory(getFiltersUseCase, profanityFilterManager))[FilterViewModel::class.java]

        // Demande la permission de notification au démarrage si nécessaire
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkAndRequestNotificationPermission()
        }

        setContent {
            MainScreen(viewModel = viewModel, openAccessibilitySettings = { openAccessibilitySettings() })
        }
    }

    // Fonction pour ouvrir les paramètres d'accessibilité
    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    // Vérification et demande de la permission de notifications si nécessaire
    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Demande la permission si elle n'a pas encore été accordée
            requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@Composable
fun MainScreen(viewModel: FilterViewModel, openAccessibilitySettings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Texte d'information ou autre contenu
        Text("Bienvenue sur SocialGuard")
        Button(onClick = openAccessibilitySettings) {
            Text("Activer le service d'accessibilité")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Affiche la liste des filtres
        FilterListScreen(viewModel = viewModel)
    }
}
