package com.abdu.hybridapp.domain

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AddCubeToViewUseCaseTest {
    @Test
    fun `when adding cube, should fetch origin position and pass it to CreateAndAddCube use case`() =
        runBlocking {
            // Given
            val tapPosition = Position3d(1f, 1f, 1f)
            val originPosition = Position3d(1f, 2f, 3f)

            val mockCreateCube = MockCreateCubeUseCase()

            val mockGetOriginPosition = MockGetInitialWorldPositionUseCase(originPosition)
            val addCubeToView = AddCubeToViewImpl(mockCreateCube, mockGetOriginPosition)

            // When
            val result = addCubeToView(tapPosition)

            // Then
            assertEquals(originPosition, mockCreateCube.givenOriginPosition)
        }
}

class MockGetInitialWorldPositionUseCase(
    private val positionToReturn: Position3d
) : GetInitialWorldPositionUseCase {
    override suspend fun invoke(): Position3d = positionToReturn
}

private class MockCreateCubeUseCase : CreateCubeUseCase {
    var givenOriginPosition: Position3d? = null
        private set

    override suspend fun invoke(tapLocation: Position3d, offsetOrigin: Position3d): Cube {
        givenOriginPosition = offsetOrigin
        return Cube.empty().copy(position = Position3d.combine(tapLocation, offsetOrigin))
    }
}