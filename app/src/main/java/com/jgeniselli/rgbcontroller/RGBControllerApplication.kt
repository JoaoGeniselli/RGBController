package com.jgeniselli.rgbcontroller

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RGBControllerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RGBControllerApplication)
            modules(DependencyInjection.module)
        }
    }
}