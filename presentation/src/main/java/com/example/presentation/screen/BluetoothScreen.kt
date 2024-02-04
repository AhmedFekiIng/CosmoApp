package com.example.presentation.screen

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.bluetooth.DomainBluetoothDevice
import androidx.compose.runtime.collectAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import viewmodels.BluetoothViewModel
import viewmodels.DeviceListViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothScreen(viewModel: BluetoothViewModel = viewModel()) {
    val context = LocalContext.current
    val bluetoothDevices by viewModel.bluetoothDevices.collectAsState()

    val receiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d("BluetoothScreen", "onReceive called")
                val action: String? = intent?.action
                if (BluetoothDevice.ACTION_FOUND == action) {
                    Log.d("BluetoothScreen", "Bluetooth device found")
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        val name = if (ActivityCompat.checkSelfPermission(
                                context!!,
                                Manifest.permission.BLUETOOTH_CONNECT
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
                        viewModel.addBluetoothDevice(
                            DomainBluetoothDevice(
                                name,
                                address,
                                characteristics
                            )
                        )
                    }
                }
            }
        }
    }

    val bluetoothPermissionState = rememberPermissionState(Manifest.permission.BLUETOOTH_CONNECT)
    val bluetoothPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startDiscovery(context, receiver)
        } else {
            Toast.makeText(
                context,
                "Bluetooth permission is required to discover devices.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bluetooth Devices:")
        if (bluetoothDevices.isEmpty()) {
            CircularProgressIndicator()
        } else {
            bluetoothDevices.forEach { device ->
                Text(text = "${device.name} - ${device.address}")
                device.characteristics.forEach { characteristic ->
                    Text(text = "Characteristic: $characteristic")
                }
            }
        }

        Button(
            onClick = {
                if (bluetoothPermissionState.hasPermission) {
                    viewModel.startDiscovery(context, receiver)
                } else {
                    bluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
                }
            }
        ) {
            Text("Discover Bluetooth Devices")
        }
    }
}