package com.example.socialguard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Switch
import androidx.compose.ui.platform.LocalContext

@Composable
fun FilterListScreen(viewModel: FilterViewModel) {
    // Observer la liste de filtres depuis le ViewModel
    val filters = viewModel.filters.collectAsState()
    val isProfanityFilterEnabled = viewModel.isProfanityFilterEnabled.collectAsState()

    // Afficher la colonne principale
    Column(
        modifier = Modifier
            .fillMaxHeight() // Remplir toute la hauteur de l'écran
            .padding(16.dp) // Optionnel : ajouter du padding
    ) {
        // Afficher la liste des filtres dans une LazyColumn
        LazyColumn(
            modifier = Modifier.weight(1f) // Prendre l'espace disponible jusqu'à ce que le bouton soit en bas
        ) {
            items(items = filters.value, itemContent = { filter ->
                FilterItem(filter = filter, onCheckedChange = { viewModel.toggleFilter(filter.id) })
            })
        }

        // Ajouter un Spacer pour séparer la liste du bouton
        Spacer(modifier = Modifier.height(8.dp))

        // Ajouter le bouton on/off pour activer/désactiver le filtre de grossièreté
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Activer le filtre de grossièretés",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = isProfanityFilterEnabled.value,
                onCheckedChange = { viewModel.toggleProfanityFilter(it) }
            )
        }
    }
}

@Composable
fun FilterItem(filter: Filter, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Icone à gauche
        Icon(
            painter = painterResource(id = R.drawable.eye),
            contentDescription = filter.text,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Texte au centre
        Text(
            text = filter.text,
            modifier = Modifier.weight(1f)
        )

        // CheckBox à droite
        Switch(
            checked = filter.state,
            onCheckedChange = { onCheckedChange(it) }
        )
    }
}
