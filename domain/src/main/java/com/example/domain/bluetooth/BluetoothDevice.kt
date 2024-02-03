package com.example.domain.bluetooth

data class BluetoothDevice(
    val name: String,
    val address: String,
    val characteristics: List<String>
)