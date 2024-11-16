package com.abdu.hybridapp.data

import com.abdu.hybridapp.domain.NodesRepository

class NodesRepositoryImpl(
    private val apiService: ApiService
) : NodesRepository {
    private var initialPosition: com.abdu.hybridapp.domain.Position3d? =
        com.abdu.hybridapp.domain.Position3d(0f, 0f, 0f)

    private fun saveInitialPosition(position: com.abdu.hybridapp.domain.Position3d) {
        initialPosition = position
    }

    override suspend fun getInitialPosition(): com.abdu.hybridapp.domain.Position3d {
        val finalPosition = initialPosition ?: apiService.getInitialWorldPosition()
        saveInitialPosition(finalPosition)
        return finalPosition
    }
}
