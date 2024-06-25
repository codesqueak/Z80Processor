package com.codingrodent.microprocessor.Io;

public class IoRequest {
    public final int address;
    public final boolean isMemory;
    public boolean isWrite;
    public int payload;
    public Callback callback;

    //Write request
    public IoRequest(int address, int payload, boolean isMemory) {
        this.address = address;
        this.payload = payload;
        this.isMemory = isMemory;
        isWrite = true;
    }

    //Read request
    public IoRequest(int address, boolean isMemory, Callback callback) {
        this.address = address;
        this.isMemory = isMemory;
        this.callback = callback;
    }

}
