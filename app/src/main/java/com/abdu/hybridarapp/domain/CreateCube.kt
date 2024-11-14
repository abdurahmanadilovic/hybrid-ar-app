package com.abdu.hybridarapp.domain

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

class CreateAndAddCube(private val getInitialWorldPosition: GetInitialWorldPosition) {
    suspend operator fun invoke(tapLocation: Position3d): DomainCube {
        val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val randomName = (1..8).map { chars.random() }.joinToString("")

        val newLocation = Position3d.combine(getInitialWorldPosition.invoke(), tapLocation)

        val color = Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat()
        )

        return DomainCube(name = randomName, position = newLocation, color = color)
    }
}