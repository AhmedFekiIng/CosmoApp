package com.example.presentation

import com.example.data.model.Device
import com.example.domain.DeviceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.mockito.MockitoAnnotations.initMocks
import viewmodels.DeviceListViewModel

@ExperimentalCoroutinesApi
class DeviceListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: DeviceRepository

    lateinit var viewModel: DeviceListViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        initMocks(this)
        viewModel = DeviceListViewModel()
    }

    @Test
    suspend fun `fetchDevices should update devices LiveData when repository returns devices`() {
        val devices = listOf(
            Device("1", "Model 1", "Product 1", "1.0", "12345", "Mode 1", false, "Mode", false, 0),
            Device("2", "Model 2", "Product 2", "2.0", "54321", "Mode 2", true, "Mode", true, 100)
        )
        `when`(repository.getDevices()).thenReturn(devices)

        // When
        viewModel.fetchDevices()

        // Then
        assertEquals(devices, viewModel.devices.value)
    }

    @Test
    fun `fetchDevices should update error LiveData when repository throws an exception`() =
        testDispatcher.runBlockingTest {
            val errorMessage = "Error fetching devices"
            `when`(repository.getDevices()).thenThrow(Exception(errorMessage))

            viewModel.fetchDevices()
            assertEquals(errorMessage, viewModel.error.value)
        }

    @Test
    suspend fun `fetchDevice should update device LiveData when repository returns a device`() {
        val device = Device("1", "Model 1", "Product 1", "1.0", "12345", "Mode 1", false, "Mode", false, 0)
        `when`(repository.getDevice(0)).thenReturn(device)

        viewModel.fetchDevice(0)

        assertEquals(device, viewModel.device.value)
    }

    @Test
    fun `fetchDevice should update error LiveData when repository throws an exception`() =
        testDispatcher.runBlockingTest {
            val errorMessage = "Error fetching device"
            `when`(repository.getDevice(0)).thenThrow(Exception(errorMessage))

            viewModel.fetchDevice(0)

            assertEquals(errorMessage, viewModel.error.value)
        }
}