package com.example.onefit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onefit.model.RelacionRegistroEntrenamientoConSerie
import com.example.onefit.model.RutinasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleHistorialViewModel @Inject constructor(
    private val repository: RutinasRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val registroId: Int = checkNotNull(savedStateHandle.get<Int>("registroId"))

    private val _detalle = MutableStateFlow<RelacionRegistroEntrenamientoConSerie?>(null)
    val detalle = _detalle.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getRegistroDetallado(registroId).collect {
                _detalle.value = it
            }
        }
    }
}