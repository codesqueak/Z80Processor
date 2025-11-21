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
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class I8080CoreTest {
    private Z80Core z80;

    @BeforeEach
    public void setUp() {
        var z80Memory = new Z80Memory("8080EX1.nas");
        z80 = new Z80Core(z80Memory, new Z80IO());
        z80.reset();
    }

    /**
     * Test core by running an initial i8080 test set
     */
    @Disabled
    public void intel8080Core() {
        // Initial setup
        z80.setProgramCounter(0x0100);
        assertEquals(0x0100, z80.getProgramCounter());
        //
        // T states ?
        assertEquals(0, z80.getTStates());
        //
        // Ok, run the program
        while (!z80.getHalt()) {
            z80.executeOneInstruction();
        }
        assertTrue(z80.getTStates() > 0);
        z80.resetTStates();
        assertEquals(0, z80.getTStates());
    }

}
