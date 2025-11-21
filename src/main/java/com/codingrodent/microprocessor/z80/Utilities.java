/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingrodent.microprocessor.z80;

import java.util.HexFormat;

/**
 * Various utility functions, mainly useful for debug
 */
public class Utilities {
    private final static String flagChar = "SZ5H3PNC";
    private final static HexFormat hexFormat = HexFormat.of().withUpperCase();

    /*
      Constructor - don't!
     */
    private Utilities() {
    }


    /**
     * Turn a byte into two hex digits
     *
     * @param value Integer in 0..255
     * @return Hex string equivalent, 00..FF
     */
    public static String getByte(final int value) {
        char[] byteText = new char[2];
        byteText[0] = hexFormat.toHighHexDigit(value);
        byteText[1] = hexFormat.toLowHexDigit(value);
        return new String(byteText);
    }

    /**
     * Turn a word into four hex digits
     *
     * @param value integer in 0..65535 range
     * @return hex representation 0000..FFFF
     */
    public static String getWord(final int value) {
        return getByte(value >>> 8) + getByte(value & 0x00FF);
    }

    /**
     * Convert a hex digit into an integer
     *
     * @param hex Character 0..9, A..F
     * @return Value 0..15
     */
    public static int getHexDigit(final char hex) {
        return HexFormat.fromHexDigit(hex);
    }

    /**
     * Convert a hex string into an integer
     *
     * @param hex Hex value
     * @return Integer equivalent
     */
    public static int getHexValue(String hex) {
        return HexFormat.fromHexDigits(hex);
    }

    /**
     * Generate a string representing the contents of the flag register
     *
     * @param value Flag register value, 0x00 .. 0XFF
     * @return String representing the set flag values for each bit, or space(s) where not set
     */
    public static String getFlags(final int value) {
        var pos = 0x80;
        var c = 0;
        var flags = new StringBuilder();
        while (pos > 0) {
            if ((value & pos) == 0)
                flags.append(" ");
            else
                flags.append(flagChar.charAt(c));
            pos = pos >>> 1;
            c++;
        }
        return flags.toString();
    }
}