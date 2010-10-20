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
package com.google.code.gtkjfilechooser;

import static java.util.Calendar.JANUARY;
import static java.util.Calendar.OCTOBER;
import static org.junit.Assert.assertEquals;

import java.security.AccessController;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.code.gtkjfilechooser.DateUtil;
import com.google.code.gtkjfilechooser.I18N;

import sun.security.action.GetPropertyAction;

public class DateUtilTest {
	static private final Date TODAY = new GregorianCalendar(2009, OCTOBER, 20, 00, 01, 30)
	.getTime();

	@BeforeClass
	static public void beforeClass() {
		I18N.init(Locale.ENGLISH);
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
	@Ignore
	public void testToPrettyFormatSixDaysAgo() throws Exception {
		Date sixDaysAgo = new GregorianCalendar(2009, OCTOBER, 14, 23, 30, 55).getTime();
		assertEquals("Wednesday", DateUtil.toPrettyFormat(sixDaysAgo, TODAY));

	}

	@Test
	@Ignore
	public void testToPrettyFormatSevenDaysAgo() throws Exception {
		Date sevenDaysAgo = new GregorianCalendar(2009, OCTOBER, 13, 23, 30).getTime();
		assertEquals("10/13/09", DateUtil.toPrettyFormat(sevenDaysAgo, TODAY));
	}

}
