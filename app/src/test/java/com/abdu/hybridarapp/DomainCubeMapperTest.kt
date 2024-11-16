package com.abdu.hybridarapp

import androidx.compose.ui.graphics.Color
import com.abdu.hybridapp.domain.Cube
import com.abdu.hybridapp.domain.Position3d
import com.abdu.hybridarapp.model.DomainCubeMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import com.abdu.hybridapp.domain.Color as DomainColor

class DomainCubeMapperTest {

    @Test
    fun `toCubeUIModel maps domain cube to UI model correctly`() {
        // Given
        val domainCube = Cube(
            name = "test_cube",
            position = Position3d(1f, 2f, 3f),
            color = DomainColor(red = 1f, green = 0f, blue = 0f)
        )

        // When
        val result = DomainCubeMapper.toCubeUIModel(domainCube)

        // Then
        assertEquals("test_cube", result.id)
        assertEquals("test_cube", result.name)
        assertEquals(1f, result.position.x)
        assertEquals(2f, result.position.y)
        assertEquals(3f, result.position.z)
        assertEquals(Color(red = 1f, green = 0f, blue = 0f), result.color)
    }
} 