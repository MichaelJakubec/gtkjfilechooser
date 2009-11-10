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

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

/**
 * <p>
 * Detect the file type basing on the magic pattern database.
 * </p>
 * The format of the magic patter file is described in {@link http
 * ://man.he.net/?topic=magic&section=all}. <br />
 * In the most linux system the magic file is in <tt>/usr/share/file/magic</tt>.
 * 
 * @author Costantino Cerbo
 * 
 */
// TODO Still implementing...
public class Magic {
	static final private ByteOrder NATIVE = ByteOrder.nativeOrder();

	/**
	 * The operators for the test condition already sorted.
	 */
	static final private char[] OPERATORS = new char[] { '!', '&', '<', '=', '>', '^',
	'~' };

	private File magicfile;

	private int currentLevel = -1;

	public Magic(File magicfile) {
		this.magicfile = magicfile;
	}

	private void resetInstanceVariables() {
		currentLevel = -1;
	}

	public Result detect(File file) throws IOException {
		Scanner sc0 = null;
		FileChannel channel = null;
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(file);
			long filesize = file.length();
			channel = fstream.getChannel();

			sc0 = new Scanner(magicfile);
			while (sc0.hasNextLine()) {
				String line = sc0.nextLine();
				if (line.startsWith("#") || line.trim().isEmpty()) {
					continue;
				}

				if (line.startsWith("!:")) {
					// other options (mime, strength, etc...)
				} else if (line.startsWith(">")) {
					// subsequent-level magic pattern
					int level = level(line);
					// println(level+"\t"+line);
				} else {
					// top-level magic pattern
					Scanner sc1 = new Scanner(line);

					try {
						int offset = toInt(sc1.next());
						Type type = parseType(sc1.next());
						String test = sc1.next();

						int len = type.lenght;
						if (len == -1) {
							// TODO string, pstring, search, default still to
							// implement.
							continue;
						}

						if (offset + len > filesize) {
							continue;
						}

						//TODO read byte rightly
						ByteBuffer bb = channel.map(MapMode.READ_ONLY, offset, len);

						byte[] b = new byte[bb.limit()];
						bb.position(0);
						bb.get(b);
						for (int i = 0; i < b.length; i++) {
							b[i] = (byte) (b[i] & 0xff);
						}

						if ("0xcafebabe".equals(test)) {
							System.out.println(test);
						}
						Object value = performTest(type, b, test);

						// The test is passed when the value is different from
						// null
						if (value != null) {
							// get the remaining part of the line
							sc1.useDelimiter("\\Z");
							String message = sc1.hasNext() ? sc1.next().trim() : null;
							printf("Message: ");
							printf(message + "\n", value);
						}
					} catch (Exception e) {
						e.printStackTrace(); // TODO remove
						throw new IllegalStateException(
								"Line that caused the exception:\n" + line, e);
					}

				}

			}

			return null;
		} finally {
			if (fstream != null) {
				fstream.close();
			}

			if (channel != null) {
				channel.close();
			}

			if (sc0 != null) {
				sc0.close();
			}

			resetInstanceVariables();
		}
	}

	private Object performTest(Type type, byte[] b, String test) {
		Object value = null;
		// The special test x always evaluates to true
		boolean passed = "x".equals(test);
		if (type.isIntegerNumber()) {
			long actual = toIntegerNumber(type.order, b);
			if (type.and != null) {
				actual = actual & type.and;
			}
			value = actual;
			if (!passed) {
				if (test.length() > 1
						&& Arrays.binarySearch(OPERATORS, test.charAt(0)) > 0) {
					long expected = toLong(test.substring(1));
					char operator = test.charAt(0);
					switch (operator) {
					case '=':
						passed = (actual == expected);
						break;
					case '!':
						passed = (actual != expected);
						break;
					case '<':
						passed = (actual < expected);
						break;
					case '>':
						passed = (actual > expected);
						break;
					case '&':
						// The actual value must have set all of the bits
						// that are set in the specified value.
						// TODO
						break;
					case '^':
						// The actual value must have clear any of the
						// bits that are set in the specified value.
						// TODO
						break;
					case '~':
						// the value specified after is negated before tested
						// (?)
						// TODO
						break;
					}
				} else {
					// No operator
					long expected = toLong(test);
					passed = (actual == expected);
				}
			}
		} else if (type.isDecimalNumber()) {
			double actual = toDecimalNumber(type.order, b);
			value = actual;
			if (!passed) {
				if (test.length() > 1
						&& Arrays.binarySearch(OPERATORS, test.charAt(0)) > 0) {
					double expected = toDouble(test.substring(1));
					char operator = test.charAt(0);
					switch (operator) {
					case '=':
						passed = (actual == expected);
						break;
					case '!':
						passed = (actual != expected);
						break;
					case '<':
						passed = (actual < expected);
						break;
					case '>':
						passed = (actual > expected);
						break;
					}
				}
			} else {
				// No operator
				double expected = toDouble(test);
				passed = (actual == expected);
			}
		} else if (type.isDate()) {
			// TODO
		} else if (type.isString()) {
			// TODO
		}

		return passed ? value : null;
	}

	/**
	 * Return the byte array as integer number.
	 */
	private long toIntegerNumber(ByteOrder order, byte[] b) {
		int len = b.length;

		switch (len) {
		case 1:
			return b[0];
		case 2:
			return ByteUtil.toShort(order, b);
		case 4:
			return ByteUtil.toInt(order, b);
		case 8:
			return ByteUtil.toLong(order, b);

		default:
			throw new IllegalArgumentException("Invalid length: " + len);
		}
	}

	/**
	 * Return the byte array as decimal number.
	 */
	private double toDecimalNumber(ByteOrder order, byte[] b) {
		int len = b.length;

		switch (len) {
		case 4:
			return ByteUtil.toFloat(order, b);
		case 8:
			return ByteUtil.toDouble(order, b);

		default:
			throw new IllegalArgumentException("Invalid length: " + len);
		}
	}

	/**
	 * Return the byte array as date.
	 */
	private Date toDate(ByteOrder order, byte[] b) {
		int len = b.length;

		switch (len) {
		case 4:
			return new Date(ByteUtil.toInt(order, b) * 1000);
		case 8:
			return new Date(ByteUtil.toLong(order, b) * 1000);

		default:
			throw new IllegalArgumentException("Invalid length: " + len);
		}
	}

	/**
	 * Returns the byte lenght for the given type.
	 */
	private Type parseType(String rawtype) {
		/**
		 * The numeric types may optionally be followed by & and a numeric
		 * value, to specify that the value is to be AND’ed with the numeric
		 * value before any comparisons are done.
		 */
		String type = null;
		Long and = null;
		boolean unsigned = false;
		if (rawtype.indexOf('&') != -1) {
			type = rawtype.substring(0, rawtype.indexOf('&'));
			// +3 to consider 0x
			and = Long.parseLong(rawtype.substring(rawtype.indexOf('&') + 3), 16);
		} else if (rawtype.startsWith("u")) {
			// Prepending a u to the typeindicates that 
			// ordered comparisons should be unsigned.
			unsigned = true;
			type = rawtype.substring(1);
		} else {
			type = rawtype;
		}

		if ("byte".equals(type)) {
			return new Type("byte", 1, NATIVE, and, unsigned);
		} else if ("short".equals(type)) {
			return new Type("short", 2, NATIVE, and, unsigned);
		} else if ("long".equals(type)) {
			return new Type("long", 4, NATIVE, and, unsigned);
		} else if ("quad".equals(type)) {
			return new Type("quad", 8, NATIVE, and, unsigned);
		} else if ("float".equals(type)) {
			return new Type("float", 4, NATIVE, and, unsigned);
		} else if ("double".equals(type)) {
			return new Type("double", 8, NATIVE, and, unsigned);
		} else if ("date".equals(type)) {
			return new Type("date", 4, NATIVE, and, unsigned);
		} else if ("beshort".equals(type)) {
			return new Type("beshort", 2, BIG_ENDIAN, and, unsigned);
		} else if ("belong".equals(type)) {
			return new Type("belong", 4, BIG_ENDIAN, and, unsigned);
		} else if ("bequad".equals(type)) {
			return new Type("bequad", 8, BIG_ENDIAN, and, unsigned);
		} else if ("befloat".equals(type)) {
			return new Type("befloat", 4, BIG_ENDIAN, and, unsigned);
		} else if ("bedouble".equals(type)) {
			return new Type("bedouble", 8, BIG_ENDIAN, and, unsigned);
		} else if ("bedate".equals(type)) {
			return new Type("bedate", 4, BIG_ENDIAN, and, unsigned);
		} else if ("beqdate".equals(type)) {
			return new Type("beqdate", 8, BIG_ENDIAN, and, unsigned);
		} else if ("beldate".equals(type)) {
			return new Type("beldate", 4, BIG_ENDIAN, and, unsigned);
		} else if ("beqldate".equals(type)) {
			return new Type("beqldate", 8, BIG_ENDIAN, and, unsigned);
		} else if ("bestring16".equals(type)) {
			return new Type("bestring16", 2, BIG_ENDIAN, and, unsigned);
		} else if ("leshort".equals(type)) {
			return new Type("leshort", 2, LITTLE_ENDIAN, and, unsigned);
		} else if ("lelong".equals(type)) {
			return new Type("lelong", 4, LITTLE_ENDIAN, and, unsigned);
		} else if ("lequad".equals(type)) {
			return new Type("lequad", 8, LITTLE_ENDIAN, and, unsigned);
		} else if ("lefloat".equals(type)) {
			return new Type("lefloat", 4, LITTLE_ENDIAN, and, unsigned);
		} else if ("ledouble".equals(type)) {
			return new Type("ledouble", 8, LITTLE_ENDIAN, and, unsigned);
		} else if ("ledate".equals(type)) {
			return new Type("ledate", 4, LITTLE_ENDIAN, and, unsigned);
		} else if ("leqdate".equals(type)) {
			return new Type("leqdate", 8, LITTLE_ENDIAN, and, unsigned);
		} else if ("leldate".equals(type)) {
			return new Type("leldate", 8, LITTLE_ENDIAN, and, unsigned);
		} else if ("leqldate".equals(type)) {
			return new Type("leqldate", 8, LITTLE_ENDIAN, and, unsigned);
		} else if ("lestring16".equals(type)) {
			return new Type("lestring16", 2, LITTLE_ENDIAN, and, unsigned);
		} else if (type.startsWith("string")) {
			// TODO type string
			return new Type("", -1, null, and, unsigned);
		} else if (type.startsWith("pstring")) {
			// TODO type pstring
			return new Type("", -1, null, and, unsigned);
		} else if (type.startsWith("search")) {
			// TODO type search
			return new Type("", -1, null, and, unsigned);
		} else if ("default".equals(type)) {
			return new Type("", -1, null, and, unsigned);
		}

		throw new IllegalArgumentException("Type '" + type + "' is unknown.");
	}

	/**
	 * Convert to int from decimal, octal and hexdecimal format.
	 */
	private int toInt(String value) {
		if (value.startsWith("0x")) {
			// Hexdecimal
			//FIXME fix the parse
			return Integer.parseInt(value.substring(2), 16);
		} else if (value.startsWith("0") && (value.length() > 1)) {
			// Octal
			return Integer.parseInt(value.substring(1), 8);
		}

		// Decimal
		return Integer.parseInt(value);
	}

	/**
	 * Convert to long from decimal, octal and hexdecimal format.
	 */
	private long toLong(String rawvalue) {
		String value = rawvalue;
		if (rawvalue.toUpperCase().endsWith("L")) {
			value =  rawvalue.substring(0, rawvalue.length() - 1);
		}

		if (value.startsWith("0x")) {
			//FIXME fix the parse
			// Hexdecimal
			return Long.parseLong(value.substring(2), 16);
		} else if (value.startsWith("0") && (value.length() > 1)) {
			// Octal
			return Long.parseLong(value.substring(1), 8);
		}

		// Decimal
		return Long.parseLong(value);
	}

	/**
	 * Convert to double from decimal, octal and hexdecimal format.
	 */
	private double toDouble(String value) {
		return Double.parseDouble(value);
	}

	private void println(String str) {
		System.out.println(str);
	}

	private void printf(String format, Object... args) {
		System.out.printf(format, args);
	}

	private int level(String line) {
		int level = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '>') {
				level++;
			} else {
				break;
			}
		}
		return level;
	}

	/**
	 * Inner bean containing the result of the detection: mime type and full
	 * description.
	 * 
	 */
	public class Result {
		private String mime;
		private String description;

		public String getMime() {
			return mime;
		}

		public String getDescription() {
			return description;
		}
	}

	private class Type {
		private final String name;
		private final int lenght;
		private final ByteOrder order;
		private final Long and;
		private final boolean unsigned;

		Type(String name, int lenght, ByteOrder byteorder, Long and, boolean unsigned) {
			this.name = name;
			this.lenght = lenght;
			this.order = byteorder;
			this.and = and;
			this.unsigned = unsigned;
		}

		boolean isDate() {
			return name.indexOf("date") != -1;
		}

		boolean isIntegerNumber() {
			return name.indexOf("short") != -1 || name.indexOf("long") != -1
			|| name.indexOf("quad") != -1;
		}

		boolean isDecimalNumber() {
			return name.indexOf("float") != -1 || name.indexOf("double") != -1;
		}

		boolean isString() {
			return name.indexOf("string") != -1;
		}
	}
}
