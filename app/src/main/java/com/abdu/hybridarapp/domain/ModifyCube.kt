package com.abdu.hybridarapp.domain

import androidx.compose.ui.graphics.Color
import com.abdu.hybridarapp.data.NodesRepository

class ModifyCube(private val nodesRepository: NodesRepository) {
    suspend fun invoke(domainCube: DomainCube, color: Color) {
        val newCube = domainCube.copy(color = color)
        nodesRepository.storeCube(newCube.name, newCube)
    }
}