package com.abdu.hybridarapp.model

import androidx.compose.ui.graphics.Color
import com.abdu.hybridarapp.domain.DomainCube
import com.abdu.hybridarapp.presentation.Float3Mapper
import dev.romainguy.kotlin.math.Float3

data class CubeData(
    val id: String,
    val name: String,
    val position: Float3,
    val color: Color
)

object DomainCubeMapper {
    fun toCubeData(domainCube: DomainCube): CubeData {
        return CubeData(
            id = domainCube.name,
            name = domainCube.name,
            position = Float3Mapper.toFloat3(domainCube.position),
            color = domainCube.color
        )
    }
}

sealed class PlacementState {
    object Idle : PlacementState()
    object PlacingCube : PlacementState()
    data class CubeSelected(val cubeData: CubeData) : PlacementState()
} 