package com.abdu.hybridarapp.domain

import com.abdu.hybridarapp.data.NodesRepository

class GetInitialWorldPosition(private val nodesRepository: NodesRepository) {
    suspend operator fun invoke(): Position3d {
        return nodesRepository.getInitialPosition()
    }
}