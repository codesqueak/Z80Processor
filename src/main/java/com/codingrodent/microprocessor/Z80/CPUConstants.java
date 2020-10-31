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
package com.codingrodent.microprocessor.Z80;

public class CPUConstants {
    // Pre-calculate parity table
    static final boolean[] PARITY_TABLE = new boolean[256];
    //
    // T States for all instructions (Where fixed) - If variable, handled locally
    final static byte[] OPCODE_T_STATES = new byte[]{4, 16, 7, 6, 4, 4, 7, 4, 4, 11, 7, 6, 4, 4, 7, 4,        // 0
            0, 16, 7, 6, 4, 4, 7, 4, 12, 11, 7, 6, 4, 4, 7, 4,                                                                // 10
            0, 16, 7, 6, 4, 4, 7, 4, 0, 11, 7, 6, 4, 4, 7, 4,                                                                // 20
            0, 16, 7, 6, 4, 4, 7, 4, 0, 11, 7, 6, 4, 4, 7, 4,                                                                // 30
            4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 4, 4, 7, 4,                                                                    // 40
            4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 4, 4, 7, 4,                                                                    // 50
            4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 4, 4, 7, 4,                                                                    // 60
            7, 7, 7, 7, 7, 7, 4, 7, 4, 4, 4, 4, 4, 4, 7, 4,                                                                    // 70
            4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 4, 4, 7, 4,                                                                    // 80
            4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 4, 4, 7, 4,                                                                    // 90
            4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 4, 4, 7, 4,                                                                    // A0
            4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 4, 4, 7, 4,                                                                    // B0
            0, 10, 0, 0, 0, 11, 7, 0, 0, 0, 0, 0, 0, 0, 7, 0,                                                                // C0
            0, 10, 0, 11, 0, 11, 7, 0, 0, 4, 0, 11, 0, 4, 7, 0,                                                                // D0
            0, 10, 0, 19, 0, 11, 7, 0, 0, 4, 0, 4, 0, 0, 7, 0,                                                                // E0
            0, 4, 0, 4, 0, 11, 7, 0, 0, 0, 0, 0, 0, 4, 7, 0                                                                    // F0
    };
    final static byte[] OPCODE_CB_STATES = new byte[]{8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8,        // 0
            8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8,                                                                // 10
            8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8,                                                                // 20
            8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8,                                                                // 30
            8, 8, 8, 8, 8, 8, 12, 8, 8, 8, 8, 8, 8, 8, 12, 8,                                                                // 40
            8, 8, 8, 8, 8, 8, 12, 8, 8, 8, 8, 8, 8, 8, 12, 8,                                                                // 50
            8, 8, 8, 8, 8, 8, 12, 8, 8, 8, 8, 8, 8, 8, 12, 8,                                                                // 60
            8, 8, 8, 8, 8, 8, 12, 8, 8, 8, 8, 8, 8, 8, 12, 8,                                                                // 70
            8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8,                                                                // 80
            8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8,                                                                // 90
            8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8,                                                                // A0
            8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8,                                                                // B0
            8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8,                                                                // C0
            8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8,                                                                // D0
            8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8,                                                                // E0
            8, 8, 8, 8, 8, 8, 15, 8, 8, 8, 8, 8, 8, 8, 15, 8                                                                // F0
    };
    final static byte[] OPCODE_DD_FD_STATES = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0,            // 0
            0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0,                                                                // 10
            0, 14, 20, 10, 8, 8, 11, 0, 0, 15, 14, 10, 8, 8, 11, 0,                                                            // 20
            0, 0, 0, 0, 23, 23, 19, 0, 0, 15, 0, 0, 0, 0, 0, 0,                                                                // 30
            8, 8, 8, 8, 8, 8, 19, 8, 8, 8, 8, 8, 8, 8, 19, 8,                                                                // 40
            8, 8, 8, 8, 8, 8, 19, 8, 8, 8, 8, 8, 8, 8, 19, 8,                                                                // 50
            8, 8, 8, 8, 8, 8, 19, 8, 8, 8, 8, 8, 8, 8, 19, 8,                                                                // 60
            19, 19, 19, 19, 19, 19, 0, 19, 8, 8, 8, 8, 8, 8, 19, 8,                                                            // 70
            0, 0, 0, 0, 19, 19, 19, 0, 0, 0, 0, 0, 19, 19, 19, 0,                                                            // 80
            0, 0, 0, 0, 19, 19, 19, 0, 0, 0, 0, 0, 19, 19, 19, 0,                                                            // 90
            0, 0, 0, 0, 19, 19, 19, 0, 0, 0, 0, 0, 19, 19, 19, 0,                                                            // A0
            0, 0, 0, 0, 19, 19, 19, 0, 0, 0, 0, 0, 19, 19, 19, 0,                                                            // B0
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,                                                                    // C0
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,                                                                    // D0
            0, 14, 0, 23, 0, 15, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0,                                                                // E0
            0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0                                                                    // F0
    };

    final static byte[] OPCODE_ED_STATES = new byte[]{8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,            // 0
            8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,                                                                    // 10
            8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,                                                                    // 20
            8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,                                                                    // 30
            12, 12, 15, 20, 9, 14, 8, 9, 12, 12, 15, 20, 9, 14, 8, 9,                                                        // 40
            12, 12, 15, 20, 9, 14, 8, 9, 12, 12, 15, 20, 9, 14, 8, 9,                                                        // 50
            12, 12, 15, 20, 9, 14, 8, 18, 12, 12, 15, 20, 9, 14, 8, 18,                                                        // 60
            0, 0, 15, 20, 9, 14, 9, 0, 12, 12, 15, 20, 9, 14, 9, 0,                                                            // 70
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,                                                                    // 80
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,                                                                    // 90
            16, 16, 16, 16, 0, 0, 0, 0, 16, 16, 16, 16, 0, 0, 0, 0,                                                            // A0
            8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,                                                                    // B0
            8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,                                                                    // C0
            8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,                                                                    // D0
            8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,                                                                    // E0
            8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8                                                                    // F0
    };
    final static byte[] OPCODE_INDEXED_CB_STATES = new byte[]{0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0,        // 0
            0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0,                                                                // 10
            0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0,                                                                // 20
            0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0,                                                                // 30
            0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 20, 0,                                                                // 40
            0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 20, 0,                                                                // 50
            0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 20, 0,                                                                // 60
            0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 20, 0,                                                                // 70
            0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0,                                                                // 80
            0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0,                                                                // 90
            0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0,                                                                // A0
            0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0,                                                                // B0
            0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0,                                                                // C0
            0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0,                                                                // D0
            0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0,                                                                // E0
            0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 23, 0                                                                // F0
    };
    // register bit codes
    final static int regCodeB = 0x0000;
    final static int regCodeC = 0x0001;
    final static int regCodeD = 0x0002;
    final static int regCodeE = 0x0003;
    final static int regCodeH = 0x0004;
    final static int regCodeL = 0x0005;
    final static int regCodeM = 0x0006;
    final static int regCodeF = 0x0006;
    final static int regCodeA = 0x0007;
    final static int regCodeIXH = 0x0004;
    final static int regCodeIXL = 0x0005;
    final static int regCodeBC = 0x0000;
    final static int regCodeDE = 0x0001;
    final static int regCodeHL = 0x0002;
    final static int regCodeSP = 0x0003;
    // or mask values
    final static int setBit0 = 0x0001;
    final static int setBit1 = 0x0002;
    final static int setBit2 = 0x0004;
    final static int setBit3 = 0x0008;
    final static int setBit4 = 0x0010;
    final static int setBit5 = 0x0020;
    final static int setBit6 = 0x0040;
    final static int setBit7 = 0x0080;
    // and mask values
    final static int resetBit0 = setBit0 ^ 0x00FF;
    final static int resetBit1 = setBit1 ^ 0x00FF;
    final static int resetBit2 = setBit2 ^ 0x00FF;
    final static int resetBit3 = setBit3 ^ 0x00FF;
    final static int resetBit4 = setBit4 ^ 0x00FF;
    final static int resetBit5 = setBit5 ^ 0x00FF;
    final static int resetBit6 = setBit6 ^ 0x00FF;
    final static int resetBit7 = setBit7 ^ 0x00FF;
    // flag register bit positions for setting
    final static int flag_S = 0x0080;
    final static int flag_Z = 0x0040;
    final static int flag_5 = 0x0020;
    final static int flag_H = 0x0010;
    final static int flag_3 = 0x0008;
    final static int flag_PV = 0x0004;
    final static int flag_N = 0x0002;
    final static int flag_C = 0x0001;
    // for resetting
    final static int flag_S_N = 0x007F;
    final static int flag_Z_N = 0x00BF;
    final static int flag_5_N = 0x00DF;
    final static int flag_H_N = 0x00EF;
    final static int flag_3_N = 0x00F7;
    final static int flag_PV_N = 0x00FB;
    final static int flag_N_N = 0x00FD;
    final static int flag_C_N = 0x00FE;
    /* LSB, MSB masking values */
    final static int lsb = 0x00FF;
    final static int msb = 0xFF00;
    final static int lsw = 0x0000FFFF;

    static {
        PARITY_TABLE[0] = true; // even PARITY_TABLE seed value
        int position = 1; // table position
        for (int bit = 0; bit < 8; bit++) {
            for (int fill = 0; fill < position; fill++) {
                PARITY_TABLE[position + fill] = !PARITY_TABLE[fill];
            }
            position = position * 2;
        }
    }

    /**
     * Stop construction
     */
    private CPUConstants() {
    }

    /**
     * All supported processor registers which can be accessed externally to the core
     */
    public enum RegisterNames {
        /**
         * 16 bit BC register pair
         */
        BC,
        /**
         * 16 bit DE register pair
         */
        DE,
        /**
         * 16 bit HL register pair
         */
        HL,
        /**
         * Alternate register file 16 bit BC register pair
         */
        BC_ALT,
        /**
         * Alternate register file 16 bit DE register pair
         */
        DE_ALT,
        /**
         * Alternate register file 16 bit HL register pair
         */
        HL_ALT,
        /**
         * IX 16 bit index register
         */
        IX,
        /**
         * IY 16 bit index register
         */
        IY,
        /**
         * Stack pointer
         */
        SP,
        /**
         * Program counter
         */
        PC,
        /**
         * 8 bit accumulator
         */
        A,
        /**
         * 8 bit flag register
         */
        F,
        /**
         * Alternate 8 bit accumulator
         */
        A_ALT,
        /**
         * Alternate 8 bit flag register
         */
        F_ALT,
        /**
         * 8 bit interrupt register
         */
        I,
        /**
         * 7 bit refresh register
         */
        R
    }
    // final static int msw = 0xFFFF0000;

}
