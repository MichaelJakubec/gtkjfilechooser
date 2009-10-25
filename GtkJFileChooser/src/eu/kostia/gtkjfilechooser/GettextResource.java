package eu.kostia.gtkjfilechooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * <p>
 * Read and use .mo files for internationalization.
 * </p>
 * The .mo file format is described <a href=
 * "http://www.gnu.org/software/hello/manual/gettext/MO-Files.html#MO-Files"
 * >here < / a > .
 * 
 * @author Costantino Cerbo
 * 
 */
public class GettextResource extends ResourceBundle {
	// enable debugging. Set false in production code.
	static final boolean DEBUG = false;

	// magic number hex = 95 04 12 DE
	static final int[] MAGIC = new int[] { -107, 4, 18, -34 };

	// magic number reversed hex = DE 12 04 95
	static final int[] MAGIC_REVERSED = new int[] { -34, 18, 4, -107 };

	/**
	 * Cache GettextResource instances with .mo file name.
	 */
	static private Map<String, GettextResource> cachedGettextResource = new HashMap<String, GettextResource>();

	/**
	 * Are the byte sequence reversed?
	 */
	private transient Boolean reversed;

	/**
	 * file format revision = 0 (position: 4)
	 */
	private transient int revision;

	/**
	 * Number of strings (position: 8)
	 */
	private transient int n;
	/**
	 * Offset of table with original strings (position: 12)
	 */
	private transient int o;

	/**
	 * Index of the currently processed original string (length & offset) [0,
	 * n-1]
	 */
	private transient int oj;

	/**
	 * Array of the lengths of the original strings.
	 */
	private transient int[] oo_length;

	/**
	 * Array of the offsets of the original strings.
	 */
	private transient int[] oo_offset;

	/**
	 * Offset of table with translation strings (position: 16)
	 */
	private transient int t;

	/**
	 * Index of the currently processed translation string (length & offset) [0,
	 * n-1]
	 */
	private transient int tj;

	/**
	 * Array of the lengths of the translation strings.
	 */
	private transient int[] tt_length;

	/**
	 * Array of the offsets of the translation strings.
	 */
	private transient int[] tt_offset;

	/**
	 * Size of hashing table (position: 20)
	 */
	private transient int s;
	/**
	 * Offset of hashing table (position: 24)
	 */
	private transient int h;

	/**
	 * Array of the byte buffers for each msgid.
	 * We use byte buffers, because we map regions of the file channel into memory. 
	 */
	private ByteBuffer[] msgidByteBuffers;

	/**
	 * Array of the byte buffers for each msgstr.
	 * We use byte buffers, because we map regions of the file channel into memory. 
	 */
	private ByteBuffer[] msgstrByteBuffers;

	/**
	 * Load a spefic .mo file
	 * 
	 * @param moFile
	 * @throws IOException
	 */
	public GettextResource(File moFile) throws IOException {
		init(moFile);
	}

	public GettextResource(Locale loc, String localedir, String textdomain) throws IOException {
		String moFilename = localedir + File.separator + loc.toString() + File.separator + "LC_MESSAGES" + File.separator +textdomain + ".mo";
		if (!new File(moFilename).exists()) {
			moFilename = localedir + File.separator + loc.getLanguage() + File.separator + "LC_MESSAGES" + File.separator +textdomain + ".mo";
			if (!new File(moFilename).exists()) {
				throw new IOException("Cannot find resource " + moFilename);
			}
		}

		init(new File(moFilename));
	}

	public GettextResource(String localedir, String textdomain) throws IOException {
		this(Locale.getDefault(), localedir, textdomain);
	}

	public GettextResource(String textdomain) throws IOException {
		this("/usr/share/locale", textdomain);
	}

	private void init(File moFile) throws IOException, FileNotFoundException {
		//look in the cache first
		String key = moFile.getAbsolutePath();
		GettextResource instance = cachedGettextResource.get(key);
		if (instance == null) {
			// update cache
			cachedGettextResource.put(key, this);
		} else {
			//The only instance variables needed are msgidByteBuffers and msgstrByteBuffers
			this.msgidByteBuffers = instance.msgidByteBuffers;
			this.msgstrByteBuffers = instance.msgstrByteBuffers;
			return;
		}

		readOffsetsAndLenghts(moFile);

		logOffsetAndLenghtInfo();

		FileChannel channel = null;
		try {
			channel = new RandomAccessFile(moFile, "r").getChannel();
			for (int i = 0; i < n; i++) {
				msgidByteBuffers[i] = channel.map(MapMode.READ_ONLY, oo_offset[i], oo_length[i]);
				msgstrByteBuffers[i] = channel.map(MapMode.READ_ONLY, tt_offset[i], tt_length[i]);
			}
		} finally {
			if (channel != null) {
				// Despite the fact that the channel has been closed, the data
				// in the file continues to be available via the memory map
				channel.close();
			}
		}

		//We've mapped the file into memory, we can now release the resources not anymore needed.
		releaseOffsetAndLenghtArrays();

		logMessages();
	}


	/**
	 * Read the offset and length for each original and translated string. This
	 * information are stored in the arrays: oo_offset, oo_length, tt_offset and
	 * tt_length.
	 * 
	 * This method read sequentially the first h bytes in the .mo file. Then we
	 * use access the file random using a {@link FileChannel} and mapping the
	 * just found positions.
	 */
	private void readOffsetsAndLenghts(File file) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			byte[] buffer = new byte[4096];
			int index = 0;
			for (int n; (n = is.read(buffer)) != -1;) {
				for (int i = 0; i < n; i++) {
					handleByte(buffer[i], index);
					index++;
					if (index > 12 && index == h) {
						break;
					}
				}
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	private void handleByte(byte b, int index) throws IOException {
		if (index == 0) {
			reversed = b == -34;
		}

		// Initialise the arrays
		if (index == 12) {
			oo_length = new int[n];
			oo_offset = new int[n];
			tt_length = new int[n];
			tt_offset = new int[n];
			msgidByteBuffers = new ByteBuffer[n];
			msgstrByteBuffers = new ByteBuffer[n];
		}

		if (index < 4) {
			// check if the file is valid
			if (b != MAGIC[index]) {
				if (b != MAGIC_REVERSED[index]) {
					throw new IOException(String.format("Invalid .mo file: byte[%d]=%h", index, b & 0xff));
				}
			}
		} else if (index >= 4 && index < 8) {
			int k = 4;
			revision = swap(b, index, revision, k);
		} else if (index >= 8 && index < 12) {
			int k = 8;
			n = swap(b, index, n, k);
		} else if (index >= 12 && index < 16) {
			int k = 12;
			o = swap(b, index, o, k);
		} else if (index >= 16 && index < 20) {
			int k = 16;
			t = swap(b, index, t, k);
		} else if (index >= 20 && index < 24) {
			int k = 20;
			s = swap(b, index, s, k);
		} else if (index >= 24 && index < 28) {
			int k = 24;
			h = swap(b, index, h, k);
		} else if (index >= o + oj * 8 && index < o + oj * 8 + 4 && index < o + (n - 1) * 8 + 4) {
			oo_length[oj] = swap(b, index, oo_length[oj], o + oj * 8);

			// increment the current entry index
			if (index == o + oj * 8 + 3) {
				oj++;
			}
		} else if (index >= o + (oj - 1) * 8 + 4 && index < o + (oj - 1) * 8 + 4 + 4 && index < o + (n - 1) * 8 + 4 + 4) {
			oo_offset[oj - 1] = swap(b, index, oo_offset[oj - 1], o + (oj - 1) * 8 + 4);
		} else if (index >= t + tj * 8 && index < t + tj * 8 + 4 && index < t + (n - 1) * 8 + 4) {
			tt_length[tj] = swap(b, index, tt_length[tj], t + tj * 8);

			// increment the current entry index
			if (index == t + tj * 8 + 3) {
				tj++;
			}
		} else if (index >= t + (tj - 1) * 8 + 4 && index < t + (tj - 1) * 8 + 4 + 4
				&& index < t + (n - 1) * 8 + 4 + 4) {
			tt_offset[tj - 1] = swap(b, index, tt_offset[tj - 1], t + (tj - 1) * 8 + 4);
		}
	}

	/**
	 * Incremental swap
	 * 
	 * @return signed 32 bit integer
	 */
	private int swap(byte b, int index, int n, int k) {
		int x = reversed ? (index - k) * 8 : (3 - index - k) * 8;
		if (x != 0) {
			n = n | (b & 0xff) << x;
		} else {
			n = n | b & 0xff;
		}
		return n;
	}

	/**
	 * Release the offset and length arrays when they aren't needed anymore.
	 */
	private void releaseOffsetAndLenghtArrays() {
		oo_length = null;
		oo_offset = null;
		tt_length = null;
		tt_offset = null;
	}

	@Override
	public Enumeration<String> getKeys() {
		Enumeration<String> en = new Enumeration<String>() {
			private int elementIndex = 0;

			@Override
			public boolean hasMoreElements() {
				return elementIndex < msgidByteBuffers.length;
			}

			@Override
			public String nextElement() {				
				String nextElement = GettextResource.this.toString(msgidByteBuffers[elementIndex]);
				elementIndex ++;
				return nextElement;
			}
		};

		return en;
	}

	/**
	 * Note that we don't throw a MissingResourceException when no
	 * translation is found. In the GNU gettext approach, the gettext function
	 * returns the (English) message key in that case.
	 */
	@Override
	protected Object handleGetObject(String key) {
		return _(key);
	}

	public String _(String msgid) {
		ByteBuffer msgidByteBuffer = ByteBuffer.wrap(msgid.getBytes());
		int idx = Arrays.binarySearch(msgidByteBuffers, msgidByteBuffer);

		if (idx < 0) {
			return msgid;
		}

		String msgstr = toString(msgstrByteBuffers[idx]);
		return !msgstr.isEmpty() ? msgstr : msgid;
	}

	private String toString(ByteBuffer buf) {
		byte[] array = new byte[buf.limit()];

		//TODO buf.position(0) necessary?
		buf.position(0);
		buf.get(array);
		buf.position(0);
		return new String(array);
	}

	/**
	 * Some debugging methods follow...
	 */
	private void logOffsetAndLenghtInfo() {
		if (DEBUG) {
			debug("reversed: ", reversed);
			debug("n = ", n);
			debug("o = ", o);
			debug("t = ", t);
			debug("s = ", s);
			debug("h = ", h);
			debug("oo_offset = ", oo_offset);
			debug("oo_length = ", oo_length);
			debug("tt_offset = ", tt_offset);
			debug("tt_length = ", tt_length);
		}
	}

	/**
	 * Log msgids and msgstrs.
	 */
	private void logMessages() {
		if (DEBUG) {
			for (int i = 0; i < msgidByteBuffers.length; i++) {
				debug("msgid: \"" + toString(msgidByteBuffers[i]) + "\"");
				debug("msgstr: \"" + toString(msgstrByteBuffers[i]) + "\"");
				debug("");
			}
		}
	}

	private void debug(Object... msgs) {
		if (DEBUG) {
			for (Object msg : msgs) {
				if (msg == null) {
					System.out.print("null");
				} else if (msg.getClass().isArray()) {
					System.out.print("[");
					int len = Array.getLength(msg);
					for (int i = 0; i < len; i++) {
						System.out.print(Array.get(msg, i));
						if (i != (len - 1)) {
							System.out.print(", ");
						}
					}
					System.out.print("]");
				} else {
					System.out.print(String.valueOf(msg));
				}
			}
			System.out.println();
		}
	}
}
