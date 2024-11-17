package com.abdu.hybridapp.data

import com.abdu.hybridapp.domain.Position3d
import retrofit2.http.GET

interface ApiService {
    @GET("/getInitialWorldPosition")
    suspend fun getInitialWorldPosition(): LocationDTO

    companion object {
        const val DEV_URL = "http://localhost:8080"
    }
}
