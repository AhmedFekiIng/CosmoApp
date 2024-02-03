package com.example.data.repository

import com.example.domain.DeviceRepository
import org.koin.dsl.module

val apiModule = module {
    single { getProperty<String>("base_url") }
    single<DeviceRepository> { DeviceRepositoryImpl(get()) }
    factory { (baseUrl: String) -> DeviceRepositoryImpl(baseUrl) }
}
