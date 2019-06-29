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
import com.codingrodent.microprocessor.support.*;
import org.junit.Before;

import static org.junit.Assert.*;

public class I8080CoreTest {
    private Z80Core z80;
    private Z80Memory z80Memory;

    @Before
    public void setUp() throws Exception {
        z80Memory = new Z80Memory("8080EX1.nas");
        z80 = new Z80Core(z80Memory, new Z80IO());
        z80.reset();
    }

    /**
     * Test core by running an initial i8080 test set
     */
 //   @Test
    public final void test8080Core() {
        // Initial setup
        z80.setProgramCounter(0x0100);
        assertEquals(z80.getProgramCounter(), 0x0100);
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

}
