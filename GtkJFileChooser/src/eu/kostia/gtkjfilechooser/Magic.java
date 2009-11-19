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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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

	private transient File magicfile;

	private transient int currentLevel = -1;
	/**
	 * The position where the processor currently is.
	 */
	private transient int currentPosition = 0;
	private transient FileChannel channel;
	private transient StringBuilder fileDescription;
	private transient Result result = null;
	private transient boolean lastTestSuccessful = false;

	public Magic(File magicfile) {
		this.magicfile = magicfile;
	}

	private void resetInstanceVariables() {
		this.currentLevel = -1;
		this.currentPosition = 0;
		this.channel = null;
		this.fileDescription = null;
		this.result = null;
		this.lastTestSuccessful = false;
	}

	public Result detect(File file) throws IOException {
		Scanner sc0 = null;
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(file);
			channel = fstream.getChannel();

			if (channel.size() == 0L) {
				result = new Result();
				result.description = "empty";
				result.mime = "application/x-empty";
				return result;
			}

			sc0 = new Scanner(new BufferedInputStream(new FileInputStream(magicfile)));
			while (sc0.hasNextLine()) {
				String line = sc0.nextLine();
				//TODO remove -----------------------------------------------------------
				if (line.startsWith("# Type: Google KML, formerly Keyhole Markup Language")) {
					System.out.println("BP");
				}
				//-----------------------------------------------------------------------
				if (line.startsWith("#") || line.trim().isEmpty()) {
					continue;
				}

				if (line.startsWith("!:")) {
					if (line.startsWith("!:mime") && currentLevel >= 0 && lastTestSuccessful) {
						/**
						 * The !:mime annotation must be the next non-blank or
						 * comment line after the magic line that identifies the
						 * file type
						 */
						String s = removeInlineComment(line);
						result.mime = s.substring("!:mime".length()).trim();											
					}
					// we deliberately choose to not implement 
					// the other options (strength, apple, etc...)
				} else if (line.startsWith(">")) {
					// subsequent-level magic pattern
					int level = level(line);
					//TODO implement this strategy
					/**
					 * The number of > on the line indicates the level of the
					 * test; a line with no > at the beginning is considered to
					 * be at level 0. Tests are arranged in a tree-like
					 * hierarchy: If a the test on a line at level n succeeds,
					 * all following tests at level n+1 are per- formed, and the
					 * messages printed if the tests succeed, until a line with
					 * level n (or less) appears.
					 */
					if (currentLevel == level || currentLevel == (level - 1)) {
						processLine(line, level);
					}
				} else {
					// top-level magic pattern

					if (currentLevel >= 0) {
						// top-level already reached.

						if (fileDescription != null && !fileDescription.toString().trim().isEmpty()) {
							// Stop the loop if the file is described...
							break;
						} else {
							// ...else reset current level and result and go on.
							currentLevel = -1;
							result = null;
						}						
					}

					processLine(line, 0);
				}
			}

			if (fileDescription != null && fileDescription.length() > 0) {
				result.description = fileDescription.toString().trim().replace("\n", "");
			}
			return result;
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

	private String removeInlineComment(String line) {
		int end = line.indexOf('#') != -1 ? line.indexOf('#') : line.length();
		return line.substring(0, end).trim();
	}

	private void processLine(String line, int level) throws IOException {
		// Workaround to include spaces (they are escaped) (Part A)
		line = line.replace("\\ ", String.valueOf((char) 0x04));

		try {
			Scanner sc1 = new Scanner(line.substring(level));

			int offset = toInt(sc1.next());
			if (offset < 0) {
				return;
			}
			Type type = parseType(sc1.next());
			String test = sc1.next().replace(String.valueOf((char) 0x04), " ");
			test = convertString(test);

			// TODO remove --------------------------------------
			if (test.indexOf("<?xml version=") != -1) {
				printf(line + "\n");
			}

			if (line.indexOf(">>23	search/400	\\<svg") != -1) {
				println(line + "\n");
			}

			// System.out.println(line);
			if (line.indexOf(">>>>(150.L+50)	beshort	0x9000") != -1) {
				println(line + "\n");
			}
			// --------------------------------------------------

			Object value = null;
			if (type.isString()) {
				value = performStringTest(offset, type, test);
			} else if (type.isSearch()) {
				value = performSearchTest(offset, type, test);
			} else if (type.isRegex()) {
				value = performRegexTest(offset, type, test);
			} else {
				// for integer, decimal and date (fixed byte-length )
				int len = type.lenght;
				if (len > 0 && (offset + len) > channel.size()) {
					// File too small
					return;
				}
				value = performByteTest(type, readByte(offset, len), test);
			}

			// The test is passed when the value
			// is different from null.
			if (value != null) {
				lastTestSuccessful = true;
				if (currentLevel == -1) {
					result = new Result();
					fileDescription = new StringBuilder();
					// 0 is the top-level
					currentLevel = 0;
				}

				// when the test passed, the currentLevel to its level.
				currentLevel = level;

				// get the remaining part of the line
				sc1.useDelimiter("\\Z");
				String message = sc1.hasNext() ? sc1.next().trim() : null;
				if (message != null) {
					// handle backspace (\b)
					if (message.startsWith("\\b")) {
						fileDescription.deleteCharAt(fileDescription.length() - 1);
						message = message.substring("\\b".length());
					}
					fileDescription.append(String.format(message, value));
					fileDescription.append(" ");
				}
				printf("Message: %s\n", message);
			} else {
				lastTestSuccessful = false;
			}
		} catch (Exception e) {
			e.printStackTrace(); // TODO remove
			throw new IllegalStateException("Line that caused the exception:\n" + line, e);
		}
	}

	/**
	 * Returns an array of signed bytes
	 */
	// TODO caching this method improve the performance?
	byte[] readByte(int offset, int len) throws IOException {
		try {
			ByteBuffer bb = channel.map(MapMode.READ_ONLY, offset, len);

			byte[] b = new byte[bb.limit()];
			bb.position(0);
			bb.get(b);

			//set the current offset position
			currentPosition += b.length;

			return b;
		} catch (Exception e) {
			throw new IllegalArgumentException("offset: " + offset + ", lenght: " + len + ", file size: " + channel.size(), e);
		}
	}

	/**
	 * Returns an array of signed bytes
	 */
	int[] readUnsignedByte(int offset, int len) throws IOException {
		byte[] b = readByte(offset, len);
		int[] bu = new int[len];
		for (int i = 0; i < len; i++) {
			bu[i] = b[i] & 0xff;
		}

		return bu;
	}

	/**
	 * For string values, the string from the file must match the spec- ified
	 * string. The operators =, < and > (but not &) can be applied to strings.
	 * The length used for matching is that of the string argument in the magic
	 * file. This means that a line can match any non-empty string (usually used
	 * to then print the string), with >\0 (because all non-empty strings are
	 * greater than the empty string).
	 */
	private Object performStringTest(int offset, Type type, String test)
	throws IOException {
		String str = readString(offset);

		if ("x".equals(test)) {
			currentPosition += str.length();
			return str;
		}

		// > stands for >\0
		if (">".equals(test)) {
			if(!str.isEmpty()) {
				currentPosition += str.length();
				return str;
			} else {
				return null;
			}
		}

		if (test.isEmpty()) {
			return str.isEmpty();
		}

		/**
		 * The string type specification can be optionally followed by /[Bbc]*.
		 * The “B” flag compacts whitespace in the target, which must contain
		 * at least one whitespace character. If the magic has n consecutive
		 * blanks, the target needs at least n consecutive blanks to match. The
		 * “b” flag treats every blank in the target as an optional blank.
		 * Finally the “c” flag, specifies case insensitive matching:
		 * lowercase characters in the magic match both lower and upper case
		 * characters in the target, whereas upper case characters in the magic
		 * only match uppercase characters in the target.
		 */
		// we don't handle the flag B because it is like the standard case

		boolean flag_b = false;
		boolean flag_c = false;
		if (type.name.indexOf('/') != -1) {
			String flags = type.name.substring(type.name.indexOf('/'));
			StringFlags stringFlags = parseStringFlags(flags);
			flag_b = stringFlags.flag_b;
			flag_c = stringFlags.flag_c;
		}

		switch (test.charAt(0)) {			
		case '>':
			int len = toInt(test.substring(1));
			if (str.length() > len) {
				currentPosition += str.length();
				return str;
			} else {
				return null;
			}
		case '<':				
			try {					
				len = toInt(test.substring(1));
				if (str.length() < len) {
					currentPosition += str.length();
					return str;
				} else {
					return null;
				}
			} catch (NumberFormatException e) {
				//go to the next test
			}
		case '=':
			test = test.substring(1);
		default:
			if (test.startsWith("\\<")) {
				test = test.substring(1);
			}

			if (flag_c) {
				// case insensitive matching
				str = str.toLowerCase();
				test = test.toLowerCase();
			}

			int adjust = 0;
			if (flag_b) {
				// blanks are optional: remove them
				adjust = str.length();
				str = str.replaceAll(" ", "");
				adjust -= str.length();
				test = test.replaceAll(" ", "");
			}

			if (str.startsWith(test)) {
				currentPosition += str.length() + adjust;
				return str;
			} else {
				return null;
			}
		}
	}

	private Object performRegexTest(int offset, Type type, String regex) throws IOException {
		String str = readString(offset);

		StringFlags stringFlags = new StringFlags();
		if (type.name.indexOf('/') != -1) {
			String flags = type.name.substring(type.name.indexOf('/'));
			stringFlags = parseStringFlags(flags);
		}

		if (stringFlags.flag_c) {
			// case insensitive matching
			str = str.toLowerCase();
			regex = regex.toLowerCase();
		}

		Matcher matcher = Pattern.compile(regex).matcher(str);

		if (matcher.find()) {
			currentPosition = offset + (stringFlags.flag_s ? matcher.start() : matcher.end());
			return str;
		} else {
			return null;
		}

	}

	private Object performSearchTest(int offset, Type type, String test) throws IOException {
		String str = readString(offset);

		if ("x".equals(test)) {
			return str;
		}

		// > stands for >\0
		if (">".equals(test)) {
			return !str.isEmpty() ? str : null;
		}

		if (test.isEmpty()) {
			return str.isEmpty();
		}


		int range = -1;
		String typeName = type.name;
		Scanner sc = new Scanner(typeName).useDelimiter(Pattern.quote("/"));
		// The first token is always the string 'search'
		sc.next();

		boolean[] flagsArray = new boolean[3];
		Arrays.fill(flagsArray, false);
		// we don't handle the flag B because it is like the standard case
		boolean flag_b = flagsArray[1];
		boolean flag_c = flagsArray[2];

		while (sc.hasNext()) {
			String next = sc.next();
			if (areStringFlags(next)) {
				StringFlags stringFlags = parseStringFlags(next);				
				flag_b = stringFlags.flag_b;
				flag_c = stringFlags.flag_c;
			} else {
				range = toInt(next);
			}		
		}

		// If the string length is less than the read, then read addition chars.
		if (range != -1 && str.length() < range) {
			int len = range + test.length();
			if (offset < channel.size()) {
				if (offset + len >= channel.size()) {
					len = (int) (channel.size() - offset - 1);
				}
				str = readString(offset, len);
			}
		}

		switch (test.charAt(0)) {			
		case '>':
			int len = toInt(test.substring(1));
			if (str.length() > len) {
				currentPosition = offset + str.length();
				return str;
			} else {
				return null;
			}
		case '<':				
			try {					
				len = toInt(test.substring(1));
				if (str.length() < len) {
					currentPosition = offset + str.length();
					return str;
				} else {
					return null;
				}
			} catch (NumberFormatException e) {
				//go to the next test
			}
		case '=':
			test = test.substring(1);
		default:
			if (test.startsWith("\\<")) {
				test = test.substring(1);
			}

			if (flag_c) {
				// case insensitive matching
				str = str.toLowerCase();
				test = test.toLowerCase();
			}

			if (flag_b) {
				// blanks are optional: remove them
				str = str.replaceAll(" ", "");
				test = test.replaceAll(" ", "");
			}
		}

		if (str.indexOf(test) >= 0) {
			currentPosition = offset + str.indexOf(test) + test.length();
			return str;
		} else {
			return null;
		}
	}

	private boolean areStringFlags(String flags) {
		if (flags.indexOf('B') != -1) {
			return true;
		}

		if (flags.indexOf('b') != -1) {
			return true;
		}

		if (flags.indexOf('c') != -1) {
			return true;
		}

		return false;		
	}

	class StringFlags {
		boolean flag_B = false;
		boolean flag_b = false;
		boolean flag_c = false;
		boolean flag_s = false;
	}
	private StringFlags parseStringFlags(String flags) {
		StringFlags stringFlags = new StringFlags();

		for (int i = 0; i < flags.length(); i++) {
			switch (flags.charAt(i)) {
			case 'B':
				stringFlags.flag_B = true;
				break;
			case 'b':
				stringFlags.flag_b = true;
				break;
			case 'c':
				stringFlags.flag_c = true;
				break;
			}
		}

		return stringFlags;
	}

	/**
	 * Beginning from the given offset, it read a string (as in C, a string is
	 * sequence of chars terminated with 0). For performance the max string
	 * length is 255.
	 * 
	 * In this case the {@code #currentPosition} is set in {@code #performStringTest(int, Type, String)}.
	 * Normally, it's set in {@code #readByte(int, int)}.
	 */
	private String readString(int offset) throws IOException {
		// usually the string used for description aren't more than 255 chars.
		int k = 255;
		int len = (offset + k) <= channel.size() ? k : (int) channel.size() - offset;
		if (len <= 0) {
			return "";
		}
		int[] bu = readUnsignedByte(offset, len);
		int n = 0;
		for (n = 0; n < bu.length; n++) {
			if (bu[n] == 0) {
				// like in C the strings are 0 terminated.
				break;
			}
		}
		bu = Arrays.copyOf(bu, n);



		return new String(bu, 0, bu.length);
	}

	private String readString(int offset, int len) throws IOException {
		int[] bu = readUnsignedByte(offset, len);
		return new String(bu, 0, bu.length);
	}

	/**
	 * When the byte lenght is fixed...
	 */
	private Object performByteTest(Type type, byte[] b, String test) {
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
						&& Arrays.binarySearch(OPERATORS, test.charAt(0)) >= 0) {
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
						passed = and(actual, expected);
						break;
					case '^':
						passed = xor(actual, expected);
						break;
					case '~':
						passed = not(actual, expected);
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
			value = toDate(type.order, b);
			// for dates the test is always passed. They are used only for
			// description.
			passed = true;
		}

		return passed ? value : null;
	}

	/**
	 * Return true if the actual value must have set all of the bits that are
	 * set in the expected value.
	 */
	private boolean and(long actual, long expected) {
		String a = ByteUtil.toBinaryString(actual);
		String e = ByteUtil.toBinaryString(expected);
		for (int i = 0; i < e.length(); i++) {
			if (e.charAt(i) == '1') {
				if (a.charAt(i) != '1') {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * The actual value must have clear any of the bits that are set in the
	 * specified value.
	 */
	private boolean xor(long actual, long expected) {
		String a = ByteUtil.toBinaryString(actual);
		String e = ByteUtil.toBinaryString(expected);
		for (int i = 0; i < e.length(); i++) {
			int x = (e.charAt(i) == '1') ? 1 : 0;
			int y = (a.charAt(i) == '1') ? 1 : 0;
			if ((x ^ y) != 1) {
				return false;
			}
		}

		return true;
	}

	/**
	 * The value specified after is negated before tested
	 */
	private boolean not(long actual, long expected) {
		String a = ByteUtil.toBinaryString(actual);
		String e = ByteUtil.toBinaryString(expected);

		// reverse the bits of the expected value
		char[] r = new char[e.length()];
		for (int i = 0; i < r.length; i++) {
			r[i] = e.charAt(i) == '0' ? '1' : '0';

		}

		String er = String.valueOf(r);
		return a.equals(er);
	}

	/**
	 * Return the byte array as unsigned integer number.
	 */
	private long toIntegerNumber(ByteOrder order, byte[] b) {
		int len = b.length;

		switch (len) {
		case 1:
			return b[0];
		case 2:
			return ByteUtil.toUnsigned(ByteUtil.toShort(order, b));
		case 4:
			return ByteUtil.toUnsigned(ByteUtil.toInt(order, b));
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
			return new Date(ByteUtil.toInt(order, b) * 1000L);
		case 8:
			return new Date(ByteUtil.toLong(order, b) * 1000L);

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
		if (rawtype.startsWith("u")) {
			// Prepending a u to the typeindicates that
			// ordered comparisons should be unsigned.
			unsigned = true;
			rawtype = rawtype.substring(1);
		}

		if (rawtype.indexOf('&') != -1) {
			type = rawtype.substring(0, rawtype.indexOf('&'));
			// +3 to consider 0x
			and = Long.parseLong(rawtype.substring(rawtype.indexOf('&') + 3), 16);
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
			return new Type(type, -1, null, null, unsigned);
		} else if (type.startsWith("pstring")) {
			return new Type(type, -1, null, null, unsigned);
		} else if (type.startsWith("search")) {
			return new Type(type, -1, null, null, unsigned);
		} else if (type.startsWith("regex")) {
			return new Type(type, -1, null, null, unsigned);
		} else if ("default".equals(type)) {
			return new Type(type, -1, null, null, unsigned);
		}

		throw new IllegalArgumentException("Type '" + type + "' is unknown.");
	}

	String convertString(String str) {
		// some manual conversions
		str = str.replace("\\ ", " ");
		str = str.replace("\\<", "\\\\<");
		str = str.replace("\"", "\\\"");
		try {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
			String cstr = (String) engine.eval("new java.lang.String(\"" + str + "\")");
			// in some magic rules is appended an useless \0 
			// (for example for "POSIX tar archives")
			return cstr.replace("\0", "");
		} catch (ScriptException e) {
			throw new IllegalStateException("Cannot parse string '" + str + "'");
		}
	}

	/**
	 * Convert to int from decimal, octal and hexdecimal format. The result is
	 * unsigned.
	 * 
	 * @throws IOException
	 */
	int toInt(String value) throws IOException {
		if (value.startsWith("0x")) {
			// Hexdecimal
			return Integer.parseInt(value.substring(2), 16);
		} else if (value.startsWith("0") && (value.length() > 1)) {
			// Octal
			return Integer.parseInt(value.substring(1), 8);
		} else if (value.startsWith("&") && (value.length() > 1)) {
			/**
			 * Sometimes you do not know the exact offset as this depends on the
			 * length or position (when indirection was used before) of
			 * preceding fields. You can specify an offset relative to the end
			 * of the last up-level field using ‘&’ as a prefix to the offset:
			 */
			return currentPosition + toInt(value.substring(1));
		} else if (value.startsWith("(") && value.endsWith(")")) {
			// Indirect offset: read from the file being examined.
			long result = -1;

			// remove ( and )
			String indirect = value.substring(1, value.length() - 1);

			int idx = indirect.indexOf('.');
			if (idx < 0) {
				// Offset without type or operations
				return toInt(indirect);
			} else {
				int offset = toInt(indirect.substring(0, idx));

				char t = indirect.charAt(idx + 1);
				result = getIndirectValue(offset, t);

				// operation
				if (indirect.length() > idx + 2) {
					char op = indirect.charAt(idx + 2);
					long rightValue = toLong(indirect.substring(idx + 3));
					result = doOperation(result, op, rightValue);
				}
			}

			// TODO test indirect offsets (testWinExe)
			return (int) result;
		}

		// Decimal
		return Integer.parseInt(value);
	}

	private long doOperation(long left, char op, long right) {
		switch (op) {
		case '+':
			return left + right;
		case '-':
			return left - right;
		case '*':
			return left * right;
		case '/':
			return left / right;
		case '%':
			return left % right;
		case '&':
			return left & right;
		case '|':
			return left | right;
		case '^':
			return left ^ right;
		default:
			throw new IllegalArgumentException("Unknown type: '" + op + "'");
		}
	}

	private long getIndirectValue(int offset, char t) throws IOException {
		switch (t) {
		case 'b':
			// little-endian byte
			return readByte(offset, 1)[0];
		case 'i':
			// little-endian id3 length
			// NOT IMPLEMENTED
			return -1;
		case 's':
			// little-endian short
			return ByteUtil.toShort(LITTLE_ENDIAN, readByte(offset, 2));
		case 'l':
			// little-endian long
			return ByteUtil.toLong(LITTLE_ENDIAN, readByte(offset, 8));
		case 'B':
			// big-endian byte
			return readByte(offset, 1)[0];
		case 'I':
			// big-endian id3 length
			// NOT IMPLEMENTED
			return -1;
		case 'S':
			// big-endian short
			return ByteUtil.toShort(BIG_ENDIAN, readByte(offset, 2));
		case 'L':
			// big-endian long
			return ByteUtil.toLong(BIG_ENDIAN, readByte(offset, 8));
		case 'm':
			// NOT IMPLEMENTED
			return -1;
		default:
			throw new IllegalArgumentException("Unknown type: '" + t + "'");
		}
	}

	/**
	 * Convert to long from decimal, octal and hexdecimal format. The result is
	 * unsigned.
	 */
	private long toLong(String rawvalue) {
		String value = rawvalue;
		if (rawvalue.toUpperCase().endsWith("L")) {
			value = rawvalue.substring(0, rawvalue.length() - 1);
		}

		if (value.startsWith("0x")) {
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
	 * Convert to double from decimal, octal and hexdecimal format. The result
	 * is unsigned.
	 */
	private double toDouble(String value) {
		return Double.parseDouble(value);
	}

	private void printf(String format, Object... args) {
		//System.out.printf(format, args);
	}

	private void println(String line) {
		System.out.println(line);
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
	 * fileDescription.
	 * 
	 */
	public class Result implements Serializable {
		private String mime;
		private String description;

		public String getMime() {
			return mime;
		}

		public String getDescription() {
			return description;
		}

		@Override
		public String toString() {
			return getDescription() + " ; mimetype: " + mime;
		}
	}

	private class Type {
		/**
		 * The name assigned to this type. It must be unique.
		 */
		private final String name;

		/**
		 * The lenght in bytes. -1 for values without fixed lenght as strings.
		 */
		private final int lenght;

		/**
		 * The byte order : big-endian or little-endian.
		 */
		private final ByteOrder order;

		/**
		 * In not {@code null}, the value is to be AND’ed with the numeric value
		 * before any comparisons are done.
		 */
		private final Long and;

		/**
		 * If {@code true} the ordered comparisons should be unsigned.
		 */
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
			return name.indexOf("byte") != -1 || name.indexOf("short") != -1
			|| name.indexOf("long") != -1 || name.indexOf("quad") != -1;
		}

		boolean isDecimalNumber() {
			return name.indexOf("float") != -1 || name.indexOf("double") != -1;
		}

		boolean isString() {			
			return name.indexOf("string") != -1;
		}

		boolean isSearch() {
			// TODO implement 'search'
			return name.indexOf("search") != -1;
		}

		boolean isRegex() {
			return name.indexOf("regex") != -1;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Type [and=").append(and);
			builder.append(", lenght=").append(lenght);
			builder.append(", name=").append(name);
			builder.append(", order=").append(order);
			builder.append(", unsigned=").append(unsigned).append("]");
			return builder.toString();
		}
	}
}
