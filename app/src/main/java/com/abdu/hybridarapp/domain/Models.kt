package com.abdu.hybridarapp.domain

import androidx.compose.ui.graphics.Color

data class Position3d(val x: Float, val y: Float, val z: Float) {
    companion object {
        fun combine(a: Position3d, b: Position3d): Position3d {
            return Position3d(a.x + b.x, a.y + b.y, a.z + b.z)
        }
    }
}

data class DomainCube(val name: String, val position: Position3d, val color: Color) {
    fun isEmpty(): Boolean {
        return this.name == "empty"
    }

    companion object {
        fun empty(): DomainCube {
            return DomainCube("empty", Position3d(0f, 0f, 0f), Color.White)
        }
    }
}