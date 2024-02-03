package com.example.domain

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.bluetooth.BluetoothRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class BluetoothRepositoryImplTest {

    private lateinit var bluetoothRepository: BluetoothRepositoryImpl

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockBluetoothAdapter: BluetoothAdapter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        bluetoothRepository = BluetoothRepositoryImpl(mockContext)
    }

    @Test
    fun `discoverDevices returns empty list when BluetoothAdapter is null`() = runBlocking {
        `when`(BluetoothAdapter.getDefaultAdapter()).thenReturn(null)

        val devices = bluetoothRepository.discoverDevices()

        assertEquals(0, devices.size)
    }

    @Test
    fun `discoverDevices requests Bluetooth permission when permission is not granted`(): Unit =
        runBlocking {
            `when`(BluetoothAdapter.getDefaultAdapter()).thenReturn(mockBluetoothAdapter)
            `when`(mockContext.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT))
                .thenReturn(PackageManager.PERMISSION_DENIED)

            bluetoothRepository.discoverDevices()

            verify(bluetoothRepository).requestBluetoothPermission()
        }

}
