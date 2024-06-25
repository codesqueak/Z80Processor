package com.codingrodent.microprocessor.Io;

public interface IoQueue {
    void writeWord(int address, int value);

    void writeByte(int address, int value);

    void readByte(int address, Callback callback);

    void readWord(int address, Callback callback);

    void ioRead(int address, Callback callback);

    void IOWrite(Integer address, int value);

    void clear();
}
