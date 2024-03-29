package com.example.cosmo

import android.app.Application
import com.example.data.repository.apiModule
import com.example.presentation.screen.bluetoothModule
import java.util.Properties
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import viewmodels.viewModelModule

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val baseUrl = assets.open("app.properties").use { inputStream ->
            val properties = Properties()
            properties.load(inputStream)
            properties.getProperty("base_url")
        }
        val koinModules = listOf(apiModule, viewModelModule, bluetoothModule)
        startKoin {
            androidContext(this@MyApp)
            properties(mapOf("base_url" to baseUrl))
            modules(koinModules)
        }
    }
}
