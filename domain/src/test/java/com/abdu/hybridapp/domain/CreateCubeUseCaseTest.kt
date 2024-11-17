package com.abdu.hybridapp.domain

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreateCubeUseCaseTest {
    @Test
    fun `when creating cube, position should be combined with initial world position`() = runBlocking {
        // Given
        val initialPosition = Position3d(1.0f, 1.0f, 1.0f)
        val tapPosition = Position3d(2.0f, 2.0f, 2.0f)
        val useCase = CreateCubeImpl()

        // When
        val result = useCase(tapPosition, initialPosition)

        // Then
        assertEquals(Position3d(3.0f, 3.0f, 3.0f), result.position)
    }
}
