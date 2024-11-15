package com.abdu.hybridarapp.domain

import com.abdu.hybridarapp.data.NodesRepository

class GetInitialWorldPositionImpl(
    private val nodesRepository: NodesRepository
) : GetInitialWorldPositionUseCase {
    override suspend operator fun invoke(): Position3d {
        return nodesRepository.getInitialPosition()
    }
}