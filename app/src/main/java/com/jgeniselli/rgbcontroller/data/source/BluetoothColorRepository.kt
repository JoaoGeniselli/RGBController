package com.jgeniselli.rgbcontroller.data.source

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class BluetoothColorRepository(private val bluetoothAdapter: BluetoothAdapter) :
    ColorRepository {

    private var bluetoothSocket: BluetoothSocket? = null
        // mock backing field support:
        get() = field
        set(value) { field = value }

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
                bluetoothSocket = try {
                    socket.connect()
                    socket
                } catch (e: Throwable) {
                    null
                }
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