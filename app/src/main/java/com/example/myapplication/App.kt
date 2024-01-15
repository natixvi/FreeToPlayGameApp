package com.example.myapplication

import android.app.Application
import android.util.Log
import com.example.myapplication.module.gameModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        Log.d("App", "Uruchomienie aplikacji.")
        startKoin {
            androidContext(this@App)
            modules(gameModule)
        }
    }
}