package sun.awt.X11;

import java.lang.reflect.Method;

public class XToolkitProxy {
	 @SuppressWarnings("unchecked")
	public boolean loadGTK() {
		 try {
			Class cls = Class.forName("sun.awt.UNIXToolkit");
			Object instance = cls.newInstance();
			Method loadGTK = cls.getMethod("loadGTK", null);
			return (Boolean)loadGTK.invoke(instance, null);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	 }
}
