package com.example.data.repository

import android.util.Log
import com.example.data.model.Device
import com.example.domain.DeviceRepository

class DeviceRepositoryImpl(baseUrl: String) : DeviceRepository {
    private val apiService = ApiService.create(baseUrl)

    override suspend fun getDevices(): List<Device> {
        return try {
            apiService.getDevices().devices
        } catch (e: Exception) {
            Log.e("DeviceRepositoryImpl", "Error fetching devices: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getDevice(item: Int): Device? {
        return try {
            val allDevices = getDevices()
            if (item in allDevices.indices) {
                return allDevices[item]
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("DeviceRepositoryImpl", "Error fetching device: ${e.message}")
            null
        }
    }
}
