package com.jgeniselli.rgbcontroller

import android.bluetooth.BluetoothAdapter
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DependencyInjection {

    val module by lazy {
        module {
            single<DevicesRepository> {
                BluetoothDevicesRepository(BluetoothAdapter.getDefaultAdapter())
            }
            viewModel { PairedDevicesViewModel(get()) }
            viewModel { RGBControllerViewModel() }
        }
    }
}