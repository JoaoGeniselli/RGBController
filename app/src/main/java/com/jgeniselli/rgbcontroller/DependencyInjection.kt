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
            single<ColorRepository> { BluetoothColorRepository() }
            viewModel { PairedDevicesViewModel(get()) }
            viewModel { (deviceAddress: String) -> RGBControllerViewModel(get(), deviceAddress) }
        }
    }
}