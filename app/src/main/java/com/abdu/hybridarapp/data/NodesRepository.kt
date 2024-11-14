package com.abdu.hybridarapp.data

import com.abdu.hybridarapp.domain.DomainCube
import com.abdu.hybridarapp.domain.Position3d

interface NodesRepository {
    suspend fun saveInitialPosition(position: Position3d)
    suspend fun getInitialPosition(): Position3d
    suspend fun storeCube(id: String, domainCube: DomainCube)
    suspend fun getCube(id: String): DomainCube
    suspend fun getCubes(): List<DomainCube>
}

class NodesRepositoryImpl(private val apiService: ApiService) : NodesRepository {
    private var initialPosition: Position3d? = null
    private val cubes = mutableMapOf<String, DomainCube>()

    override suspend fun saveInitialPosition(position: Position3d) {
        initialPosition = position
    }

    override suspend fun getInitialPosition(): Position3d {
        val finalPosition = initialPosition ?: apiService.getInitialWorldPosition()
        saveInitialPosition(finalPosition)
        return finalPosition
    }

    override suspend fun storeCube(id: String, domainCube: DomainCube) {
        cubes[id] = domainCube
    }

    override suspend fun getCube(id: String): DomainCube {
        return cubes[id] ?: DomainCube.empty()
    }

    override suspend fun getCubes(): List<DomainCube> {
        return cubes.values.toList()
    }
}
