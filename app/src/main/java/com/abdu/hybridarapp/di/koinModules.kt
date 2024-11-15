package com.abdu.hybridarapp.di

import com.abdu.hybridarapp.data.ApiService
import com.abdu.hybridarapp.data.ApiServiceImpl
import com.abdu.hybridarapp.data.NodesRepository
import com.abdu.hybridarapp.data.NodesRepositoryImpl
import com.abdu.hybridarapp.domain.AddCubeToView
import com.abdu.hybridarapp.domain.CreateAndAddCube
import com.abdu.hybridarapp.domain.GetInitialWorldPosition
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetInitialWorldPosition)
    singleOf(::CreateAndAddCube)
    singleOf(::AddCubeToView)
}

val networkModule = module {
    singleOf(::ApiServiceImpl) { bind<ApiService>() }
    singleOf(::NodesRepositoryImpl) { bind<NodesRepository>() }
}

val koinModules = listOf(
    domainModule,
    networkModule,
)