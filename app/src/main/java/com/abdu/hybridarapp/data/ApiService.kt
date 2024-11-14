package com.abdu.hybridarapp.data

import com.abdu.hybridarapp.domain.Position3d

interface ApiService {
    suspend fun getInitialWorldPosition(): Position3d
}

class ApiServiceImpl : ApiService {
    override suspend fun getInitialWorldPosition(): Position3d {
        return Position3d(0f, 0f, 0f)
    }
}