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

import com.codingrodent.microprocessor.support.Z80IO;
import com.codingrodent.microprocessor.support.Z80Memory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Z80CoreTestED {
    private Z80Core z80;
    private Z80Memory z80Memory;

    @BeforeEach
    public void setUp() {
        z80Memory = new Z80Memory("NAS_Test.nas");
        z80 = new Z80Core(z80Memory, new Z80IO());
        z80.reset();
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Verify instructions not covered in the main test program [ED prefix set]
     */
    @Test
    public final void testED1() {
        int addr = 0xC000;
        // IN / OUT B
        z80Memory.writeByte(addr++, 0x06); // LD B
        z80Memory.writeByte(addr++, 0x45); //
        z80Memory.writeByte(addr++, 0x0E); // LD C
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xED); // OUT (C),B
        z80Memory.writeByte(addr++, 0x41); //
        z80Memory.writeByte(addr++, 0xED); // IN B,(C)
        z80Memory.writeByte(addr++, 0x40); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x4534, z80.getRegisterValue(CPUConstants.RegisterNames.BC));
        //
        // RETN
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xE5); // PUSH HL
        z80Memory.writeByte(addr++, 0xED); // RETN
        z80Memory.writeByte(addr++, 0x45); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // LD I,A
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xED); // LD I,A
        z80Memory.writeByte(addr++, 0x47); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x00, z80.getRegisterValue(CPUConstants.RegisterNames.I));
        //
        // LD I,A
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x80); //
        z80Memory.writeByte(addr++, 0xED); // LD I,A
        z80Memory.writeByte(addr++, 0x47); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x80, z80.getRegisterValue(CPUConstants.RegisterNames.I));
    }

    /**
     * Verify instructions not covered in the main test program [ED prefix set] [Second replication]
     */
    @Test
    public final void testED2() {
        int addr = 0xC000;
        // IN / OUT C
        z80Memory.writeByte(addr++, 0x0E); // LD C
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xED); // OUT (C),C
        z80Memory.writeByte(addr++, 0x49); //
        z80Memory.writeByte(addr++, 0xED); // IN C,(C)
        z80Memory.writeByte(addr++, 0x48); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0034, z80.getRegisterValue(CPUConstants.RegisterNames.BC));
        //
        // RETI
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xE5); // PUSH HL
        z80Memory.writeByte(addr++, 0xED); // RETI
        z80Memory.writeByte(addr++, 0x4D); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // NEG
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x01); //
        z80Memory.writeByte(addr++, 0xED); // NEG
        z80Memory.writeByte(addr, 0x4C); //
        z80.reset();
        run(0xC000);
        assertEquals(0xFF, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // LD A,I
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xED); // LD I,A
        z80Memory.writeByte(addr++, 0x47); //
        z80Memory.writeByte(addr++, 0xED); // LD A,I
        z80Memory.writeByte(addr++, 0x57); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x80); //
        z80Memory.writeByte(addr++, 0xED); // LD I,A
        z80Memory.writeByte(addr++, 0x47); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr++, 0xED); // LD A,I
        z80Memory.writeByte(addr++, 0x57); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x80, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        assertEquals(0x80, z80.getRegisterValue(CPUConstants.RegisterNames.I));
    }

    /**
     * Verify instructions not covered in the main test program [ED prefix set] [Third replication]
     */
    @Test
    public final void testED3() {
        int addr = 0xC000;
        // IN / OUT D
        z80Memory.writeByte(addr++, 0x16); // LD D
        z80Memory.writeByte(addr++, 0x45); //
        z80Memory.writeByte(addr++, 0x0E); // LD C
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xED); // OUT (C),D
        z80Memory.writeByte(addr++, 0x51); //
        z80Memory.writeByte(addr++, 0xED); // IN D,(C)
        z80Memory.writeByte(addr++, 0x50); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x4500, z80.getRegisterValue(CPUConstants.RegisterNames.DE));
        //
        // RETN
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xE5); // PUSH HL
        z80Memory.writeByte(addr++, 0xED); // RETN
        z80Memory.writeByte(addr++, 0x55); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // NEG
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x01); //
        z80Memory.writeByte(addr++, 0xED); // NEG
        z80Memory.writeByte(addr, 0x54); //
        z80.reset();
        run(0xC000);
        assertEquals(0xFF, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // LD R,A
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x00); // LD A
        z80Memory.writeByte(addr++, 0xF7); //
        z80Memory.writeByte(addr++, 0xED); // LD R,A
        z80Memory.writeByte(addr++, 0x4F); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x00, z80.getRegisterValue(CPUConstants.RegisterNames.R));
        //
        // LD R,A
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xED); // LD R,A
        z80Memory.writeByte(addr++, 0x4F); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x34, z80.getRegisterValue(CPUConstants.RegisterNames.R));
    }

    /**
     * Verify instructions not covered in the main test program [ED prefix set] [Fourth replication]
     */
    @Test
    public final void testED4() {
        int addr = 0xC000;
        // IN / OUT E
        z80Memory.writeByte(addr++, 0x1E); // LD E
        z80Memory.writeByte(addr++, 0x45); //
        z80Memory.writeByte(addr++, 0x0E); // LD C
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xED); // OUT (C),E
        z80Memory.writeByte(addr++, 0x59); //
        z80Memory.writeByte(addr++, 0xED); // IN E,(C)
        z80Memory.writeByte(addr++, 0x58); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0045, z80.getRegisterValue(CPUConstants.RegisterNames.DE));
        //
        // RETN
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xE5); // PUSH HL
        z80Memory.writeByte(addr++, 0xED); // RETN
        z80Memory.writeByte(addr++, 0x5D); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // NEG
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x01); //
        z80Memory.writeByte(addr++, 0xED); // NEG
        z80Memory.writeByte(addr, 0x5C); //
        z80.reset();
        run(0xC000);
        assertEquals(0xFF, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // LD A,R
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xED); // LD R,A
        z80Memory.writeByte(addr++, 0x4F); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x80); //
        z80Memory.writeByte(addr++, 0xED); // LD R,A
        z80Memory.writeByte(addr++, 0x4F); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr++, 0xED); // LD A,R
        z80Memory.writeByte(addr++, 0x5F); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x00, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        assertEquals(0x80, z80.getRegisterValue(CPUConstants.RegisterNames.R));
    }

    /**
     * Verify instructions not covered in the main test program [ED prefix set] [Fifth replication]
     */
    @Test
    public final void testED5() {
        int addr = 0xC000;
        // IN / OUT H
        z80Memory.writeByte(addr++, 0x26); // LD H
        z80Memory.writeByte(addr++, 0x45); //
        z80Memory.writeByte(addr++, 0x0E); // LD C
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xED); // OUT (C),H
        z80Memory.writeByte(addr++, 0x61); //
        z80Memory.writeByte(addr++, 0xED); // IN H,(C)
        z80Memory.writeByte(addr++, 0x60); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x4500, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        //
        // RETN
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xE5); // PUSH HL
        z80Memory.writeByte(addr++, 0xED); // RETN
        z80Memory.writeByte(addr++, 0x65); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // NEG
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x01); //
        z80Memory.writeByte(addr++, 0xED); // NEG
        z80Memory.writeByte(addr, 0x64); //
        z80.reset();
        run(0xC000);
        assertEquals(0xFF, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // LD A,R
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x45); //
        z80Memory.writeByte(addr++, 0xED); // LD R,A
        z80Memory.writeByte(addr++, 0x4F); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xED); // LD A,R
        z80Memory.writeByte(addr++, 0x5F); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x45, z80.getRegisterValue(CPUConstants.RegisterNames.A));
    }

    /**
     * Verify instructions not covered in the main test program [ED prefix set] [Sixth replication]
     */
    @Test
    public final void testED6() {
        int addr = 0xC000;
        // IN / OUT L
        z80Memory.writeByte(addr++, 0x2E); // LD L
        z80Memory.writeByte(addr++, 0x45); //
        z80Memory.writeByte(addr++, 0x0E); // LD C
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xED); // OUT (C),A
        z80Memory.writeByte(addr++, 0x69); //
        z80Memory.writeByte(addr++, 0xED); // IN A,(C)
        z80Memory.writeByte(addr++, 0x68); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0045, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        //
        // RETN
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xE5); // PUSH HL
        z80Memory.writeByte(addr++, 0xED); // RETN
        z80Memory.writeByte(addr++, 0x6D); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // NEG
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x01); //
        z80Memory.writeByte(addr++, 0xED); // NEG
        z80Memory.writeByte(addr, 0x6C); //
        z80.reset();
        run(0xC000);
        assertEquals(0xFF, z80.getRegisterValue(CPUConstants.RegisterNames.A));
    }

    /**
     * Verify instructions not covered in the main test program [ED prefix set] [7th replication]
     */
    @Test
    public final void testED7() {
        int addr = 0xC000;
        // IN / OUT F - Uber weird
        z80Memory.writeByte(addr++, 0x0E); // LD C
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xED); // OUT (C),F (Actually zero)
        z80Memory.writeByte(addr++, 0x71); //
        z80Memory.writeByte(addr++, 0xED); // IN ??,(C)
        z80Memory.writeByte(addr++, 0x70); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x44, z80.getRegisterValue(CPUConstants.RegisterNames.F));
        //
        // RETN
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xE5); // PUSH HL
        z80Memory.writeByte(addr++, 0xED); // RETN
        z80Memory.writeByte(addr++, 0x75); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // NEG
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x01); //
        z80Memory.writeByte(addr++, 0xED); // NEG
        z80Memory.writeByte(addr, 0x74); //
        z80.reset();
        run(0xC000);
        assertEquals(0xFF, z80.getRegisterValue(CPUConstants.RegisterNames.A));
    }

    /**
     * Verify instructions not covered in the main test program [ED prefix set] [8th replication]
     */
    @Test
    public final void testED8() {
        int addr = 0xC000;
        // IN / OUT
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x45); //
        z80Memory.writeByte(addr++, 0x0E); // LD C
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xED); // OUT (C),A
        z80Memory.writeByte(addr++, 0x79); //
        z80Memory.writeByte(addr++, 0xED); // IN A,(C)
        z80Memory.writeByte(addr++, 0x78); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x45, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // RETN
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xE5); // PUSH HL
        z80Memory.writeByte(addr++, 0xED); // RETN
        z80Memory.writeByte(addr++, 0x7D); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // NEG
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x01); //
        z80Memory.writeByte(addr++, 0xED); // NEG
        z80Memory.writeByte(addr, 0x7C); //
        z80.reset();
        run(0xC000);
        assertEquals(0xFF, z80.getRegisterValue(CPUConstants.RegisterNames.A));
    }

    /**
     * Verify instructions not covered in the main test program [ED prefix set] Odds 'n' ends
     */
    @Test
    public final void testEDVarious() {
        int addr = 0xC000;
        // LD (nn),HL
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x12); //
        z80Memory.writeByte(addr++, 0xED); // LD (NNNN), HL
        z80Memory.writeByte(addr++, 0x63); //
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x78); //
        z80Memory.writeByte(addr++, 0x56); //
        z80Memory.writeByte(addr++, 0xED); // LD HL, (NNNN)
        z80Memory.writeByte(addr++, 0x6B); //
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x1234, z80Memory.readWord(0x1000));
        assertEquals(0x1234, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        //
        addr = 0xC000;
        // LD (nn),HL
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0xFF); //
        z80Memory.writeByte(addr++, 0xFF); //
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x01); //
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xED); // ADC HL,BC
        z80Memory.writeByte(addr++, 0x4A); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0000, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        //
        addr = 0xC000;
        // IN / OUT (Checking S flag)
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x80); //
        z80Memory.writeByte(addr++, 0x0E); // LD C
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xED); // OUT (C),A
        z80Memory.writeByte(addr++, 0x79); //
        z80Memory.writeByte(addr++, 0xED); // IN A,(C)
        z80Memory.writeByte(addr++, 0x40); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x80, z80.getRegisterValue(CPUConstants.RegisterNames.A));
    }

    /**
     * Verify instructions not covered in the main test program [ED prefix set] Odds 'n' ends
     */
    @Test
    public final void testEDVariousIO() {
        //
        // INI
        int addr = 0xC000;
        z80Memory.writeByte(addr++, 0xD3); // OUT A
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x02); //
        z80Memory.writeByte(addr++, 0xED); // INI
        z80Memory.writeByte(addr++, 0xA2); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        z80Memory.writeByte(0x1000, 0x77); // data
        z80.reset();
        run(0xC000);
        assertEquals(0x1001, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        assertEquals(0x0134, z80.getRegisterValue(CPUConstants.RegisterNames.BC));
        assertEquals(0x00, z80Memory.readByte(0x1000));
        //
        // OUTI
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x02); //
        z80Memory.writeByte(addr++, 0xED); // OUTI
        z80Memory.writeByte(addr++, 0xA3); //
        z80Memory.writeByte(addr++, 0xDB); // IN A
        z80Memory.writeByte(addr++, 0x34); // HALT
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        z80Memory.writeByte(0x1000, 0x77); // data
        z80.reset();
        run(0xC000);
        assertEquals(0x1001, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        assertEquals(0x0134, z80.getRegisterValue(CPUConstants.RegisterNames.BC));
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // IND
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0xD3); // OUT A
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x02); //
        z80Memory.writeByte(addr++, 0xED); // IND
        z80Memory.writeByte(addr++, 0xAA); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        z80Memory.writeByte(0x1000, 0x77); // data
        z80.reset();
        run(0xC000);
        assertEquals(0x0FFF, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        assertEquals(0x0134, z80.getRegisterValue(CPUConstants.RegisterNames.BC));
        assertEquals(0x00, z80Memory.readByte(0x1000));
        //
        // OUTD
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x02); //
        z80Memory.writeByte(addr++, 0xED); // OUTD
        z80Memory.writeByte(addr++, 0xAB); //
        z80Memory.writeByte(addr++, 0xDB); // IN A
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        z80Memory.writeByte(0x1000, 0x77); // data
        z80.reset();
        run(0xC000);
        assertEquals(0x0FFF, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        assertEquals(0x0134, z80.getRegisterValue(CPUConstants.RegisterNames.BC));
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));

        //
        // INIR
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0xD3); // OUT A
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x02); //
        z80Memory.writeByte(addr++, 0xED); // INIR
        z80Memory.writeByte(addr++, 0xB2); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        z80Memory.writeByte(0x1000, 0x55); // data
        z80Memory.writeByte(0x1000, 0x77); // data
        z80.reset();
        run(0xC000);
        assertEquals(0x1002, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        assertEquals(0x0034, z80.getRegisterValue(CPUConstants.RegisterNames.BC));
        assertEquals(0x00, z80Memory.readByte(0x1000));
        //
        // OUTIR
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x02); //
        z80Memory.writeByte(addr++, 0xED); // OUTI
        z80Memory.writeByte(addr++, 0xB3); //
        z80Memory.writeByte(addr++, 0xDB); // IN A
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        z80Memory.writeByte(0x1000, 0x55); // data
        z80Memory.writeByte(0x1001, 0x77); // data
        z80.reset();
        run(0xC000);
        assertEquals(0x1002, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        assertEquals(0x0034, z80.getRegisterValue(CPUConstants.RegisterNames.BC));
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));
        //
        // INDR
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0xD3); // OUT A
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x02); //
        z80Memory.writeByte(addr++, 0xED); // IND
        z80Memory.writeByte(addr++, 0xBA); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        z80Memory.writeByte(0x1000, 0x55); // data
        z80Memory.writeByte(0x0FFF, 0x77); // data
        z80.reset();
        run(0xC000);
        assertEquals(0x0FFE, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        assertEquals(0x0034, z80.getRegisterValue(CPUConstants.RegisterNames.BC));
        assertEquals(0x00, z80Memory.readByte(0x1000));
        //
        // OUTDR
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x02); //
        z80Memory.writeByte(addr++, 0xED); // OUTD
        z80Memory.writeByte(addr++, 0xBB); //
        z80Memory.writeByte(addr++, 0xDB); // IN A
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        z80Memory.writeByte(0x1000, 0x55); // data
        z80Memory.writeByte(0x0FFF, 0x77); // data
        z80.reset();
        run(0xC000);
        assertEquals(0x0FFE, z80.getRegisterValue(CPUConstants.RegisterNames.HL));
        assertEquals(0x0034, z80.getRegisterValue(CPUConstants.RegisterNames.BC));
        assertEquals(0x77, z80.getRegisterValue(CPUConstants.RegisterNames.A));

    }

    /**
     * Not implemented (act as NOP's) instructions - just call for completeness
     */
    @Test
    public final void testNotImplementedED() {
        int addr = 0xC000;
        // IM
        z80Memory.writeByte(addr++, 0xED); // IM (0)
        z80Memory.writeByte(addr++, 0x46); //
        //
        z80Memory.writeByte(addr++, 0xED); // IM (0)
        z80Memory.writeByte(addr++, 0x4E); //
        //
        z80Memory.writeByte(addr++, 0xED); // IM (1)
        z80Memory.writeByte(addr++, 0x56); //
        //
        z80Memory.writeByte(addr++, 0xED); // IM (1)
        z80Memory.writeByte(addr++, 0x5E); //
        //
        z80Memory.writeByte(addr++, 0xED); // IM (1)
        z80Memory.writeByte(addr++, 0x66); //
        //
        z80Memory.writeByte(addr++, 0xED); // IM (1)
        z80Memory.writeByte(addr++, 0x6E); //
        //
        z80Memory.writeByte(addr++, 0xED); // IM (1)
        z80Memory.writeByte(addr++, 0x76); //
        //
        z80Memory.writeByte(addr++, 0xED); // IM (2)
        z80Memory.writeByte(addr++, 0x7E); //
        //
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        z80.reset();
        run(0xC000);
    }

    private void run(int address) { //
        // Ok, run the program
        z80.setProgramCounter(address);
        while (!z80.getHalt()) {
            try {
                // System.out.println(utilities.getWord(z80.getRegisterValue(RegisterNames.PC)));
                z80.executeOneInstruction();
            } catch (Exception e) {
                System.out.println("Hardware crash, oops! " + e.getMessage());
            }
        }
    }

}
