package com.abdu.hybridarapp.model

import androidx.compose.ui.graphics.Color
import com.abdu.hybridapp.domain.DomainCube
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
            color = Color(
                red = domainCube.color.red,
                green = domainCube.color.green,
                blue = domainCube.color.blue
            )
        )
    }
}

sealed class PlacementState {
    object Idle : PlacementState()
    object PlacingCube : PlacementState()
    data class CubeSelected(val cubeData: CubeData) : PlacementState()
} 