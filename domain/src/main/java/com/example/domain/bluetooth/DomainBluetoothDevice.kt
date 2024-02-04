package com.example.domain.bluetooth

data class DomainBluetoothDevice(
    val name: String,
    val address: String,
    val characteristics: List<String>
)