package com.abdu.hybridapp.data

import com.google.gson.annotations.SerializedName

data class LocationDTO(
    @SerializedName("Location")
    val location: Location
)

data class Location(
    @SerializedName("X")
    val x: Float,
    @SerializedName("Y")
    val y: Float,
    @SerializedName("Z")
    val z: Float
)
