/*******************************************************************************
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
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 *******************************************************************************/
package eu.kostia.gtkjfilechooser;

import static java.util.Calendar.JANUARY;
import static java.util.Calendar.OCTOBER;
import static org.junit.Assert.assertEquals;

import java.security.AccessController;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sun.security.action.GetPropertyAction;

public class DateUtilTest {
	static private final Date TODAY = new GregorianCalendar(2009, OCTOBER, 20, 00, 01, 30)
	.getTime();

	@BeforeClass
	static public void beforeClass() {
		Locale.setDefault(Locale.ENGLISH);
	}

	@AfterClass
	static public void resetLocale() {
		String language = AccessController.doPrivileged(new GetPropertyAction("user.language"));
		String country = AccessController.doPrivileged(new GetPropertyAction("user.country"));

		Locale.setDefault(new Locale(language, country));
	}

	@Test
	public void testToJulianDayNumber0() throws Exception {
		Date date = new GregorianCalendar(2009, OCTOBER, 20).getTime();
		assertEquals(2455125L, DateUtil.toJulianDayNumber(date));
	}

	@Test
	public void testToJulianDayNumber1() throws Exception {
		Date date = new GregorianCalendar(2000, JANUARY, 1, 12, 00).getTime();
		assertEquals(2451545L, DateUtil.toJulianDayNumber(date));
	}

	@Test
	public void testToPrettyFormatToday() throws Exception {
		assertEquals("00:01", DateUtil.toPrettyFormat(TODAY, TODAY));
	}

	@Test
	public void testToPrettyFormatYesterday() throws Exception {		
		Date yesterday = new GregorianCalendar(2009, OCTOBER, 19, 23, 59, 59).getTime();
		assertEquals("Yesterday at 23:59", DateUtil.toPrettyFormat(yesterday, TODAY));

		String language = AccessController.doPrivileged(new GetPropertyAction("user.language"));
		String country = AccessController.doPrivileged(new GetPropertyAction("user.country"));
		Locale.setDefault(new Locale(language, country));
	}

	@Test
	public void testToPrettyFormatSixDaysAgo() throws Exception {
		Date sixDaysAgo = new GregorianCalendar(2009, OCTOBER, 14, 23, 30, 55).getTime();
		assertEquals("Wednesday", DateUtil.toPrettyFormat(sixDaysAgo, TODAY));

	}

	@Test
	public void testToPrettyFormatSevenDaysAgo() throws Exception {
		Date sevenDaysAgo = new GregorianCalendar(2009, OCTOBER, 13, 23, 30).getTime();
		assertEquals("10/13/09", DateUtil.toPrettyFormat(sevenDaysAgo, TODAY));
	}

}
