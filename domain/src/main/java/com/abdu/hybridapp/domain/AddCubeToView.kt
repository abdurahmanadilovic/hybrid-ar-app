package com.abdu.hybridapp.domain

class AddCubeToViewImpl(
    private val createAndAddCube: CreateAndAddCubeUseCase
) : AddCubeToViewUseCase {
    override suspend operator fun invoke(tapLocation: Position3d): Cube {
        return createAndAddCube(tapLocation)
    }
}