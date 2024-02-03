package com.example.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Bluetooth
import androidx.navigation.NavController
import com.example.data.model.Device
import androidx.lifecycle.viewmodel.compose.viewModel
import viewmodels.DeviceListViewModel

@Composable
fun DeviceListScreen(navController: NavController, viewModel: DeviceListViewModel = viewModel()) {
    val devicesState = viewModel.devices.collectAsState(initial = emptyList())
    val loadingState by viewModel.loading.collectAsState()
    val errorState by viewModel.error.collectAsState()
    val devices = devicesState.value

    LaunchedEffect(Unit) {
        viewModel.fetchDevices()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Device List",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp)
            )
            IconButton(
                onClick = {
                    navController.navigate("bluetooth")
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Bluetooth,
                    contentDescription = "Bluetooth",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
        if (loadingState) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorState != null) {
            Text(
                text = "Error: ${errorState}",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        } else {
            LazyColumn {
                itemsIndexed(devices) { index, device ->
                    DeviceListItem(index, device = device, onItemClick = { selectedIndex ->
                        navController.navigate("deviceDetail/$index")
                    })
                }
            }
        }
    }
}

@Composable
fun DeviceListItem(index: Int, device: Device, onItemClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onItemClick(index) }
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Model: ${device.model}",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Product: ${device.product}", style = MaterialTheme.typography.body1)
        Text(text = "Firmware Version: ${device.firmwareVersion}", style = MaterialTheme.typography.body1)
        Text(text = "Serial: ${device.serial}", style = MaterialTheme.typography.body1)
        Text(text = "Installation Mode: ${device.installationMode}", style = MaterialTheme.typography.body1)
        Text(text = "Brake Light: ${device.brakeLight}", style = MaterialTheme.typography.body1)
        Text(text = "Light Mode: ${device.lightMode}", style = MaterialTheme.typography.body1)
        Text(text = "Light Auto: ${device.lightAuto}", style = MaterialTheme.typography.body1)
        Text(text = "Light Value: ${device.lightValue}", style = MaterialTheme.typography.body1)
    }
}