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
 */package net.codingrodent.microprocessor.support;

import java.io.*;

import net.codingrodent.microprocessor.IMemory;

public class Z80Memory implements IMemory
{
	private int[]	memory	= new int[65536];

	public Z80Memory()
	{
		// Set all to HALT - stops runaway code
		for (int a = 0; a < memory.length; a++)
		{
			memory[a] = 0x76;
		}
		// A very simple I/O routine to simulate NAS-SYS character output call
		memory[0x30] = 0xD3; // out (00), a
		memory[0x31] = 0x00; //
		memory[0x32] = 0xC9; // ret
		//
		try
		{
			readHexDumpFile("NAS_Test.nas");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Read a standard tape dump file into an array and return
	 * 
	 * @param fileName
	 *            The file to read
	 * @return The block of memory read
	 * @throws FileNotFoundException
	 *             Thrown if the file specified does not exist
	 * @throws IOException
	 *             Thrown if a failure occurs while reading the file
	 */
	public void readHexDumpFile(String fileName) throws FileNotFoundException, IOException
	{
		Utilities Utilities = new Utilities();
		String line;
		int address, base;
		LineNumberReader source = new LineNumberReader(new FileReader(fileName));
		//
		boolean firstTime = true;
		while (true)
		{ // read a line

			String inputLine = source.readLine();
			if ((null == inputLine) || (inputLine.charAt(0) == '.'))
			{
				break;
			}
			line = inputLine.trim();
			// System.out.println("<" + line + ">");

			// convert and place into memory
			address = Utilities.getHexValue(line.substring(0, 4));
			// System.out.println("Address : " + address + " : " + line.substring(0, 4));
			if (firstTime)
			{
				firstTime = false;
			}
			base = 5;
			for (int i = 0; i < 8; i++)
			{
				int value = Utilities.getHexValue(line.substring(base, base + 2));
				memory[address] = value;
				base = base + 3;
				address++;
			}
		}
		source.close();
	}

	@Override
	// Read a byte from memory
	public int readByte(int address)
	{
		return memory[address];
	}

	@Override
	// Read a word from memory
	public int readWord(int address)
	{
		return readByte(address) + readByte(address + 1) * 256;
	}

	@Override
	public void writeByte(int address, int data)
	{
		memory[address] = data;
	}

	@Override
	public void writeWord(int address, int data)
	{
		writeByte(address, (data & 0x00FF));
		address = (address + 1) & 65535;
		data = (data >>> 8);
		writeByte(address, data);
	}
}
