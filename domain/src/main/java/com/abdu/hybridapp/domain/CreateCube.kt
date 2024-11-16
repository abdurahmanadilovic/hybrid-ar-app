package com.abdu.hybridapp.domain

import kotlin.random.Random

class CreateAndAddCubeImpl(
    private val getInitialWorldPosition: GetInitialWorldPositionUseCase
) : CreateAndAddCubeUseCase {
    override suspend operator fun invoke(tapLocation: Position3d): Cube {
        val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val randomName = (1..8).map { chars.random() }.joinToString("")

        val newLocation = Position3d.combine(getInitialWorldPosition(), tapLocation)

        val color = when (Random.nextInt(5)) {
            0 -> Color.Red
            1 -> Color.Green
            2 -> Color.Blue
            3 -> Color.Yellow
            else -> Color.Magenta
        }


        return Cube(name = randomName, position = newLocation, color = color)
    }
}