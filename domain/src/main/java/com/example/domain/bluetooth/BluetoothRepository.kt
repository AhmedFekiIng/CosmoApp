package com.example.domain.bluetooth

interface BluetoothRepository {
    suspend fun discoverDevices(): List<BluetoothDevice>
}
