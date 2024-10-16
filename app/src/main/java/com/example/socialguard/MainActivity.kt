package com.example.socialguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.socialguard.ui.theme.SocialGuardTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: FilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filterLocalDataSource = FilterLocalDataSource(this)
        val getFiltersUseCase = GetFiltersUseCase(filterLocalDataSource)

        viewModel = ViewModelProvider(this, ViewModelFactory(getFiltersUseCase))[FilterViewModel::class.java]

        setContent {
            FilterListScreen(viewModel = viewModel)
        }
    }
}
