package viewmodels

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DeviceListViewModel() }
    viewModel { BluetoothViewModel() }
}
