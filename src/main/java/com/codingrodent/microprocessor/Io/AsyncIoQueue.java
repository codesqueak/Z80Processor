package com.codingrodent.microprocessor.Io;

import java.util.LinkedList;
import java.util.Queue;

public class AsyncIoQueue implements IoQueue {
    public final Queue<IoRequest> requests = new LinkedList<>();

    @Override
    public void writeWord(int address, int value) {
        requests.add(new IoRequest(address, value & 0xff, true));
        requests.add(new IoRequest(address + 1, value >> 8, true));
    }

    @Override
    public void writeByte(int address, int value) {
        requests.add(new IoRequest(address, value, true));
    }

    @Override
    public void readByte(int address, Callback callback) {
        requests.add(new IoRequest(address, true, callback));
    }

    @Override
    public void readWord(int address, Callback callback) {
        readByte(address, (lowByte) -> readByte(address + 1, (hiByte) -> callback.accept((hiByte << 8) + lowByte)));
    }

    @Override
    public void ioRead(int address, Callback callback) {
        requests.add(new IoRequest(address, false, callback));
    }

    @Override
    public void IOWrite(Integer address, int value) {
        requests.add(new IoRequest(address, value, false));
    }

    @Override
    public void clear() {
        requests.clear();
    }
}
