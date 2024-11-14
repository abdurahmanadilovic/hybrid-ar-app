package com.abdu.hybridarapp.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdu.hybridarapp.domain.AddCubeToView
import com.abdu.hybridarapp.model.CubeData
import com.abdu.hybridarapp.model.DomainCubeMapper
import com.abdu.hybridarapp.presentation.Float3Mapper
import dev.romainguy.kotlin.math.Float3
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class PlacementViewModel(private val addCube: AddCubeToView) : ViewModel() {
    private val _state = MutableStateFlow<ARViewState>(ARViewState())
    val state: StateFlow<ARViewState> = _state

    private val _cubes = MutableStateFlow<List<CubeData>>(emptyList())
    val cubes: StateFlow<List<CubeData>> = _cubes

    fun placeCube(position: Float3) {
        viewModelScope.launch {
            val addedCube = addCube(Float3Mapper.toPosition3d(position))
            val newCube = CubeData(
                id = generateId(),
                name = generateRandomName(),
                position = position,
                color = generateRandomColor()
            )
            _cubes.value += newCube
            _state.value = _state.value.copy(
                cubes = _state.value.cubes + listOf(
                    DomainCubeMapper.toCubeData(addedCube)
                )
            )
        }
    }

    fun selectCube(cubeId: String) {
        _cubes.value.find { it.id == cubeId }?.let { cube ->
            _state.value = _state.value.copy(selectedCube = cube)
        }
    }

    fun changeCubeColor(cubeId: String, color: Color) {
        val updatedCubes = _cubes.value.map { cube ->
            if (cube.id == cubeId) {
                cube.copy(color = color)
            } else cube
        }
        _cubes.value = updatedCubes
    }

    private fun generateId() = Random.nextInt(1000000).toString()

    private fun generateRandomName(): String {
        val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..8).map { chars.random() }.joinToString("")
    }

    private fun generateRandomColor(): Color {
        return Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat()
        )
    }
}

data class ARViewState(
    val cubes: List<CubeData> = emptyList(),
    val selectedCube: CubeData? = null,
    val cameraOrigin: Float3 = Float3(0f, 0f, 0f),
    val arrowAngle: Float = 0f,
)