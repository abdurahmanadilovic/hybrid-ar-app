package com.abdu.hybridapp.data

import com.abdu.hybridapp.domain.NodesRepository
import com.abdu.hybridapp.domain.Position3d

class NodesRepositoryImpl(
    private val apiService: ApiService
) : NodesRepository {
    private var initialPosition: LocationDTO? = null

    private fun saveInitialPosition(position: LocationDTO) {
        initialPosition = position
    }

    override suspend fun getInitialPosition(): Position3d {
        val finalPosition = initialPosition ?: apiService.getInitialWorldPosition()
        saveInitialPosition(finalPosition)
        return Position3d(
            x = finalPosition.location.x,
            y = finalPosition.location.y,
            z = finalPosition.location.z
        )
    }
}
