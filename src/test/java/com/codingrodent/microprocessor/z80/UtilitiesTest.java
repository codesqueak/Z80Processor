package com.codingrodent.microprocessor.z80;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilitiesTest {

    @Test
    void getByte() {
        assertEquals("00", Utilities.getByte(0));
        assertEquals("7F", Utilities.getByte(127));
        assertEquals("FF", Utilities.getByte(255));
        assertEquals("00", Utilities.getByte(256));
    }

    @Test
    void getWord() {
        assertEquals("0000", Utilities.getWord(0));
        assertEquals("1234", Utilities.getWord(0x1234));
        assertEquals("FFFF", Utilities.getWord(0xFFFF));
    }

    @Test
    void getHexDigit() {
        assertEquals(0, Utilities.getHexDigit('0'));
        assertEquals(1, Utilities.getHexDigit('1'));
        assertEquals(9, Utilities.getHexDigit('9'));
        assertEquals(10, Utilities.getHexDigit('A'));
        assertEquals(15, Utilities.getHexDigit('F'));
    }

    @Test
    void getHexValue() {
        assertEquals(0, Utilities.getHexValue("0"));
        assertEquals(127, Utilities.getHexValue("007F"));
        assertEquals(4096, Utilities.getHexValue("1000"));
        assertEquals(65535, Utilities.getHexValue("FFFF"));
    }

    @Test
    void getFlags() {
        assertEquals("        ", Utilities.getFlags(0));
        assertEquals("SZ5H3PNC", Utilities.getFlags(0xFF));
    }
}