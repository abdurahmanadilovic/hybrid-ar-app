package com.abdu.hybridapp.domain

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetInitialWorldPositionUseCaseTest {
    @Test
    fun `when repository returns position, use case should return same position`() = runBlocking {
        // Given
        val expectedPosition = Position3d(1.0f, 2.0f, 3.0f)
        val mockRepo = MockNodesRepository(expectedPosition)
        val useCase = GetInitialWorldPositionImpl(mockRepo)

        // When
        val result = useCase()

        // Then
        assertEquals(expectedPosition, result)
    }
}

private class MockNodesRepository(private val positionToReturn: Position3d) : NodesRepository {
    override suspend fun getInitialPosition(): Position3d = positionToReturn
} 