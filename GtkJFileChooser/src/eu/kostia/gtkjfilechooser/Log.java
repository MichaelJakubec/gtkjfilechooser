package eu.kostia.gtkjfilechooser;

import java.lang.reflect.Array;


/**
 * Naive logger with stack strace.
 * Set DEBUG to false for production code.
 * 
 * @author c.cerbo
 *
 */
public class Log {
	/**
	 * Set false for production code
	 */
	private static final boolean DEBUG = false;

	static public void debug(Object... msgs) {
		if (DEBUG) {
			String location = getInvokingLocation();

			System.out.print(location);
			System.out.print(": ");
			for (Object msg : msgs) {
				if (msg == null) {
					System.out.print("null");
				} else if(msg.getClass().isArray()){
					int len = Array.getLength(msg);
					for (int i = 0; i < len; i++) {
						System.out.print(Array.get(msg, i));
						if (i != (len -1)) {
							System.out.print(", ");
						}						
					}
				} else {
					System.out.print(String.valueOf(msg));
				}
			}
			System.out.println();
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
