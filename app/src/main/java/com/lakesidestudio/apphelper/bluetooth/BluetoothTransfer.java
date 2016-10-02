package com.lakesidestudio.apphelper.bluetooth;

import android.bluetooth.BluetoothSocket;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by blackkensai on 16-10-1.
 */

public class BluetoothTransfer {
    public static final String TYPE_PKG = "PKG";
    public static final String TYPE_APK = "APK";
    public static final String TYPE_INTERNAL_DATA = "IDT";

    public static final String RES_OK = "OK";
    public static final String RES_NG = "NG";
    public static final String RES_LENGTH_NG = "LENNG";
    public static final String RES_CHECKSUM_NG = "CHSNG";

    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private BluetoothSocket bluetoothSocket;

    public BluetoothTransfer(BluetoothSocket bluetoothSocket) throws IOException {
        inputStream = new ObjectInputStream(bluetoothSocket.getInputStream());
        outputStream = new ObjectOutputStream(bluetoothSocket.getOutputStream());
        this.bluetoothSocket = bluetoothSocket;
    }

    public Response sendPackageName(String packageName) throws IOException, ClassNotFoundException {
        outputStream.writeObject(TYPE_PKG);
        outputStream.writeObject(packageName);
        return wait4Response();
    }

    public BluetoothData wait4Data() throws IOException, ClassNotFoundException {
        BluetoothData bluetoothData = new BluetoothData();
        bluetoothData.type = String.valueOf(inputStream.readObject());
        switch (bluetoothData.type) {
            case TYPE_PKG:
                bluetoothData.packageName = String.valueOf(inputStream.readObject());
                responseOK();
                break;
            default:
                throw new IllegalArgumentException("Not support.");
        }
        return bluetoothData;
    }

    private Response wait4Response() throws IOException, ClassNotFoundException {
        Response response = new Response();
        response.response = String.valueOf(inputStream.readObject());
        response.message = String.valueOf(inputStream.readObject());
        return response;
    }

    private void responseOK() throws IOException {
        outputStream.writeObject(RES_OK);
        outputStream.writeObject("OK");
    }

    private void responseError(String message) throws IOException {
        outputStream.writeObject(RES_NG);
        outputStream.writeObject(message);
    }

    private void responseError(Throwable throwable) throws IOException {
        outputStream.writeObject(RES_NG);
        outputStream.writeObject(throwable.getMessage());
    }

    public void close() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Response {
        public String response;
        public String message;

        public boolean isOk() {
            return RES_OK.equals(response);
        }
    }
}
