package com.abdu.hybridarapp.model

import androidx.compose.ui.graphics.Color
import dev.romainguy.kotlin.math.Float3

data class CubeUIModel(
    val id: String,
    val name: String,
    val position: Float3,
    val color: Color
)
