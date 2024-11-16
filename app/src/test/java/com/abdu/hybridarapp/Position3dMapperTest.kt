package com.abdu.hybridarapp

import com.abdu.hybridapp.domain.Position3d
import com.abdu.hybridarapp.model.Position3dMapper
import dev.romainguy.kotlin.math.Float3
import org.junit.Assert.assertEquals
import org.junit.Test

class Position3dMapperTest {

    @Test
    fun `toFloat3 maps Position3d to Float3 correctly`() {
        // Given
        val position = Position3d(1f, 2f, 3f)

        // When
        val result = Position3dMapper.toFloat3(position)

        // Then
        assertEquals(1f, result.x)
        assertEquals(2f, result.y)
        assertEquals(3f, result.z)
    }

    @Test
    fun `from maps Float3 to Position3d correctly`() {
        // Given
        val float3 = Float3(1f, 2f, 3f)

        // When
        val result = Position3dMapper.from(float3)

        // Then
        assertEquals(1f, result.x)
        assertEquals(2f, result.y)
        assertEquals(3f, result.z)
    }
} 