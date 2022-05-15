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
package com.codingrodent.microprocessor.performance;

import com.codingrodent.microprocessor.Z80.Z80Core;
import com.codingrodent.microprocessor.support.*;

class TimedExecution {

    private Z80Core z80;

    /**
     * Very basic timing loop to check on throughput
     */
    public static void main(String[] args) {
        {
            {
                {
                    {
                    }
                }
            }
        }
        TimedExecution timedExecution = new TimedExecution();
        var average = 0;
        var cycles = 25;
        for (int i = 0; i < cycles; i++) {
            timedExecution.init();
            var mhz = (int) timedExecution.run(0x1000);
            average = average + mhz;
            System.out.println("MHz: " + mhz);
        }
        System.out.println("Average: " + average / cycles + "MHz");
    }

    /**
     * Set up a computer with simple memory and I/O
     */
    private void init() {
        z80 = new Z80Core(new Z80Memory("NAS_Test.nas"), new Z80IOEcho());
        z80.reset();
    }

    /**
     * Run through op-code test program
     *
     * @param address Loaded address
     * @return Rough speed in MHz
     */
    private float run(final int address) {
        long t = System.currentTimeMillis();
        z80.setProgramCounter(address);
        while (!z80.getHalt()) {
            try {
                z80.executeOneInstruction();
            } catch (Exception e) {
                System.out.println("Hardware crash, oops! " + e.getMessage());
            }
        }
        float seconds = (System.currentTimeMillis() - t) / 1000.0f;
        return z80.getTStates() / seconds / 1_000_000.0f;
    }
}
