package eu.kostia.gtkjfilechooser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ByteUtil {
	/**
	 * Integer to byte array
	 */
	static public byte[] toByteArray(final int i) {
		return new ToByteArray() {
			@Override
			void write(DataOutputStream dos) throws IOException {
				dos.writeInt(i);
			}
		}.toByteArray();
	}

	public static byte[] toByteArray(final int[] ints) {
		return new ToByteArray() {
			@Override
			void write(DataOutputStream dos) throws IOException {
				for (int i : ints) {
					dos.writeInt(i);
				}
			}
		}.toByteArray();
	}

	/**
	 * Long to byte array
	 */
	static public byte[] toByteArray(final long l) {
		return new ToByteArray() {
			@Override
			void write(DataOutputStream dos) throws IOException {
				dos.writeLong(l);
			}
		}.toByteArray();
	}

	static public byte[] toByteArray(final String s) {
		return new ToByteArray() {
			@Override
			void write(DataOutputStream dos) throws IOException {
				dos.writeBytes(s);
			}
		}.toByteArray();
	}

	public static byte[] toByteArray(final String... strings) {
		return new ToByteArray() {
			@Override
			void write(DataOutputStream dos) throws IOException {
				for (String s : strings) {
					dos.writeBytes(s);
				}
			}
		}.toByteArray();
	}

	public static byte[] toByteArray(final Serializable obj) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			bos.close();
			byte[] data = bos.toByteArray();
			return data;
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	/**
	 * Concatenate byte arrays
	 */
	static public byte[] concat(byte[]... array) {
		byte[] concat = array[0];
		for (int i = 1; i < array.length; i++) {
			byte[] current = array[i];
			int oldLength = concat.length;
			int newLength = oldLength + current.length;
			concat = Arrays.copyOf(concat, newLength);
			for (int j = oldLength; j < newLength; j++) {
				concat[j] = current[j - oldLength];
			}
		}

		return concat;
	}

	/**
	 * Convert an short into 2 bytes
	 * 
	 * @param value
	 *            The short to convert
	 * @param bigEndian
	 *            {@code true} for the big-endian format, else {@code false} for
	 *            little-endian.
	 * @return The 2-byte representation of the primitive short.
	 */
	static public byte[] toBytes(short value, ByteOrder bOrder) {
		return toBytes(value, bOrder, 2);
	}

	/**
	 * Convert an integer into 4 bytes
	 * 
	 * @param valuei
	 *            The integer to convert
	 * @param bigEndian
	 *            {@code true} for the big-endian format, else {@code false} for
	 *            little-endian.
	 * @return The 4-byte representation of the primitive int.
	 */
	static public byte[] toBytes(int value, ByteOrder bOrder) {
		return toBytes(value, bOrder, 4);
	}

	/**
	 * Convert a long into 8 bytes
	 * 
	 * @param value
	 *            The long to convert
	 * @param bigEndian
	 *            {@code true} for the big-endian format, else {@code false} for
	 *            little-endian.
	 * @return The 8-byte representation of the primitive long.
	 */
	static public byte[] toBytes(long value, ByteOrder bOrder) {
		return toBytes(value, bOrder, 8);
	}

	static private byte[] toBytes(long value, ByteOrder bOrder, int n) {
		byte[] dword = new byte[n];
		for (int i = 0; i < n; i++) {
			if (ByteOrder.BIG_ENDIAN == bOrder) {
				dword[i] = (byte) ((value >> (i * 8)) & 0xff);
			} else {
				// for litte-endian simply reverse the order
				dword[i] = (byte) ((value >> ((n - 1 - i) * 8)) & 0xff);
			}
		}

		return dword;
	}

	/**
	 * Convert a byte array of length two into a short.
	 * 
	 * @param bigEndian
	 *            {@code true} for the big-endian format, else {@code false} for
	 *            little-endian.
	 * @param b
	 *            The byte array to convert.
	 * @return The converted short value.
	 */
	static public short toShort(ByteOrder bOrder, byte... b) {
		checkArray(b, "short", 2);
		if (ByteOrder.BIG_ENDIAN == bOrder) {
			return (short) ((b[0] & 0xff) << 0 | (b[1] & 0xff) << 8);
		} else {
			return (short) ((b[0] & 0xff) << 8 | (b[1] & 0xff) << 0);
		}
	}

	/**
	 * Convert a byte array of length four into an integer.
	 * 
	 * @param bigEndian
	 *            {@code true} for the big-endian format, else {@code false} for
	 *            little-endian.
	 * @param b
	 *            The byte array to convert.
	 * @return The converted integer value.
	 */
	static public int toInt(ByteOrder bOrder, byte... b) {
		checkArray(b, "int", 4);
		if (ByteOrder.BIG_ENDIAN == bOrder) {
			return (b[0] & 0xff) << 0 | (b[1] & 0xff) << 8 | (b[2] & 0xff) << 16
			| (b[3] & 0xff) << 24;
		} else {
			return (b[0] & 0xff) << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8
			| (b[3] & 0xff) << 0;
		}

	}

	/**
	 * Convert a byte array of length eight into a long.
	 * 
	 * @param bigEndian
	 *            {@code true} for the big-endian format, else {@code false} for
	 *            little-endian.
	 * @param b
	 *            The byte array to convert.
	 * @return The converted long value.
	 */
	static public long toLong(ByteOrder bOrder, byte... b) {
		checkArray(b, "long", 8);
		if (ByteOrder.BIG_ENDIAN == bOrder) {
			return (b[0] & 0xff) << 0 | (b[1] & 0xff) << 8 | (b[2] & 0xff) << 16
			| (b[3] & 0xff) << 24 | (b[4] & 0xff) << 32 | (b[5] & 0xff) << 40
			| (b[6] & 0xff) << 48 | (b[7] & 0xff) << 56;
		} else {
			return (b[0] & 0xff) << 56 | (b[1] & 0xff) << 48 | (b[2] & 0xff) << 40
			| (b[3] & 0xff) << 32 | (b[4] & 0xff) << 24 | (b[5] & 0xff) << 16
			| (b[6] & 0xff) << 8 | (b[7] & 0xff) << 0;
		}
	}

	static public float toFloat(ByteOrder bOrder, byte... b) {
		checkArray(b, "float", 4);

		return Float.intBitsToFloat(toInt(bOrder, b));
	}

	static public double toDouble(ByteOrder bOrder, byte... b) {
		checkArray(b, "float", 8);

		return Double.longBitsToDouble(toLong(bOrder, b));
	}

	static private void checkArray(byte[] bytes, String type, int n) {
		if (bytes.length != n) {
			throw new IllegalArgumentException(n + " bytes needed for type " + type);
		}

	}

}

abstract class ToByteArray {
	byte[] toByteArray() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			write(dos);
			dos.flush();

			return bos.toByteArray();
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	abstract void write(DataOutputStream dos) throws IOException;
}
