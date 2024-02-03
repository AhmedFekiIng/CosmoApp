package com.example.presentation.screen

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.presentation.screen.Constants.REQUEST_BLUETOOTH_PERMISSION_CODE
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.example.domain.bluetooth.BluetoothDevice as DomainBluetoothDevice

object Constants {
    const val REQUEST_BLUETOOTH_PERMISSION_CODE = 123
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothScreen() {
    val context = LocalContext.current
    val bluetoothDevices = remember { mutableStateOf<List<DomainBluetoothDevice>>(emptyList()) }

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
                        bluetoothDevices.value = bluetoothDevices.value + DomainBluetoothDevice(
                            name,
                            address,
                            characteristics
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
            startBluetoothDiscovery(context, receiver)
        } else {
            Toast.makeText(
                context,
                "Bluetooth permission is required to discover devices.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        if (bluetoothPermissionState.permissionRequested &&
            bluetoothPermissionState.hasPermission
        ) {
            startBluetoothDiscovery(context, receiver)
        }
    }

    Column {
        Text(text = "Bluetooth Devices:")
        if (bluetoothDevices.value.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            bluetoothDevices.value.forEach { device ->
                Text(text = "${device.name} - ${device.address}")
                device.characteristics.forEach { characteristic ->
                    Text(text = "Characteristic: $characteristic")
                }
            }
        }

        Button(
            onClick = {
                if (bluetoothPermissionState.hasPermission) {
                    startBluetoothDiscovery(context, receiver)
                } else {
                    bluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
                }
            }
        ) {
            Text("Discover Bluetooth Devices")
        }
    }
}

fun startBluetoothDiscovery(context: Context, receiver: BroadcastReceiver) {
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
        Log.d("BluetoothScreen", "Bluetooth adapter enabled")
        if (checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("BluetoothScreen", "Bluetooth permissions granted")
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            context.registerReceiver(receiver, filter)
            bluetoothAdapter.startDiscovery()
        } else {
            Log.d("BluetoothScreen", "Bluetooth permissions not granted")
            requestBluetoothPermission(context)
        }
    } else {
        Toast.makeText(
            context,
            "Please enable Bluetooth to discover devices.",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun requestBluetoothPermission(context: Context) {
    val activity = context.findActivity()
    activity?.let {
        ActivityCompat.requestPermissions(
            it,
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN
            ),
            REQUEST_BLUETOOTH_PERMISSION_CODE
        )
    }
}

fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}


@Preview
@Composable
fun PreviewBluetoothScreen() {
    BluetoothScreen()
}