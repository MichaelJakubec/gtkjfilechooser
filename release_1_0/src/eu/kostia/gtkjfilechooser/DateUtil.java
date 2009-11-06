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

import static eu.kostia.gtkjfilechooser.I18N._;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * Format a date in a more human readable way, according the following set
	 * of rules:
	 * 
	 * <ul>
	 * <li><i>Today:</i> shows just the time (HH:MM)</li>
	 * <li><i>Yesterday:</i> shows "Yesterday at HH:MM"</li>
	 * <li><i>Days from last week: </i> show the locale day of week</li>
	 * <li><i>Any other date: </i> format is using {@link DateFormat#getDateInstance()}</li>
	 * </ul>
	 * 
	 * @param date The {@link Date} to format.
	 * @return The "prettified" date.
	 */
	public static String toPrettyFormat(Date date) {
		return toPrettyFormat(date, new Date());
	}

	/**
	 * Package visibility for testing
	 */
	static String toPrettyFormat(Date d, Date today) {
		long days_diff = toJulianDayNumber(today) - toJulianDayNumber(d);

		if (days_diff == 0) {
			// Today: show just the time (HH:MM)
			return String.format("%tR", d);
		}

		if (days_diff == 1) {
			// Yesterday
			String mgsstr = _("Yesterday at %H:%M");
			return new Strftime(mgsstr).format(d);
		}

		if (days_diff > 1 && days_diff < 7) {
			// Days from last week
			return String.format("%tA", d);
		}

		// Any other date
		return DateFormat.getDateInstance(DateFormat.SHORT).format(d);
	}

	/**
	 * The given date as Julian day number. The Julian day number is the amount
	 * of day since 1/1/1. Useful to compute days difference.
	 * 
	 * @param date
	 *            The date to convert.
	 * @return The Julian day number.
	 */
	public static long toJulianDayNumber(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int Y = cal.get(Calendar.YEAR);
		int M = cal.get(Calendar.MONTH) + 1; // jan=1, feb=2,...
		int D = cal.get(Calendar.DAY_OF_MONTH);

		return (1461 * (Y + 4800 + (M - 14) / 12)) / 4
		+ (367 * (M - 2 - 12 * ((M - 14) / 12))) / 12
		- (3 * ((Y + 4900 + (M - 14) / 12) / 100)) / 4 + D - 32075;
	}
}
