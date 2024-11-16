package com.abdu.hybridarapp.model

import androidx.compose.ui.graphics.Color
import com.abdu.hybridapp.domain.Cube

object DomainCubeMapper {
    fun toCubeUIModel(domainCube: Cube): CubeUIModel {
        return CubeUIModel(
            id = domainCube.name,
            name = domainCube.name,
            position = Position3dMapper.toFloat3(domainCube.position),
            color = Color(
                red = domainCube.color.red,
                green = domainCube.color.green,
                blue = domainCube.color.blue
            )
        )
    }
}