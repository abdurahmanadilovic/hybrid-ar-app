package com.abdu.hybridarapp

import com.abdu.hybridarapp.domain.CalculateArrowAngleImpl
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.roundToInt

class CalculateArrowAngleUseCaseTest {
    private val calculateArrowAngle = CalculateArrowAngleImpl()

    @Test
    fun `when target is to the right, angle should be 0 degrees`() {
        val angle = calculateArrowAngle(0f, 0f, 1f, 0f)
        assertEquals(0, angle.roundToInt())
    }

    @Test
    fun `when target is above, angle should be -90 degrees`() {
        val angle = calculateArrowAngle(0f, 0f, 0f, -1f)
        assertEquals(-90, angle.roundToInt())
    }

    @Test
    fun `when target is to the left, angle should be 180 degrees`() {
        val angle = calculateArrowAngle(0f, 0f, -1f, 0f)
        assertEquals(180, angle.roundToInt())
    }

    @Test
    fun `when target is below, angle should be 90 degrees`() {
        val angle = calculateArrowAngle(0f, 0f, 0f, 1f)
        assertEquals(90, angle.roundToInt())
    }

    @Test
    fun `when target is at 45 degrees, angle should be 45 degrees`() {
        val angle = calculateArrowAngle(0f, 0f, 1f, 1f)
        assertEquals(45, angle.roundToInt())
    }
} 