package com.abdu.hybridarapp.domain

interface GetInitialWorldPositionUseCase {
    suspend operator fun invoke(): Position3d
}

interface CreateAndAddCubeUseCase {
    suspend operator fun invoke(tapLocation: Position3d): DomainCube
}

interface AddCubeToViewUseCase {
    suspend operator fun invoke(tapLocation: Position3d): DomainCube
} 