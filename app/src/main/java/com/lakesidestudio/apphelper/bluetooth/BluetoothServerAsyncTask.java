package com.lakesidestudio.apphelper.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by blackkensai on 16-9-29.
 */


public class BluetoothServerAsyncTask extends AsyncTask<Context, Integer, BluetoothData> {
    private BluetoothServerSocket bluetoothServerSocket;
    private boolean running = true;

    private void waitingPackage(BluetoothSocket bluetoothSocket) {
        BluetoothTransfer bluetoothTransfer = null;
        try {
            bluetoothTransfer = new BluetoothTransfer(bluetoothSocket);
            while (true) {
                bluetoothTransfer.wait4Data();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (bluetoothTransfer != null) {
                bluetoothTransfer.close();
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected BluetoothData doInBackground(Context[] params) {
        try {
            bluetoothServerSocket = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord("AppHelper", BluetoothConstant.BLUETOOTH_UUID);
            while (running) {
                BluetoothSocket socket = bluetoothServerSocket.accept();
                if (socket != null) {
                    bluetoothServerSocket.close();
                    this.waitingPackage(socket);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
