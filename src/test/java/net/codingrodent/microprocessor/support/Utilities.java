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
package net.codingrodent.microprocessor.support;

public class Utilities
{
    private final String hexChar = "0123456789ABCDEF";

    /*
      Constructor
     */
    public Utilities()
    {
    }

    /*
      turn a 4 bit value into its equivalent hex digit
     */
    private char getHexCharacter(short value)
    {
        return hexChar.charAt(value);
    }

    /*
      turn a byte into two hex digits
     */
    public String getByte(int value)
    {

        char[] byteText = new char[2];
        try
        {
            byteText[0] = getHexCharacter( (short) (value >>> 4));
            byteText[1] = getHexCharacter( (short) (value & 0x0F));
        }
        catch (Exception e)
        {
            byteText[0] = '*';
            byteText[1] = '*';
        }
        return new String(byteText);
    }

    /*
      turn a word into four hex digits
     */
    public String getWord(int value)
    {
        return getByte( (short) (value >>> 8)) + getByte( (short) (value & 0x00FF));
    }

  /*
    convert a hex digit into an int
   */

    public int getHexDigit(char hex)
    {
        int i;
        for (i = 0; i < hexChar.length(); i++)
        {
            if (hexChar.charAt(i) == hex)
            {
                return i;
            }
        }
        return -1;
    }

  /*
    convert a hex string into an integer
   */

    public int getHexValue(String hex)
    {
        int total = 0;
        for (int i = 0; i < hex.length(); i++)
        {
            total = total * 16 + getHexDigit(hex.charAt(i));
        }
        return total;
    }

}