package com.example.domain

import com.example.data.model.Device

interface DeviceRepository {
    suspend fun getDevices(): List<Device>
    suspend fun getDevice(item: Int): Device?
}