package com.jgeniselli.rgbcontroller

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BluetoothColorRepository(private val bluetoothAdapter: BluetoothAdapter) : ColorRepository {

    private var bluetoothSocket: BluetoothSocket? = null

    override suspend fun applyColor(color: Color) {
        GlobalScope.launch(Dispatchers.IO) {
            bluetoothSocket?.outputStream?.write(color.toLightDevicePattern().toByteArray())
        }
    }

    override suspend fun connectToDevice(pairedDeviceAddress: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val device = bluetoothAdapter.getRemoteDevice(pairedDeviceAddress)
            device?.let {
                val uuid = device.uuids.firstOrNull()?.uuid
                val socket = device.createRfcommSocketToServiceRecord(uuid)
                socket.connect()
                bluetoothSocket = socket
            }
        }
    }

    override suspend fun disconnect() {
        GlobalScope.launch(Dispatchers.IO) {
            if (bluetoothSocket?.isConnected == true) {
                bluetoothSocket?.close()
            }
        }
    }
}