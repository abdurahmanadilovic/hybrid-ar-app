package com.abdu.hybridapp.domain

class AddCubeToViewImpl(
    private val createCube: CreateCubeUseCase,
    private val getInitialWorldPositionUseCase: GetInitialWorldPositionUseCase
) : AddCubeToViewUseCase {
    override suspend operator fun invoke(tapLocation: Position3d): Cube {
        return createCube(
            tapLocation = tapLocation,
            offsetOrigin = getInitialWorldPositionUseCase()
        )
    }
}