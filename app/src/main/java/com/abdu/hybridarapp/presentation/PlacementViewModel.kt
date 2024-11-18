package com.abdu.hybridarapp.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdu.hybridapp.domain.AddCubeToViewUseCase
import com.abdu.hybridapp.domain.CalculateArrowAngleUseCase
import com.abdu.hybridapp.domain.GetInitialWorldPositionUseCase
import com.abdu.hybridarapp.model.CubeUIModel
import com.abdu.hybridarapp.model.DomainCubeMapper
import com.abdu.hybridarapp.model.Position3dMapper
import com.google.ar.core.Plane
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.arcore.position
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlacementViewModel(
    private val addCubeToViewUseCase: AddCubeToViewUseCase,
    private val getInitialWorldPositionUseCase: GetInitialWorldPositionUseCase,
    private val calculateArrowAngleUseCase: CalculateArrowAngleUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ARViewState())
    val state: StateFlow<ARViewState> = _state

    private val _configState = MutableStateFlow(ConfigState())
    val configState: StateFlow<ConfigState> = _configState

    var trackedPlanes = listOf<Plane>()
    private var currentPlaneIndex = 0

    init {
        getAppConfig()
    }

    private fun getAppConfig() {
        viewModelScope.launch {
            try {
                _configState.value = _configState.value.copy(isLoading = true)
                val cameraOrigin = getInitialWorldPositionUseCase()
                _configState.value = ConfigState(
                    cameraOrigin = Position3dMapper.toFloat3(cameraOrigin),
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

    private fun placeCube(position: Float3) {
        viewModelScope.launch {
            val newDomainCube = addCubeToViewUseCase(Position3dMapper.from(position))
            val newUICube = DomainCubeMapper.toCubeUIModel(newDomainCube)
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

    fun updateTrackedPlanes(planes: List<Plane>) {
        if (planes.isEmpty()) return
        trackedPlanes = planes
        currentPlaneIndex = currentPlaneIndex.coerceIn(0, trackedPlanes.size - 1)
    }

    fun placeNextCube() {
        val trackedPlanes =
            trackedPlanes.filter { it.trackingState == com.google.ar.core.TrackingState.TRACKING }
        if (trackedPlanes.isEmpty()) return

        val plane = trackedPlanes[currentPlaneIndex % trackedPlanes.size]
        val planeCenter = plane.centerPose.position

        // Generate random offset within the plane boundaries
        val randomX = (Math.random() * 0.6 - 0.3).toFloat()
        val randomZ = (Math.random() * 0.6 - 0.3).toFloat()

        val position = Float3(
            planeCenter.x + randomX,
            planeCenter.y,
            planeCenter.z + randomZ
        )

        placeCube(position)
        currentPlaneIndex = (currentPlaneIndex + 1) % trackedPlanes.size
    }
}

data class ARViewState(
    val cubes: List<CubeUIModel> = emptyList(),
    val selectedCube: CubeUIModel? = null,
    val arrowAngle: Float = 0f,
)

data class ConfigState(
    val cameraOrigin: Float3? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false
)