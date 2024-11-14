package com.abdu.hybridarapp.domain

import com.abdu.hybridarapp.data.NodesRepository

class GetAllCubes(private val repository: NodesRepository) {
    suspend fun invoke(): List<DomainCube> {
        return repository.getCubes()
    }
}