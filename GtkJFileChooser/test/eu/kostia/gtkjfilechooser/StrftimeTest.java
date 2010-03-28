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
