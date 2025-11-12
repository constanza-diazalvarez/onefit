package com.example.onefit.viewmodel

import androidx.lifecycle.ViewModel
import com.example.onefit.model.EntrenamientoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.asLiveData

@HiltViewModel
class ListarRutinasViewModel @Inject constructor(
    private val repository: EntrenamientoRepository
) : ViewModel() {
    val todasLasRutinas = repository.getAllRutinas().asLiveData()
}