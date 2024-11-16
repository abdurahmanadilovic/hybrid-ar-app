package com.abdu.hybridapp.domain

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreateAndAddCubeUseCaseTest {
    @Test
    fun `when creating cube, position should be combined with initial world position`() = runBlocking {
        // Given
        val initialPosition = Position3d(1.0f, 1.0f, 1.0f)
        val tapPosition = Position3d(2.0f, 2.0f, 2.0f)
        val mockGetInitialPosition = MockGetInitialWorldPositionUseCase(initialPosition)
        val useCase = CreateAndAddCubeImpl(mockGetInitialPosition)

        // When
        val result = useCase(tapPosition)

        // Then
        assertEquals(Position3d(3.0f, 3.0f, 3.0f), result.position)
    }
}

private class MockGetInitialWorldPositionUseCase(
    private val positionToReturn: Position3d
) : GetInitialWorldPositionUseCase {
    override suspend fun invoke(): Position3d = positionToReturn
} 