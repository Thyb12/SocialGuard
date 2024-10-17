package com.example.socialguard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val getFiltersUseCase: GetFiltersUseCase, private val profanityFilterManager: ProfanityFilterManager) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            return FilterViewModel(getFiltersUseCase,profanityFilterManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
