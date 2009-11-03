package eu.kostia.gtkjfilechooser;

import java.lang.reflect.Array;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.kostia.gtkjfilechooser.ui.GtkFileChooserUI;

/**
 * Naive logger with stack strace. Set DEBUG to false for production code.
 * 
 * @author c.cerbo
 * 
 */
public class Log {

	static final private Logger LOG = Logger.getLogger(GtkFileChooserUI.class.getName());

	/**
	 * Set false for production code
	 */
	static final private boolean DEBUG = true;

	static public void debug(Object... msgs) {
		if (DEBUG) {
			String location = getInvokingLocation();

			StringBuilder sb = new StringBuilder();
			sb.append(location);
			sb.append(": ");

			appendMessages(sb, msgs);

			System.out.println(sb);
		}
	}

	static public void debug0(Object... msgs) {
		if (LOG.isLoggable(Level.FINEST)) {
			StringBuilder sb = new StringBuilder();
			appendMessages(sb, msgs);
			LOG.finest(sb.toString());
		}
	}

	private static void appendMessages(StringBuilder sb, Object... msgs) {
		for (Object msg : msgs) {
			if (msg == null) {
				sb.append("null");
			} else if (msg.getClass().isArray()) {
				int len = Array.getLength(msg);
				for (int i = 0; i < len; i++) {
					sb.append(Array.get(msg, i));
					if (i != (len - 1)) {
						sb.append(", ");
					}
				}
			} else {
				sb.append(String.valueOf(msg));
			}
		}
	}

	private static String getInvokingLocation() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		String location = null;
		for (int i = 0; i < stackTrace.length; i++) {
			StackTraceElement s = stackTrace[i];
			if (Log.class.getName().equals(s.getClassName())
					&& "debug".equals(s.getMethodName())) {
				StackTraceElement next = stackTrace[i + 1];
				location = next.getClassName() + "." + next.getMethodName() + "("
						+ next.getFileName() + ":" + next.getLineNumber() + ")";
			}
		}
		return location;
	}

	public static void main(String[] args) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Log.debug("ciao");
			}
		}).start();
	}

}
