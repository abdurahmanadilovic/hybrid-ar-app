package com.abdu.hybridarapp.data

import com.abdu.hybridarapp.domain.Position3d

interface NodesRepository {
    suspend fun getInitialPosition(): Position3d
}

class NodesRepositoryImpl(private val apiService: ApiService) : NodesRepository {
    private var initialPosition: Position3d? = null

    private fun saveInitialPosition(position: Position3d) {
        initialPosition = position
    }

    override suspend fun getInitialPosition(): Position3d {
        val finalPosition = initialPosition ?: apiService.getInitialWorldPosition()
        saveInitialPosition(finalPosition)
        return finalPosition
    }
}
