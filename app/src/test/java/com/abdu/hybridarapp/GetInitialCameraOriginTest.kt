package com.abdu.hybridarapp

import com.abdu.hybridarapp.data.NodesRepository
import com.abdu.hybridarapp.domain.DomainCube
import com.abdu.hybridarapp.domain.GetInitialWorldPosition
import com.abdu.hybridarapp.domain.Position3d
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetInitialCameraOriginTest {

    @Test
    fun testGetInitialCameraOrigin() = runBlocking {
        val nodesRepository = MockNodesRepo()
        nodesRepository.cameraOrigin = Position3d(1.0f, 1.0f, 1.0f)
        val getInitialWorldPosition = GetInitialWorldPosition(nodesRepository)

        assertEquals(Position3d(1.0f, 1.0f, 1.0f), getInitialWorldPosition())
    }
}

class MockNodesRepo : NodesRepository {
    var cameraOrigin: Position3d = Position3d(0.0f, 0.0f, 0.0f)
    private var cubes: List<DomainCube> = emptyList()

    override suspend fun getInitialPosition(): Position3d {
        return cameraOrigin
    }
}