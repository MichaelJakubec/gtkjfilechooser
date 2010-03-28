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
package eu.kostia.gtkjfilechooser;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Test;

public class StrftimeTest {
	Date date = new GregorianCalendar(2009, Calendar.OCTOBER, 9, 8, 15, 10).getTime();

	@Test
	public void testFormat() {
		String fmt0 = "Yesterday at %H:%M";
		String res0 = new Strftime(fmt0).format(date);
		System.out.println(res0);

		String fmt0a = "Ieri alle %k.%M";
		String res0a = new Strftime(fmt0a).format(date);
		System.out.println(res0a);

		String fmt1 = "%A";
		String res1 = new Strftime(fmt1).format(date);
		System.out.println(res1);

		String fmt2 = "%x";
		String res2 = new Strftime(fmt2).format(date);
		System.out.println(res2);
	}

	@Test
	public void test_c_it_IT() {
		Locale.setDefault(Locale.ITALY);
		String fmt0 = "%c";
		String res0 = new Strftime(fmt0).format(date);
		System.out.println(res0);
		assertEquals("ven 9 ott 2009 08:15:10 CEST", res0);
	}

	@Test
	public void test_c_en_US() {
		Locale.setDefault(Locale.US);
		String fmt0 = "%c";
		String res0 = new Strftime(fmt0).format(date);
		System.out.println(res0);
		assertEquals("Fri 9 Oct 2009 08:15:10 CEST", res0);
	}

	@Test
	public void test_c_de_DE() {
		Locale.setDefault(Locale.GERMANY);
		String fmt0 = "%c";
		String res0 = new Strftime(fmt0).format(date);
		System.out.println(res0);
		assertEquals("Fr 9 Okt 2009 08:15:10 MESZ", res0);
	}

	@Test
	public void testJdkFormatter0() {
		String fmt = "Duke's Birthday: %1$tm %1$te,%1$tY";
		Date now = new Date();
		String ret = strftime(fmt, now);
		System.out.println(ret);
	}

	@Test
	public void testJdkFormatter1() {
		String fmt = "Duke's Birthday: %1$tC";
		String ret = strftime(fmt, date);
		System.out.println(ret);
	}

	private String strftime(String fmt, Date date) {
		String ret = new Formatter().format(fmt, date).toString();
		return ret;
	}
}
