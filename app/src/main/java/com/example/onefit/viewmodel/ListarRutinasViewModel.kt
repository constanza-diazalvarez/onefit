package com.example.onefit.viewmodel

import androidx.lifecycle.ViewModel
import com.example.onefit.model.RutinasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.asLiveData

@HiltViewModel
class ListarRutinasViewModel @Inject constructor(
    private val repository: RutinasRepository
) : ViewModel() {

    init {
        android.util.Log.d("VIEWMODEL_DEBUG", "ViewModel creado correctamente")
    }
    val todasLasRutinas = repository.getAllRutinas().asLiveData()
}