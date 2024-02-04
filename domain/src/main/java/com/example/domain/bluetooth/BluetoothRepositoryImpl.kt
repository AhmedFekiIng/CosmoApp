package com.example.domain.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import kotlin.random.Random


class BluetoothRepositoryImpl(private val context: Context) : BluetoothRepository {

    companion object {
        private const val BLUETOOTH_PERMISSION = Manifest.permission.BLUETOOTH_CONNECT
    }

    override suspend fun discoverDevices(): List<DomainBluetoothDevice> {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val devices: MutableList<DomainBluetoothDevice> = mutableListOf()

        if (checkSelfPermission(
                context,
                BLUETOOTH_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("BluetoothRepositoryImpl", "Bluetooth permissions granted")
            bluetoothAdapter?.takeIf { it.isEnabled }?.startDiscovery()
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Log.d("BluetoothRepositoryImpl", "onReceive called")
                    val action: String? = intent?.action
                    if (BluetoothDevice.ACTION_FOUND == action) {
                        val device: BluetoothDevice? =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        device?.let {
                            val name = if (checkSelfPermission(
                                    context!!,
                                    BLUETOOTH_PERMISSION
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                device.name ?: "Unknown"
                            } else {
                                "Unknown"
                            }
                            val address = device.address
                            val characteristics = mutableListOf<String>()
                            if (device.bondState == BluetoothDevice.BOND_BONDED) {
                                for (uuid in device.uuids) {
                                    characteristics.add(uuid.toString())
                                }
                            }
                            devices.add(DomainBluetoothDevice(name, address, characteristics))
                        }
                    }
                }
            }
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            context.registerReceiver(receiver, filter)

            bluetoothAdapter?.cancelDiscovery()
        } else {
            Log.d("BluetoothRepositoryImpl", "Bluetooth permissions not granted")
            requestBluetoothPermission()
        }

        return devices
    }

    private fun requestBluetoothPermission() {
        (context as? Activity)?.let { activity ->
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(BLUETOOTH_PERMISSION),
                generatePermissionRequestCode()
            )
        }
    }

    private fun generatePermissionRequestCode(): Int {
        return Random.nextInt(0, Int.MAX_VALUE)

    }
}