package viewmodels

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.bluetooth.DomainBluetoothDevice
import com.example.domain.bluetooth.BluetoothRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

object Constants {
    const val REQUEST_BLUETOOTH_PERMISSION_CODE = 123
}

class BluetoothViewModel() : ViewModel() {
    private val bluetoothRepository: BluetoothRepository by inject(BluetoothRepository::class.java)
    private val _bluetoothDevices = MutableStateFlow<List<DomainBluetoothDevice>>(emptyList())
    val bluetoothDevices: StateFlow<List<DomainBluetoothDevice>> = _bluetoothDevices

    init {
        viewModelScope.launch {
            _bluetoothDevices.value = bluetoothRepository.discoverDevices().map {
                DomainBluetoothDevice(it.name, it.address, it.characteristics)
            }
        }
    }

    fun startDiscovery(context: Context, receiver: BroadcastReceiver) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
            Log.d("BluetoothViewModel", "Bluetooth adapter enabled")
            if (checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("BluetoothViewModel", "Bluetooth permissions granted")
                val filter = IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND)
                context.registerReceiver(receiver, filter)
                bluetoothAdapter.startDiscovery()
            } else {
                Log.d("BluetoothViewModel", "Bluetooth permissions not granted")
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

    fun addBluetoothDevice(device: DomainBluetoothDevice) {
        val currentList = _bluetoothDevices.value.toMutableList()
        currentList.add(device)
        _bluetoothDevices.value = currentList
    }

    private fun requestBluetoothPermission(context: Context) {
        val activity = context.findActivity()
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN
                ),
                Constants.REQUEST_BLUETOOTH_PERMISSION_CODE
            )
        }
    }

    private fun Context.findActivity(): Activity? {
        var currentContext = this
        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) {
                return currentContext
            }
            currentContext = currentContext.baseContext
        }
        return null
    }
}