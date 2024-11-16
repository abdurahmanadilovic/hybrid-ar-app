package com.abdu.hybridapp.domain

class GetInitialWorldPositionImpl(
    private val nodesRepository: NodesRepository
) : GetInitialWorldPositionUseCase {
    override suspend operator fun invoke(): Position3d {
        return nodesRepository.getInitialPosition()
    }
}