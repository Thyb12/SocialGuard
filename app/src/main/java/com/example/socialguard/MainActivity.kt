package com.example.socialguard

import android.content.Intent
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
            // Gérer le cas où la permission est refusée
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filterLocalDataSource = FilterLocalDataSource(this)
        val getFiltersUseCase = GetFiltersUseCase(filterLocalDataSource)
        val profanityFilterManager = ProfanityFilterManager(this)

        viewModel = ViewModelProvider(this, ViewModelFactory(getFiltersUseCase, profanityFilterManager))[FilterViewModel::class.java]

        setContent {
            MainScreen(viewModel = viewModel, openAccessibilitySettings = { openAccessibilitySettings() })
        }
    }

    // Fonction pour ouvrir les paramètres d'accessibilité
    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
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

        // Ajoute un bouton pour ouvrir les paramètres d'accessibilité
        Button(onClick = openAccessibilitySettings) {
            Text("Activer le service d'accessibilité")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Affiche la liste des filtres
        FilterListScreen(viewModel = viewModel)
    }
}
