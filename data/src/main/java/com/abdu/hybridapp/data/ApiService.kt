package com.abdu.hybridapp.data

import com.abdu.hybridapp.domain.Position3d
import retrofit2.http.GET

interface ApiService {
    @GET("/getInitialWorldPosition")
    suspend fun getInitialWorldPosition(): LocationDTO

    companion object {
        const val DEV_URL = "https://58ae66b4-e4e5-44f4-903f-fd0dbd2b117f.mock.pstmn.io"
    }
}
