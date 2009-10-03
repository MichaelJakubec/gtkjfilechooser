package eu.kostia.gtkjfilechooser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class UrlUtil {

	static public String decode(String str) {
		try {
			return URLDecoder.decode(str, Charset.defaultCharset().toString());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	static public String encode(String str) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			int dec = ch;

			// ASCII Control characters
			if (dec >= 0 && dec <= 31) {
				sb.append("%").append(Integer.toHexString(dec));
				continue;
			}

			// Non-ASCII characters
			if (dec >= 127) {
				sb.append("%").append(Integer.toHexString(dec));
				continue;
			}

			// Reserved and unsafe characters
			switch (dec) {
			case '$':
			case '&':
			case '+':
			case ',':
			case ':':
			case ';':
			case '=':
			case '?':
			case '@':
			case ' ':
			case 34: // Quotation marks
			case '<':
			case '>':
			case '#':
			case '%':
			case '{':
			case '}':
			case '|':
			case 92: // Backslash
			case '^':
			case '~':
			case '[':
			case ']':
			case '`':
				sb.append("%").append(Integer.toHexString(dec));
				continue;
			}

			// Append the char as it is
			sb.append(ch);
		}

		return sb.toString();
	}
}
