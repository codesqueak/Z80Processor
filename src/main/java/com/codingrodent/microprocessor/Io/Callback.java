package com.codingrodent.microprocessor.Io;

@FunctionalInterface
public interface Callback {
    void accept(int data);
}
