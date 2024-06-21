package com.codingrodent.microprocessor.Io;

import java.util.function.Consumer;

public interface IoQueue {
    void writeWord(int address, int value);

    void writeByte(int address, int value);

    void readByte(int address, Consumer<Integer> callback);

    void readWord(int address, Consumer<Integer> callback);

    void ioRead(int address, Consumer<Integer> callback);

    void IOWrite(Integer address, int value);

    void clear();
}
