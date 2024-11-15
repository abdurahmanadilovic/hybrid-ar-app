package com.abdu.hybridarapp.di

import com.abdu.hybridarapp.data.ApiService
import com.abdu.hybridarapp.data.ApiServiceImpl
import com.abdu.hybridarapp.data.NodesRepository
import com.abdu.hybridarapp.data.NodesRepositoryImpl
import com.abdu.hybridarapp.domain.*
import com.abdu.hybridarapp.viewmodel.PlacementViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetInitialWorldPositionImpl) { bind<GetInitialWorldPositionUseCase>() }
    singleOf(::CreateAndAddCubeImpl) { bind<CreateAndAddCubeUseCase>() }
    singleOf(::AddCubeToViewImpl) { bind<AddCubeToViewUseCase>() }
}

val networkModule = module {
    singleOf(::ApiServiceImpl) { bind<ApiService>() }
    singleOf(::NodesRepositoryImpl) { bind<NodesRepository>() }
}

val presentationModule = module {
    viewModelOf(::PlacementViewModel)
}

val koinModules = listOf(
    domainModule,
    networkModule,
    presentationModule
)