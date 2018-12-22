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

import com.codingrodent.microprocessor.ProcessorException;
import com.codingrodent.microprocessor.Z80.CPUConstants.RegisterNames;
import com.codingrodent.microprocessor.support.*;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class Z80CoreTestCB {
    private Z80Core z80;
    private Z80Memory z80Memory;

    @Before
    public void setUp() throws Exception {
        z80Memory = new Z80Memory("NAS_Test.nas");
        z80 = new Z80Core(z80Memory, new Z80IO());
        z80.reset();
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Verify instructions not covered in the main test program [CB prefix set]
     */
    @Test
    public final void testCB() {
        int addr = 0xC000;
        // RLC B
        z80Memory.writeByte(addr++, 0x06); // LD B
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xCB); // RLC B
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.BC));
        //
        // RRC B
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x06); // LD B
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xCB); // RRC B
        z80Memory.writeByte(addr++, 0x08); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.BC));
        //
        // RL B
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x06); // LD B
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xCB); // RL B
        z80Memory.writeByte(addr++, 0x10); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.BC));
        //
        // RR B
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x06); // LD B
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xCB); // RR B
        z80Memory.writeByte(addr++, 0x18); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.BC));
        //
        // SLA B
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x06); // LD B
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xCB); // SLA B
        z80Memory.writeByte(addr++, 0x20); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.BC));
        //
        // SRA B
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x06); // LD B
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xCB); // SRA B
        z80Memory.writeByte(addr++, 0x28); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.BC));
        //
        // SLL B
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x06); // LD B
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xCB); // SLL B
        z80Memory.writeByte(addr++, 0x30); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0100, z80.getRegisterValue(RegisterNames.BC));
        //
        // SRL B
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x06); // LD B
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xCB); // SRL B
        z80Memory.writeByte(addr++, 0x38); //
        z80Memory.writeByte(addr, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.BC));
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
