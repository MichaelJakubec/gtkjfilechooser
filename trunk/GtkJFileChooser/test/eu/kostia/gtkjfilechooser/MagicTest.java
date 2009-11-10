/*
 * Copyright 2009 Costantino Cerbo.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 */
package eu.kostia.gtkjfilechooser;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.junit.Test;

import eu.kostia.gtkjfilechooser.Magic.Result;

/**
 * @author Costantino Cerbo
 * 
 */
public class MagicTest {

	/**
	 * Test method for
	 * {@link eu.kostia.gtkjfilechooser.Magic#Magic(java.io.File)}.
	 */
	@Test
	public void testMagic() throws Exception {
		Magic magic = new Magic(new File("misc/magic/magic"));
		Result result = magic.detect(new File("misc/magic/testfiles/Wildcard.class"));

		// TODO Still implementing...
		assertNotNull(result);
		assertEquals("compiled Java class data, version 50.0 (Java 1.6)", result
				.getDescription());
		assertEquals("application/octet-stream", result.getMime());
	}

	// Numeric values may be preceded by a character indicating the opera-
	// tion to be performed. It may be =, to specify that the value from
	// the file must equal the specified value, <, to specify that the
	// value from the file must be less than the specified value, >, to
	// specify that the value from the file must be greater than the spec-
	// ified value, &, to specify that the value from the file must have
	// set all of the bits that are set in the specified value, ^, to
	// specify that the value from the file must have clear any of the
	// bits that are set in the specified value, or ~, the value specified
	// after is negated before tested. x, to specify that any value will
	// match. If the character is omitted, it is assumed to be =. Opera-
	// tors &, ^, and ~ don’t work with floats and doubles. The operator
	// ! specifies that the line matches if the test does not succeed.
	@Test
	public void operators() throws Exception {
		char[] expecteds = new char[] { '!', '&', '<', '=', '>', '^', '~' };
		char[] operators = new char[] { '!', '&', '<', '=', '>', '^', '~' };
		Arrays.sort(operators);
		System.out.println(Arrays.toString(operators));
		assertArrayEquals(expecteds, operators);
	}

	@Test
	public void testCafeBabe() throws Exception {
		byte[] java = new byte[] { (byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe };
		long l = ByteUtil.toInt(ByteOrder.LITTLE_ENDIAN, java);
		System.out.println(l);
	}

}
