package com.tarokanizer

import android.app.Application
import di.getSharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TarokanizerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TarokanizerApp)
            modules(getSharedModules())
        }
    }

}
