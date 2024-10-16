package com.example.socialguard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun FilterListScreen(viewModel: FilterViewModel) {
    // Observer la liste de filtres depuis le ViewModel
    val filters = viewModel.filters.collectAsState()

    // Afficher la liste dans une LazyColumn
    LazyColumn() {
        items(items = filters.value, itemContent = { filter ->
            FilterItem(filter = filter, onCheckedChange = { viewModel.toggleFilter(filter.id) })
        }
        )
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
            painter = painterResource(id = filter.icon),
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
