package com.abdu.hybridapp.domain

interface GetInitialWorldPositionUseCase {
    suspend operator fun invoke(): Position3d
}

interface CreateAndAddCubeUseCase {
    suspend operator fun invoke(tapLocation: Position3d): DomainCube
}

interface AddCubeToViewUseCase {
    suspend operator fun invoke(tapLocation: Position3d): DomainCube
}

interface CalculateArrowAngleUseCase {
    operator fun invoke(
        arrowCenterX: Float,
        arrowCenterY: Float,
        targetX: Float,
        targetY: Float
    ): Float
} 