package com.example.myapplication.module

import com.example.myapplication.MainViewModel
import com.example.myapplication.details.DetailsViewModel
import com.example.myapplication.repository.GameRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val gameModule = module{
    single { GameRepository() }

    viewModel {MainViewModel(get())}
    viewModel {DetailsViewModel(get())}
}