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

import com.codingrodent.microprocessor.Z80.CPUConstants.RegisterNames;
import com.codingrodent.microprocessor.support.Z80IO;
import com.codingrodent.microprocessor.support.Z80Memory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Z80CoreTestInterrupts {
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

}
