package com.abdu.hybridarapp.model

import com.abdu.hybridapp.domain.Position3d
import dev.romainguy.kotlin.math.Float3

object Position3dMapper {
    fun toFloat3(position: Position3d): Float3 {
        return Float3(position.x, position.y, position.z)
    }

    fun from(float3: Float3): Position3d {
        return Position3d(float3.x, float3.y, float3.z)
    }
}