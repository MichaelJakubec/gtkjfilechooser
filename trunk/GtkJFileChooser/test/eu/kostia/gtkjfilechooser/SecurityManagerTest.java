package eu.kostia.gtkjfilechooser;

import org.junit.Test;

public class SecurityManagerTest {

	@Test
	public void testFileAlreadyExists() throws Exception {
		//TODO reproduce file already exist exception
		SecurityManager security = System.getSecurityManager();
		String existingFile = "test/eu/kostia/gtkjfilechooser/SecurityManagerTest.java";
		if (security != null) security.checkWrite(existingFile);
	}
}
