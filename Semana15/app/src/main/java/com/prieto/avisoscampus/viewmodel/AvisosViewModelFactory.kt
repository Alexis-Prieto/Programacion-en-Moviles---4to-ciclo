package com.prieto.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prieto.avisoscampus.data.AvisosRepository

class AvisosViewModelFactory(
    private val repo: AvisosRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(
                AvisosViewModel::class.java
            )
        ) {
            return AvisosViewModel(repo) as T
        }
        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}