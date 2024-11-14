package com.abdu.hybridarapp.domain

interface ARView {
    fun addCube(domainCube: DomainCube)
    fun removeCube(id: String)
    fun setCameraOrigin(position3d: Position3d)
}