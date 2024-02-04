package com.example.presentation.screen

import com.example.domain.bluetooth.BluetoothRepository
import com.example.domain.bluetooth.BluetoothRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val bluetoothModule = module {
    single<BluetoothRepository> { BluetoothRepositoryImpl(androidContext()) }
}

