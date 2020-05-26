package com.jgeniselli.rgbcontroller

import android.bluetooth.BluetoothAdapter
import com.jgeniselli.rgbcontroller.color.control.RGBControllerViewModel
import com.jgeniselli.rgbcontroller.data.source.BluetoothColorRepository
import com.jgeniselli.rgbcontroller.data.source.BluetoothDevicesRepository
import com.jgeniselli.rgbcontroller.data.source.ColorRepository
import com.jgeniselli.rgbcontroller.data.source.DevicesRepository
import com.jgeniselli.rgbcontroller.devices.PairedDevicesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DependencyInjection {

    val module by lazy {
        module {
            single<DevicesRepository> {
                BluetoothDevicesRepository(
                    BluetoothAdapter.getDefaultAdapter()
                )
            }
            single<ColorRepository> {
                BluetoothColorRepository(
                    BluetoothAdapter.getDefaultAdapter()
                )
            }
            viewModel {
                PairedDevicesViewModel(
                    get()
                )
            }
            viewModel { (deviceAddress: String) ->
                RGBControllerViewModel(
                    get(),
                    deviceAddress
                )
            }
        }
    }
}