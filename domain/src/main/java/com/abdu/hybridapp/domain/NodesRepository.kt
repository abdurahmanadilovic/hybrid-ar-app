package com.abdu.hybridapp.domain

interface NodesRepository {
    suspend fun getInitialPosition(): Position3d
}