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

import com.codingrodent.microprocessor.support.Z80IO;
import com.codingrodent.microprocessor.support.Z80Memory;
import com.codingrodent.microprocessor.z80.CPUConstants.RegisterNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Z80CoreInterruptsTest {
    private Z80Core z80;
    private Z80Memory z80Memory;

    @BeforeEach
    public void setUp() {
        z80Memory = new Z80Memory("NAS_Test.nas");
        z80 = new Z80Core(z80Memory, new Z80IO());
        z80.reset();
    }

    /**
     * Does NMI work ?
     */
    @Test
    public final void testNMI() {
        int addr = 0xC000;
        z80Memory.writeByte(addr++, 0xF3); // DI
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0x0066;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x00); // NOP
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000, true);
        assertEquals(0x02, z80.getRegisterValue(RegisterNames.A));
        //
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0xFB); // EI
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0x0066;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x00); // NOP
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000, true);
        assertEquals(0x02, z80.getRegisterValue(RegisterNames.A));
        //
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x00); // NOP
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0x0066;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x00); // NOP
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000, true);
        assertEquals(0x01, z80.getRegisterValue(RegisterNames.A));
        //
        // Bug check - incorrect return address pushed !
        assertEquals(0xC001, z80Memory.readWord(0xFFFE));
        //
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0x0066;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x00); // NOP
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000, true);
        //
        // Bug check - incorrect return address pushed !
        assertEquals(0xC001, z80Memory.readWord(0xFFFE));
    }

    private void run(int address, boolean nmi) { //
        // Ok, run the program
        z80.setProgramCounter(address);
        int haltCount = 2;
        while (!z80.getHalt() || haltCount != 0) {
            if (z80.getHalt()) {
                --haltCount;
            }
            try {
                // System.out.println(utilities.getWord(z80.getRegisterValue(RegisterNames.PC)));
                z80.executeOneInstruction();
                if (nmi) {
                    nmi = false;
                    z80.setNMI();
                }
            } catch (Exception e) {
                System.out.println("Hardware crash, oops! " + e.getMessage());
            }
        }
    }

    @Test
    public final void testNMIdisabledAfterEIDI() {

        // Test DI Block NMI's for 1 additional instruction, after DI

        int addr = 0xC000;
        z80Memory.writeByte(addr++, 0xF3); // DI
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x00); // NOP
        z80Memory.writeByte(addr, 0x76); // HALT
        //
        addr = 0x0066;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x00); // NOP
        z80Memory.writeByte(addr, 0x76); // HALT

        // executing (with DI) with NMI after DI instruction
        testNMI(0xC000, 1);
        // thus NMI will be delayed by 1 instruction, so 2 x INC
        assertEquals(0x02, z80.getRegisterValue(RegisterNames.A));

        // executing (with DI) with NMI after INC instruction
        testNMI(0xC000, 2);
        // thus NMI will be delayed by 1 instruction, so 2 x INC
        assertEquals(0x02, z80.getRegisterValue(RegisterNames.A));

        // executing (without DI) with NMI after INC instruction
        testNMI(0xC001, 1);
        // thus NMI will be delayed by 1 instruction, so 2 x INC
        assertEquals(0x02, z80.getRegisterValue(RegisterNames.A));

        // Now Same Tests for EI

        addr = 0xC000;
        z80Memory.writeByte(addr, 0xFB); // EI

        // executing (with EI) with NMI after EI instruction
        testNMI(0xC000, 1);
        // thus NMI will be delayed by 1 instruction, so 2 x INC
        assertEquals(0x02, z80.getRegisterValue(RegisterNames.A));

        // executing (with EI) with NMI after INC instruction
        testNMI(0xC000, 2);
        // thus NMI will be delayed by 1 instruction, so 2 x INC
        assertEquals(0x02, z80.getRegisterValue(RegisterNames.A));

        // executing (without EI) with NMI after INC instruction
        testNMI(0xC001, 1);
        // thus NMI will be delayed by 1 instruction, so 2 x INC
        assertEquals(0x02, z80.getRegisterValue(RegisterNames.A));

    }

    private void testNMI(int address, int nmiCount) {
        // Ok, run the program
        z80.reset();
        z80.setProgramCounter(address);
        while (!z80.getHalt()) {
            try {
                // System.out.println(utilities.getWord(z80.getRegisterValue(RegisterNames.PC)));
                z80.executeOneInstruction();
                if (--nmiCount == 0) { // dec and test
                    z80.setNMI(); // force the NMI after N instructions
                }
            } catch (Exception e) {
                System.out.println("Hardware crash, oops! " + e.getMessage());
            }
        }
    }
}
