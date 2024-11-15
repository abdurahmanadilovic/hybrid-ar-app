package com.abdu.hybridarapp

import android.app.Application
import com.abdu.hybridarapp.di.koinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HybridArApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@HybridArApplication)
            modules(koinModules)
        }
    }
} 