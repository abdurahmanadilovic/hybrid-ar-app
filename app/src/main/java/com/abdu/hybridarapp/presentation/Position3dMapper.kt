package com.abdu.hybridarapp.presentation

import com.abdu.hybridarapp.domain.Position3d
import dev.romainguy.kotlin.math.Float3

object Float3Mapper {
    fun toFloat3(position: Position3d): Float3 {
        return Float3(position.x, position.y, position.z)
    }

    fun toPosition3d(float3: Float3): Position3d {
        return Position3d(float3.x, float3.y, float3.z)
    }
}