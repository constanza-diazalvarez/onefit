package com.example.onefit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onefit.model.RelacionRutinaConEjercicios // O 'RutinaDetallada'
import com.example.onefit.model.RutinasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RutinaDetalleViewModel @Inject constructor(
    private val repository: RutinasRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _rutinaDetalle = MutableStateFlow<RelacionRutinaConEjercicios?>(null)
    val rutinaDetalle = _rutinaDetalle.asStateFlow()

    init {
        val rutinaId: Int? = savedStateHandle.get("rutinaId")

        if (rutinaId != null) {
            viewModelScope.launch {
                repository.getRutinaDetallada(rutinaId).collect { detalle ->
                    _rutinaDetalle.value = detalle
                }
            }
        }
    }
}