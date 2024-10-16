package com.example.socialguard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilterViewModel(private val getFiltersUseCase: GetFiltersUseCase) : ViewModel() {

    private val _filters = MutableStateFlow<List<Filter>>(emptyList())
    val filters: StateFlow<List<Filter>> = _filters

    init {
        loadFilters()
    }

    private fun loadFilters() {
        _filters.value = getFiltersUseCase.getFilters()
    }

    // Fonction pour mettre à jour l'état d'un filtre
    fun toggleFilter(id: String) {
        _filters.value = _filters.value.map { filter ->
            if (filter.id == id) filter.copy(state = !filter.state) else filter
        }
        // Sauvegarde la liste mise à jour
        getFiltersUseCase.saveFilters(_filters.value)
    }
}
