package com.example.socialguard

class GetFiltersUseCase(private val filterLocalDataSource: FilterLocalDataSource) {

    // Récupère les filtres en vérifiant d'abord le stockage local
    fun getFilters(): List<Filter> {
        return filterLocalDataSource.getFilters().ifEmpty {
            // Si aucune donnée locale, on retourne une liste par défaut
            val defaultFilters = listOf(
                Filter("harc_sex", "Harcèlement Sexuel", false, R.drawable.ic_launcher_background),
                Filter("spam", "Spam", true, R.drawable.ic_launcher_background),
            )
            filterLocalDataSource.saveFilters(defaultFilters)
            defaultFilters
        }
    }

    // Sauvegarde les filtres après modification
    fun saveFilters(filters: List<Filter>) {
        filterLocalDataSource.saveFilters(filters)
    }
}
