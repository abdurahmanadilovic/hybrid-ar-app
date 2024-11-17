package com.abdu.hybridapp.domain

class AddCubeToViewImpl(
    private val createAndAddCube: CreateCubeUseCase,
    private val getInitialWorldPositionUseCase: GetInitialWorldPositionUseCase
) : AddCubeToViewUseCase {
    override suspend operator fun invoke(tapLocation: Position3d): Cube {
        return createAndAddCube(
            tapLocation = tapLocation,
            originPosition = getInitialWorldPositionUseCase()
        )
    }
}