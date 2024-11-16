package com.abdu.hybridarapp.data

import com.abdu.hybridarapp.domain.Position3d
import retrofit2.http.GET

interface ApiService {
    @GET("/getInitialWorldPosition")
    suspend fun getInitialWorldPosition(): Position3d
}
