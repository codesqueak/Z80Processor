package com.codingrodent.microprocessor.Io;

import com.codingrodent.microprocessor.IBaseDevice;
import com.codingrodent.microprocessor.IMemory;

import java.util.function.Consumer;

public class SyncIoQueue implements IoQueue {
    private final IMemory memory;
    private final IBaseDevice ioDevice;

    public SyncIoQueue(IMemory memory, IBaseDevice ioDevice) {
        this.memory = memory;
        this.ioDevice = ioDevice;
    }

    @Override
    public void writeWord(int address, int value) {
        memory.writeWord(address, value);
    }

    @Override
    public void writeByte(int address, int value) {
        memory.writeByte(address, value);
    }

    @Override
    public void readByte(int address, Consumer<Integer> callback) {
        int b = memory.readByte(address);
        callback.accept(b);

    }

    @Override
    public void readWord(int address, Consumer<Integer> callback) {
        int word = memory.readWord(address);
        callback.accept(word);
    }

    @Override
    public void ioRead(int address, Consumer<Integer> callback) {
        callback.accept(ioDevice.IORead(address));
    }

    @Override
    public void IOWrite(Integer address, int value) {
        ioDevice.IOWrite(address, value);
    }

    @Override
    public void clear() {

    }

}
