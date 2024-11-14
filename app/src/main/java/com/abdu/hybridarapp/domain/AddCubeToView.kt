package com.abdu.hybridarapp.domain

class AddCubeToView(val createAndAddCube: CreateAndAddCube) {
    suspend operator fun invoke(tapLocation: Position3d): DomainCube {
        val newCube = createAndAddCube(tapLocation)
        return newCube
    }
}