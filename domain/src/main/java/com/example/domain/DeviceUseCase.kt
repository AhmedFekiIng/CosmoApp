package com.example.domain

import com.example.data.model.Device

interface DeviceUseCase {
    suspend fun getDevices(): List<Device>
}
