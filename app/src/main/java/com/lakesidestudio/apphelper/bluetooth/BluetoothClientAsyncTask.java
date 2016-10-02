package com.lakesidestudio.apphelper.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by blackkensai on 16-10-1.
 */

public class BluetoothClientAsyncTask extends AsyncTask<Context, Integer, Void> {
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;

    public BluetoothClientAsyncTask(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    @Override
    protected Void doInBackground(Context... params) {
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(BluetoothConstant.BLUETOOTH_UUID);
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                bluetoothSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
