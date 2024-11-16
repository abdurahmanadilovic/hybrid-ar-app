package com.abdu.hybridapp.domain

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AddCubeToViewUseCaseTest {
    @Test
    fun `when adding cube, should delegate to CreateAndAddCube use case`() = runBlocking {
        // Given
        val tapPosition = Position3d(1.0f, 1.0f, 1.0f)
        val expectedCube = Cube.empty()
        val mockCreateAndAddCube = MockCreateAndAddCubeUseCase(expectedCube)
        val useCase = AddCubeToViewImpl(mockCreateAndAddCube)

        // When
        val result = useCase(tapPosition)

        // Then
        assertEquals(expectedCube, result)
        assertEquals(tapPosition, mockCreateAndAddCube.lastTapPosition)
    }
}

private class MockCreateAndAddCubeUseCase(
    private val cubeToReturn: Cube
) : CreateAndAddCubeUseCase {
    var lastTapPosition: Position3d? = null
        private set

    override suspend fun invoke(tapLocation: Position3d): Cube {
        lastTapPosition = tapLocation
        return cubeToReturn
    }
}