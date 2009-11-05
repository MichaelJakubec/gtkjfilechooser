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
package eu.kostia.gtkjfilechooser.xbel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Parse and format ISO 8601 dates.
 * 
 * @see http://www.w3.org/TR/NOTE-datetime
 * @author c.cerbo
 *
 */
public class ISO8601DateFormat {
	private DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	/**
	 * Format a date in ISO 8601 (for example "1994-11-05T08:15:30-05:00" or
	 * "2009-09-06T16:59:39Z").
	 * 
	 * @param date
	 *            The date to format.
	 * @return the ISO 8601 format date.
	 */
	public String format(Date date) {
		String d = fmt.format(date); 
		return d.substring(0, 22) + ":" + d.substring(22);
	}

	/**
	 * Parse a date in ISO 8601 format (for example "1994-11-05T08:15:30-05:00"
	 * or "2009-09-06T16:59:39Z").
	 * 
	 * @param source
	 *            A <code>String</code> whose beginning should be parsed.
	 * @return A <code>Date</code> parsed from the string.
	 * @exception ParseException
	 *                if the beginning of the specified string cannot be parsed.
	 */
	public Date parse(String source) throws ParseException {
		return fmt.parse(preprocess(source));
	}

	private String preprocess(String s) throws ParseException {

		if (s.charAt(s.length() - 1) == 'Z') {
			// handle a date with the special UTC designator ("Z"). 

			// remove the Z and concat +0000
			s = s.substring(0, s.length() - 1) + "+0000";
		} else {
			// handle the case with time zone designator (+hh:mm or -hh:mm)

			s = s.substring(0, s.length() - 3) + s.substring(s.length() - 2);
		}

		return s;
	}

}
