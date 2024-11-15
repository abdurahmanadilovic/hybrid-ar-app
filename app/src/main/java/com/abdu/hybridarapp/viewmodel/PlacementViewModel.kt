package com.abdu.hybridarapp.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdu.hybridarapp.domain.AddCubeToViewUseCase
import com.abdu.hybridarapp.model.CubeData
import com.abdu.hybridarapp.model.DomainCubeMapper
import com.abdu.hybridarapp.presentation.Float3Mapper
import dev.romainguy.kotlin.math.Float3
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlacementViewModel(private val addCubeToViewUseCase: AddCubeToViewUseCase) : ViewModel() {
    private val _state = MutableStateFlow(ARViewState())
    val state: StateFlow<ARViewState> = _state

    fun placeCube(position: Float3) {
        viewModelScope.launch {
            val newDomainCube = addCubeToViewUseCase(Float3Mapper.toPosition3d(position))
            val newUICube = DomainCubeMapper.toCubeData(newDomainCube)
            _state.value = _state.value.copy(
                cubes = _state.value.cubes + listOf(newUICube)
            )
        }
    }

    fun selectCube(cubeId: String) {
        _state.value.cubes.find { it.id == cubeId }?.let { cube ->
            _state.value = _state.value.copy(selectedCube = cube)
        }
    }

    fun changeCubeColor(color: Color) {
        val updatedCubes = _state.value.cubes.map { cube ->
            if (cube.id == _state.value.selectedCube?.id) {
                cube.copy(color = color)
            } else cube
        }
        _state.value = _state.value.copy(cubes = updatedCubes)
    }
}

data class ARViewState(
    val cubes: List<CubeData> = emptyList(),
    val selectedCube: CubeData? = null,
    val cameraOrigin: Float3 = Float3(0f, 0f, 0f),
    val arrowAngle: Float = 0f,
)