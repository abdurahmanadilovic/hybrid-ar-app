package com.abdu.hybridapp.domain

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CreateCubeUseCaseTest {
    private lateinit var createCube: CreateCubeImpl

    @Before
    fun setup() {
        createCube = CreateCubeImpl()
    }

    @Test
    fun `when creating cube, offset origin should be saved with initial world position`() =
        runBlocking {
            // Given
            val initialPosition = Position3d(1.0f, 1.0f, 1.0f)
            val tapPosition = Position3d(2.0f, 2.0f, 2.0f)
            val useCase = CreateCubeImpl()

            // When
            val result = useCase(tapPosition, initialPosition)

            // Then
            assertEquals(initialPosition, result.offsetOrigin)
            assertEquals(tapPosition, result.position)
        }

    @Test
    fun `create cube generates random color from available colors`() = runBlocking {
        // Given
        val tapLocation = Position3d(1.0f, 2.0f, 3.0f)
        val offsetOrigin = Position3d(0.0f, 0.0f, 0.0f)
        val availableColors = setOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta)

        // When
        val cube = createCube(tapLocation, offsetOrigin)

        // Then
        assert(availableColors.contains(cube.color))
    }

    @Test
    fun `create cube generates unique names for multiple cubes`() = runBlocking {
        // Given
        val tapLocation = Position3d(1.0f, 2.0f, 3.0f)
        val offsetOrigin = Position3d(0.0f, 0.0f, 0.0f)

        // When
        val cube1 = createCube(tapLocation, offsetOrigin)
        val cube2 = createCube(tapLocation, offsetOrigin)

        // Then
        assert(cube1.name != cube2.name)
    }
}
