package com.abdu.hybridapp.domain

interface GetInitialWorldPositionUseCase {
    suspend operator fun invoke(): Position3d
}

interface CreateCubeUseCase {
    suspend operator fun invoke(tapLocation: Position3d, offsetOrigin: Position3d): Cube
}

interface AddCubeToViewUseCase {
    suspend operator fun invoke(tapLocation: Position3d): Cube
}

interface CalculateArrowAngleUseCase {
    operator fun invoke(
        arrowCenterX: Float,
        arrowCenterY: Float,
        targetX: Float,
        targetY: Float
    ): Float
} 