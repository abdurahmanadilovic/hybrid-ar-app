package com.abdu.hybridarapp.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdu.hybridarapp.model.CubeData
import com.abdu.hybridarapp.model.DomainCubeMapper
import com.abdu.hybridarapp.presentation.Float3Mapper
import dev.romainguy.kotlin.math.Float3
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlacementViewModel(
    private val addCubeToViewUseCase: com.abdu.hybridapp.domain.AddCubeToViewUseCase,
    private val getInitialWorldPositionUseCase: com.abdu.hybridapp.domain.GetInitialWorldPositionUseCase,
    private val calculateArrowAngleUseCase: com.abdu.hybridapp.domain.CalculateArrowAngleUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ARViewState())
    val state: StateFlow<ARViewState> = _state

    private val _configState = MutableStateFlow(ConfigState())
    val configState: StateFlow<ConfigState> = _configState

    init {
        getAppConfig()
    }

    private fun getAppConfig() {
        viewModelScope.launch {
            try {
                _configState.value = _configState.value.copy(isLoading = true)
                val cameraOrigin = getInitialWorldPositionUseCase()
                _configState.value = ConfigState(
                    cameraOrigin = Float3Mapper.toFloat3(cameraOrigin),
                    isLoading = false
                )
            } catch (ex: Exception) {
                _configState.value = ConfigState(
                    error = ex,
                    isLoading = false
                )
            }
        }
    }

    fun retryInitialization() {
        _configState.value = ConfigState(isLoading = true)
        getAppConfig()
    }

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

    fun changeSelectedCubeColor(color: Color) {
        val updatedCubes = _state.value.cubes.map { cube ->
            if (cube.id == _state.value.selectedCube?.id) {
                cube.copy(color = color)
            } else cube
        }
        _state.value = _state.value.copy(cubes = updatedCubes)
    }

    fun updateArrowAngle(
        arrowCenterX: Float,
        arrowCenterY: Float,
        targetX: Float,
        targetY: Float
    ) {
        val angle = calculateArrowAngleUseCase(
            arrowCenterX,
            arrowCenterY,
            targetX,
            targetY
        )
        _state.value = _state.value.copy(arrowAngle = angle)
    }
}

data class ARViewState(
    val cubes: List<CubeData> = emptyList(),
    val selectedCube: CubeData? = null,
    val arrowAngle: Float = 0f,
)

data class ConfigState(
    val cameraOrigin: Float3? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false
)