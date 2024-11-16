package com.abdu.hybridarapp.di

import com.abdu.hybridapp.domain.*
import com.abdu.hybridarapp.data.ApiService
import com.abdu.hybridarapp.data.NodesRepositoryImpl
import com.abdu.hybridarapp.viewmodel.PlacementViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val domainModule = module {
    singleOf(::GetInitialWorldPositionImpl) { bind<GetInitialWorldPositionUseCase>() }
    singleOf(::CreateAndAddCubeImpl) { bind<CreateAndAddCubeUseCase>() }
    singleOf(::AddCubeToViewImpl) { bind<AddCubeToViewUseCase>() }
    singleOf(::CalculateArrowAngle) { bind<CalculateArrowAngleUseCase>() }
}

val networkModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://localhost:8080/") // Replace with your actual base URL
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(ApiService::class.java) }
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