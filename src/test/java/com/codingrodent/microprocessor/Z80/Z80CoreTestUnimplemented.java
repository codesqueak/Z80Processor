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

import com.codingrodent.microprocessor.support.*;
import org.junit.jupiter.api.*;

public class Z80CoreTestUnimplemented {
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

    private void run(int address) { //
        // Ok, run the program
        z80.setProgramCounter(address);
        while (!z80.getHalt()) {
            // System.out.println(utilities.getWord(z80.getRegisterValue(RegisterNames.PC)));
            z80.executeOneInstruction();
        }
    }

}
