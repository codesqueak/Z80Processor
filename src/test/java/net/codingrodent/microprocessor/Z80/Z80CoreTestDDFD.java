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
package net.codingrodent.microprocessor.Z80;

import net.codingrodent.microprocessor.ProcessorException;
import net.codingrodent.microprocessor.Z80.CPUConstants.RegisterNames;
import net.codingrodent.microprocessor.support.*;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class Z80CoreTestDDFD {
    private Z80Core z80;
    private Z80Memory z80Memory;

    @Before
    public void setUp() throws Exception {
        z80Memory = new Z80Memory();
        z80 = new Z80Core(z80Memory, new Z80IO());
        z80.reset();
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Verify instructions not covered in the main test program [DD prefix set]
     */
    @Test
    public final void testDD() {
        int addr = 0xC000;
        // JP (IX)
        z80Memory.writeByte(addr++, 0xDD); // LD IX
        z80Memory.writeByte(addr++, 0x21); //
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr++, 0xDD); // JP (IX)
        z80Memory.writeByte(addr++, 0xE9); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0x1000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x34, z80.getRegisterValue(RegisterNames.A));
        //
        // LD SP,IX
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0xDD); // LD IX
        z80Memory.writeByte(addr++, 0x21); //
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x12); //
        z80Memory.writeByte(addr++, 0xDD); // LD SP
        z80Memory.writeByte(addr++, 0xF9); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x1234, z80.getRegisterValue(RegisterNames.SP));
        //
        // EX (SP),IX
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x31); // LD SP
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr++, 0xDD); // LD IX
        z80Memory.writeByte(addr++, 0x21); //
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x12); //
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x78); //
        z80Memory.writeByte(addr++, 0x56); //
        z80Memory.writeByte(addr++, 0xE5); // PUSH HL
        z80Memory.writeByte(addr++, 0xDD); // EX (SP) IX
        z80Memory.writeByte(addr++, 0xE3);
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        z80.reset();
        run(0xC000);
        assertEquals(0x5678, z80.getRegisterValue(RegisterNames.IX));
    }

    /**
     * Not a test as such - here to give code coverage only.
     * <p>
     * Actual tests are done in the main test program but instructions are replicated in blocks of 8 and it only tests
     * one per block.
     * <p>
     * This code just exercises all other possible variants.
     * <p>
     * WARNING: A disagreement exists about what these instructions do. For example,
     * <p>
     * Test Program: #DD #CB nn #00 RLC (IX+nn)
     * <p>
     * Docs (e.g. http://www.z80.info/z80undoc.htm) : #DD #CB nn #00 RLC (IX+nn) & LD B,(IX+nn)
     * <p>
     * The issue is if the parallel load actually happens (e.g. LD B,(IX+nn)). The test program is derived from a real
     * chip so going with that for now. May change !!!
     */
    @Test
    public final void testDDCB() {
        int addr = 0xC000;
        //
        for (int opcode = 0; opcode <= 0xFF; opcode++) {
            z80Memory.writeByte(addr++, 0xDD); // DD FD
            z80Memory.writeByte(addr++, 0xCB); //
            z80Memory.writeByte(addr++, opcode); // Make sure we have +ve and =ve offsets
            z80Memory.writeByte(addr++, opcode); //
        }
        z80Memory.writeByte(addr++, 0x76); // HALT
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
            } catch (ProcessorException e) {
                System.out.println("Hardware crash, oops! " + e.getMessage());
            }
        }
    }

}
