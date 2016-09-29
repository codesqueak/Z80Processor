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

import static org.junit.Assert.*;

public class Z80CoreTest {
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
     * Test core by running test application this covers most instructions. Other tests are to 'mop up' uncovered
     * regions
     */
    @Test
    public final void testCore() {
        // Initial setup
        assertEquals(z80.getProgramCounter(), 0x0000);
        z80.setResetAddress(0x1234);
        z80.reset();
        assertEquals(z80.getProgramCounter(), 0x1234);
        z80.setProgramCounter(0x1000);
        assertEquals(z80.getProgramCounter(), 0x1000);
        //
        // T states ?
        assertEquals(0, z80.getTStates());
        //
        // Ok, run the program
        while (!z80.getHalt()) {
            try {
                z80.executeOneInstruction();
                // System.out.println(utilities.getWord(z80.getRegisterValue(RegisterNames.PC)));
            } catch (ProcessorException e) {
                System.out.println("Hardware crash, oops! " + e.getMessage());
                e.printStackTrace();
            }
        }
        assertTrue(z80.getTStates() > 0);
        z80.resetTStates();
        assertEquals(0, z80.getTStates());

    }

    /**
     * Check LDIR / LDDIR function
     */
    @Test
    public final void testBlockMove() {
        // Initial setup
        int loc = 0xC000;
        z80.setProgramCounter(loc);
        z80Memory.writeByte(loc++, 0x03); // inc BC
        z80Memory.writeByte(loc++, 0x03); // inc BC
        z80Memory.writeByte(loc++, 0xED); // ldir
        z80Memory.writeByte(loc++, 0xB0); //
        z80Memory.writeByte(loc++, 0x03); // inc BC
        z80Memory.writeByte(loc++, 0x03); // inc BC
        z80Memory.writeByte(loc++, 0xED); // lddr
        z80Memory.writeByte(loc++, 0xB8); //
        z80Memory.writeByte(loc, 0x76); // halt

        //
        // Ok, run the program
        while (!z80.getHalt()) {
            try {
                z80.executeOneInstruction();
                if (z80.blockMoveInProgress()) {
                    fail("block moves now internalized for performance - should never get here");
                }
            } catch (ProcessorException e) {
                System.out.println("Hardware crash, oops! " + e.getMessage());
            }
        }
    }

    /**
     * Verify instructions not covered in the main test program
     */
    @Test
    public final void testGeneralInstructions() {
        int addr = 0xC000;
        // EX AF AF
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x12); //
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xC5); // PUSH BC
        z80Memory.writeByte(addr++, 0xF1); // POP AF
        z80Memory.writeByte(addr++, 0x08); // EX AF AF
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x00, z80.getRegisterValue(RegisterNames.A));
        assertEquals(0x00, z80.getRegisterValue(RegisterNames.F));
        assertEquals(0x34, z80.getRegisterValue(RegisterNames.A_ALT));
        assertEquals(0x12, z80.getRegisterValue(RegisterNames.F_ALT));
        //
        // DJNZ
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x04); //
        z80Memory.writeByte(addr++, 0x0C); // INC C
        z80Memory.writeByte(addr++, 0x10); // DJNZ
        z80Memory.writeByte(addr++, 0xFD); // -2
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xD000);
        assertEquals(0x0004, z80.getRegisterValue(RegisterNames.BC));
        //
        // JR NZ
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x20); // JR NZ
        z80Memory.writeByte(addr++, 0x01); // 1
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x3D); // DEC A
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xC0); // RET NZ
        z80Memory.writeByte(addr++, 0x20); // JR NZ
        z80Memory.writeByte(addr++, 0x01); // 1
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x01, z80.getRegisterValue(RegisterNames.A));
        //
        // JR NC
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x37); // SCF
        z80Memory.writeByte(addr++, 0x30); // JR NC
        z80Memory.writeByte(addr++, 0x02); // 2
        z80Memory.writeByte(addr++, 0xEE); // XOR 0xFF
        z80Memory.writeByte(addr++, 0xFF); //
        z80Memory.writeByte(addr++, 0x30); // JR NC
        z80Memory.writeByte(addr++, 0x00); // 0
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0xFF, z80.getRegisterValue(RegisterNames.A));
        //
        // JR C
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x38); // JR C
        z80Memory.writeByte(addr++, 0x02); // 2
        z80Memory.writeByte(addr++, 0xEE); // XOR 0xFF
        z80Memory.writeByte(addr++, 0xFF); //
        z80Memory.writeByte(addr++, 0x37); // SCF
        z80Memory.writeByte(addr++, 0x38); // JR C
        z80Memory.writeByte(addr++, 0x00); // 1
        z80Memory.writeByte(addr++, 0x0D); // DEC C
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xD000);
        assertEquals(0xFF, z80.getRegisterValue(RegisterNames.A));
        //
        // RET NZ
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xCD); // CALL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0x01); // LD BC
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x12); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0xC0); // RET NZ
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x1234, z80.getRegisterValue(RegisterNames.BC));
        //
        // CALL Z
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0xFF); //
        z80Memory.writeByte(addr++, 0xCC); // CALL Z
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xCC); // CALL Z
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xC9); // RET
        z80.reset();
        run(0xC000);
        assertEquals(0x01, z80.getRegisterValue(RegisterNames.A));
        //
        // RET NC / JP NC / CALL NC
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x37); // SCF
        z80Memory.writeByte(addr++, 0xD4); // CALL NC
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xD2); // JP NC
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0x3F); // CCF
        z80Memory.writeByte(addr++, 0xD4); // CALL NC
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0xFF); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xD2); // JP NC
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xE0); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x88); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xD0); // RET NC
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xE000;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xD0); // RET NC
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x02, z80.getRegisterValue(RegisterNames.A));
        //
        // RET C / JP C
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x37); // SCF
        z80Memory.writeByte(addr++, 0xD0); // RET NC
        z80Memory.writeByte(addr++, 0xD0); // JP NC
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xCD); // CALL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xDA); // JP C
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xE0); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0xD8); // RET C
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xE000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x88); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x88, z80.getRegisterValue(RegisterNames.A));
        //
        // EXX / EX (SP),HL
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x03); // INC BC
        z80Memory.writeByte(addr++, 0x13); // INC DE
        z80Memory.writeByte(addr++, 0x13); // INC DE
        z80Memory.writeByte(addr++, 0x23); // INC HL
        z80Memory.writeByte(addr++, 0x23); // INC HL
        z80Memory.writeByte(addr++, 0x23); // INC HL
        z80Memory.writeByte(addr++, 0xD9); // EXX
        z80Memory.writeByte(addr++, 0x11); // LD DE
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x12); //
        z80Memory.writeByte(addr++, 0xD5); // PUSH DE
        z80Memory.writeByte(addr++, 0xE3); // EXX / EX (SP),HL
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x0001, z80.getRegisterValue(RegisterNames.BC_ALT));
        assertEquals(0x0002, z80.getRegisterValue(RegisterNames.DE_ALT));
        assertEquals(0x0003, z80.getRegisterValue(RegisterNames.HL_ALT));
        assertEquals(0x1234, z80.getRegisterValue(RegisterNames.HL));
        //
        // IN A
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x12); //
        z80Memory.writeByte(addr++, 0xD3); // OUT A
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0xDB); // IN A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x12, z80.getRegisterValue(RegisterNames.A));
        //
        // RET PO / JP PO / CALL PO
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x7F); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xE4); // CALL PO
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xE0); // RET PO
        z80Memory.writeByte(addr++, 0xE2); // JP PO
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        //
        z80Memory.writeByte(addr++, 0x3D); // DEC A
        z80Memory.writeByte(addr++, 0x3D); // DEC A
        z80Memory.writeByte(addr++, 0xE4); // CALL PO
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0xFF); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xE2); // JP PO
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xE0); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x88); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xE0); // RET PO
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xE000;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xE0); // RET PO
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x02, z80.getRegisterValue(RegisterNames.A));
        //
        // RET PE / JP PE / CALL PE
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x7F); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xEC); // CALL PE
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x7F); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xEA); // JP PE
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xE0); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x88); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xE0); // RET PE
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xE000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xE8); // RET PE
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x01, z80.getRegisterValue(RegisterNames.A));
        //
        // JMP (HL)
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x33); //
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xE0); //
        z80Memory.writeByte(addr++, 0xE9); // JMP (HL)
        z80Memory.writeByte(addr++, 0xD0); // HALT
        //
        addr = 0xE000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr++, 0xD0); // HALT
        //
        z80.reset();
        run(0xC000);
        assertEquals(0x77, z80.getRegisterValue(RegisterNames.A));
        //
        // LD SP,HL
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x21); // LD HL
        z80Memory.writeByte(addr++, 0x34); //
        z80Memory.writeByte(addr++, 0x12); //
        z80Memory.writeByte(addr++, 0xF9); // LD SP,HL
        z80Memory.writeByte(addr++, 0xD0); // HALT
        //
        z80.reset();
        run(0xC000);
        assertEquals(0x1236, z80.getRegisterValue(RegisterNames.SP));
        //
        // RET NS / JP NS / CALL NS
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x80); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xF4); // CALL NS
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0xF2); // JP NS
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x08); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xF4); // CALL NS
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x43); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xF2); // JP NS
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xE0); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x88); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x3C); // DEC A
        z80Memory.writeByte(addr++, 0xF0); // RET NS
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xE000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x07F); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xF0); // RET NS
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x80, z80.getRegisterValue(RegisterNames.A));
        //
        // RET S / JP S / CALL S
        addr = 0xC000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x7F); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xFC); // CALL S
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xD0); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0xAA); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xFA); // JP S
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0xE0); //
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x88); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xD000;
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0x3C); // DEC A
        z80Memory.writeByte(addr++, 0xF8); // RET S
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x77); //
        z80Memory.writeByte(addr++, 0x76); // HALT
        //
        addr = 0xE000;
        z80Memory.writeByte(addr++, 0x3E); // LD A
        z80Memory.writeByte(addr++, 0x11); //
        z80Memory.writeByte(addr++, 0x3C); // INC A
        z80Memory.writeByte(addr++, 0xF8); // RET S
        z80Memory.writeByte(addr++, 0x76); // HALT
        z80.reset();
        run(0xC000);
        assertEquals(0x12, z80.getRegisterValue(RegisterNames.A));
    }

    /**
     * Verify RST instructions - hard to do in an emulated environment due to system ROMs
     */
    @Test
    public final void testRST() {
        int addr = 0xC000;
        //
        //
        z80Memory.writeByte(addr++, 0x31); // LD SP
        z80Memory.writeByte(addr++, 0x00); //
        z80Memory.writeByte(addr++, 0x10); //
        //
        z80Memory.writeByte(addr++, 0xC7); // RST 0
        run(0xC000);
        assertEquals(0x0000, z80.getProgramCounter());
        //
        addr = 0xC000;
        z80.reset();
        z80Memory.writeByte(addr++, 0xCF); // RST 8
        run(0xC000);
        assertEquals(0x0008, z80.getProgramCounter());
        //
        addr = 0xC000;
        z80.reset();
        z80Memory.writeByte(addr++, 0xD7); // RST 10
        run(0xC000);
        assertEquals(0x0010, z80.getProgramCounter());
        //
        addr = 0xC000;
        z80.reset();
        z80Memory.writeByte(addr++, 0xDF); // RST 18
        run(0xC000);
        assertEquals(0x0018, z80.getProgramCounter());
        //
        addr = 0xC000;
        z80.reset();
        z80Memory.writeByte(addr++, 0xE7); // RST 20
        run(0xC000);
        assertEquals(0x0020, z80.getProgramCounter());
        //
        addr = 0xC000;
        z80.reset();
        z80Memory.writeByte(addr++, 0xEF); // RST 28
        run(0xC000);
        assertEquals(0x0028, z80.getProgramCounter());
        //
        addr = 0xC000;
        z80.reset();
        z80Memory.writeByte(addr++, 0xF7); // RST 30
        z80Memory.writeByte(addr++, 0x76); // HALT
        run(0xC000);
        assertEquals(0xC001, z80.getProgramCounter());
        //
        addr = 0xC000;
        z80.reset();
        z80Memory.writeByte(addr++, 0xFF); // RST 48
        run(0xC000);
        assertEquals(0x0038, z80.getProgramCounter());
    }

    /**
     * General util function tests
     */
    @Test
    public final void testUtils() {
        assertEquals(0x00, z80.getRegisterValue(RegisterNames.A));
        assertEquals(0x00, z80.getRegisterValue(RegisterNames.F));
        assertEquals(0x00, z80.getRegisterValue(RegisterNames.A_ALT));
        assertEquals(0x00, z80.getRegisterValue(RegisterNames.F_ALT));
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.BC));
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.DE));
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.HL));
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.BC_ALT));
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.DE_ALT));
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.HL_ALT));
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.SP));
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.PC));
        z80.setProgramCounter(0x1000);
        assertEquals(0x1000, z80.getProgramCounter());
        assertEquals(0x1000, z80.getRegisterValue(RegisterNames.PC));
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.IX));
        assertEquals(0x0000, z80.getRegisterValue(RegisterNames.IY));
        assertEquals(0x00, z80.getRegisterValue(RegisterNames.I));
        assertEquals(0x00, z80.getRegisterValue(RegisterNames.R));
        //
        assertEquals(z80.getMajorVersion(), "4");
        assertEquals(z80.getMinorVersion(), "0");
        assertEquals(z80.getPatchVersion(), "1");
        assertEquals(z80.getName(), "Z80A_NMOS");
        assertEquals(z80.toString(), "Z80A_NMOS Revision 4.0.1");
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
