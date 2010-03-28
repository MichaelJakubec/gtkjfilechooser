/*******************************************************************************
 * Copyright 2010 Costantino Cerbo.  All Rights Reserved.
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
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 *******************************************************************************/
package eu.kostia.gtkjfilechooser.xbel;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class ISO8601DateFormatTest {

	@Test
	public void testParse0() throws Exception {
		String source = "1994-11-05T08:15:30-05:00";
		Date date = new ISO8601DateFormat().parse(source);
		System.out.println(date.toString());
		assertEquals(new Date(784041330000L), date);
	}

	@Test
	public void testParse1() throws Exception {
		String source = "2009-09-06T16:59:39Z";
		Date date = new ISO8601DateFormat().parse(source);
		System.out.println(date.toString());
		assertEquals(new Date(1252256379000L), date);
	}



	@Test
	public void testFormat() throws Exception {
		Date date = new GregorianCalendar(2009, Calendar.OCTOBER, 5, 19, 30, 45).getTime();
		assertEquals("2009-10-05T19:30:45+02:00", new ISO8601DateFormat().format(date));
	}

}
