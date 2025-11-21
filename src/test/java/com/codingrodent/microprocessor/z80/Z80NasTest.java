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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Z80NasTest {
    private Z80Core z80;
    private int a = 0;

    @BeforeEach
    public void setUp() {
        Z80Memory z80Memory = new Z80Memory("NAS_Test.nas");
        z80 = new Z80Core(z80Memory, new Z80IO());
        z80.reset();
    }

    /**
     * Test core by running test application this covers most instructions. Other tests are to 'mop up' uncovered
     * regions
     */
    @Test
    public final void testCore() {
        // Initial setup
        assertEquals(0x0000, z80.getProgramCounter());
        z80.setResetAddress(0x1234);
        z80.reset();
        assertEquals(0x1234, z80.getProgramCounter());
        z80.setProgramCounter(0x1000);
        assertEquals(0x1000, z80.getProgramCounter());
        //
        // T states ?
        assertEquals(0, z80.getTStates());
        //
        // Ok, run the program
        while (!z80.getHalt()) {
            try {
                //       System.out.println(getRegs());
                a++;
                z80.executeOneInstruction();
            } catch (Exception e) {
                System.out.println("Hardware crash, oops! " + e.getMessage());
            }
        }
        assertTrue(z80.getTStates() > 0);
        z80.resetTStates();
        assertEquals(0, z80.getTStates());
        assertEquals(0, z80.getTStates());
    }

    private String getRegs() {
        return a + " >> Execute @" //
                + Utilities.getWord(z80.getRegisterValue(CPUConstants.RegisterNames.PC)) //
                + " : " + "00" + " SP:" + Utilities.getWord(z80.getRegisterValue(CPUConstants.RegisterNames.SP))  //
                + "  AF:" + Utilities.getByte(z80.getRegisterValue(CPUConstants.RegisterNames.A)) //
                + Utilities.getByte(z80.getRegisterValue(CPUConstants.RegisterNames.F)) //
                + "  BC:" + Utilities.getWord(z80.getRegisterValue(CPUConstants.RegisterNames.BC)) //
                + "  DE:" + Utilities.getWord(z80.getRegisterValue(CPUConstants.RegisterNames.DE)) //
                + "  HL:" + Utilities.getWord(z80.getRegisterValue(CPUConstants.RegisterNames.HL)) //
                + "  IX:" + Utilities.getWord(z80.getRegisterValue(CPUConstants.RegisterNames.IX)) //
                + "  IY:" + Utilities.getWord(z80.getRegisterValue(CPUConstants.RegisterNames.IY)); //

    }

}
