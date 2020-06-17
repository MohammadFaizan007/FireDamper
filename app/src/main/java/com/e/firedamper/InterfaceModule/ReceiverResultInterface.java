package com.e.firedamper.InterfaceModule;


import com.e.firedamper.EncodeDecodeModule.ByteQueue;

public interface ReceiverResultInterface {

    void onScanSuccess(int successCode, ByteQueue byteQueue);
    void onScanFailed(int errorCode);


}
