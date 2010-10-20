/*******************************************************************************
 * Copyright (c) 2010 Costantino Cerbo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Costantino Cerbo - initial API and implementation
 ******************************************************************************/
package com.google.code.gtkjfilechooser.xbel;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.google.code.gtkjfilechooser.xbel.ISO8601DateFormat;

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
