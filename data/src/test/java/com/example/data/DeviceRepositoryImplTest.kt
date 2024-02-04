package com.example.data

import com.example.data.repository.DeviceRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class DeviceRepositoryImplTest {
    val mockApiService = "https://cosmo-api.develop-sr3snxi-x6u2x52ooksf4.de-2.platformsh.site/test/"
    val deviceRepository = DeviceRepositoryImpl(mockApiService)

    @Test
    fun `test getDevices`() {
        runBlocking {
            val devices = deviceRepository.getDevices()
            assertEquals(6, devices.size)
        }
    }

    @Test
    fun `test getDevice with valid index`() {
        runBlocking {
            val device = deviceRepository.getDevice(0)
            assertEquals("RIDE", device?.model)
        }
    }

    @Test
    fun `test getDevice with invalid index`() {
        runBlocking {
            val device = deviceRepository.getDevice(-1)
            assertEquals(null, device)
        }
    }
}
