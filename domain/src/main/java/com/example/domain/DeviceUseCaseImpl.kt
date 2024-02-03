package com.example.domain

import com.example.data.model.Device
import com.example.domain.DeviceRepository

class DeviceUseCaseImpl(private val repository: DeviceRepository) : DeviceUseCase {
    override suspend fun getDevices(): List<Device> {
        return repository.getDevices()
    }
}
