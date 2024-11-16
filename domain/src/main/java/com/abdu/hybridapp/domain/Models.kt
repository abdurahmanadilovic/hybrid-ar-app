package com.abdu.hybridapp.domain

data class Position3d(val x: Float, val y: Float, val z: Float) {
    companion object {
        fun combine(a: Position3d, b: Position3d): Position3d {
            return Position3d(a.x + b.x, a.y + b.y, a.z + b.z)
        }
    }
}

data class Color(val red: Float, val green: Float, val blue: Float) {
    companion object {
        val White = Color(1f, 1f, 1f)
        val Black = Color(0f, 0f, 0f)
        val Red = Color(1f, 0f, 0f)
        val Green = Color(0f, 1f, 0f)
        val Blue = Color(0f, 0f, 1f)
        val Yellow = Color(1f, 1f, 0f)
        val Magenta = Color(1f, 0f, 1f)
    }
}

data class Cube(val name: String, val position: Position3d, val color: Color) {
    fun isEmpty(): Boolean {
        return this.name == "empty"
    }

    companion object {
        fun empty(): Cube {
            return Cube("empty", Position3d(0f, 0f, 0f), Color.White)
        }
    }
}