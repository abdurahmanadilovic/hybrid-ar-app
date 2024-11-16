package com.abdu.hybridapp.domain

import kotlin.math.atan2

class CalculateArrowAngle : CalculateArrowAngleUseCase {
    override fun invoke(
        arrowCenterX: Float,
        arrowCenterY: Float,
        targetX: Float,
        targetY: Float
    ): Float {
        val deltaX = targetX - arrowCenterX
        val deltaY = targetY - arrowCenterY
        return Math.toDegrees(atan2(deltaY.toDouble(), deltaX.toDouble())).toFloat()
    }
} 