package eu.kostia.gtkjfilechooser;

//TODO remove this class in the releases
public class Log {
	static public void debug(Object...  msgs){
		if (true){
			for (Object msg : msgs) {
				System.out.print(String.valueOf(msg));
			}
			System.out.println();
		}
	}
}
