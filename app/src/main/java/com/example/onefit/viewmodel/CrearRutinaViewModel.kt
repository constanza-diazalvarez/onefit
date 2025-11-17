package com.example.onefit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onefit.model.Rutina
import com.example.onefit.model.RutinasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrearRutinaViewModel @Inject constructor(
    private val repository: RutinasRepository
) : ViewModel() {

    private val _nombre = MutableStateFlow("")
    val nombre = _nombre.asStateFlow()

    private val _descripcion = MutableStateFlow("")
    val descripcion = _descripcion.asStateFlow()

    private val _errorNombre = MutableStateFlow<String?>(null)
    val errorNombre = _errorNombre.asStateFlow()

    private val _navegarSiguiente = MutableStateFlow<Long?>(null)
    val navegarSiguiente = _navegarSiguiente.asStateFlow()

    fun onNombreChange(nuevoNombre: String) {
        _nombre.value = nuevoNombre
        // Si había un error, lo borramos al escribir
        if (errorNombre.value != null) {
            _errorNombre.value = null
        }
    }

    fun onDescripcionChange(nuevaDescripcion: String) {
        _descripcion.value = nuevaDescripcion
    }

    fun onSiguienteClick() {
        val nombreActual = _nombre.value

        if (nombreActual.isBlank()) {
            _errorNombre.value = "El nombre no puede estar vacío"
            return
        }

        val nuevaRutina = Rutina(
            nombre = nombreActual,
            descripcion = _descripcion.value.takeIf { it.isNotBlank() } // null si está vacío
        )

        viewModelScope.launch {
            val nuevoId = repository.insertRutina(nuevaRutina)

            _navegarSiguiente.value = nuevoId
        }
    }

    fun onNavegacionCompletada() {
        _navegarSiguiente.value = null
    }
}