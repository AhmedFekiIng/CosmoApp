package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Device
import com.example.domain.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class DeviceListViewModel : ViewModel() {

    private val repository: DeviceRepository by inject(DeviceRepository::class.java)


    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> get() = _devices

    private val _device = MutableStateFlow<Device?>(null)
    val device: StateFlow<Device?> get() = _device

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun fetchDevices() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val fetchedDevices = repository.getDevices()
                _devices.value = fetchedDevices
            } catch (e: Exception) {
                _error.value = "Failed to fetch devices: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchDevice(item: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val fetchedDevice = repository.getDevice(item)
                _device.value = fetchedDevice
            } catch (e: Exception) {
                _error.value = "Failed to fetch device: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
