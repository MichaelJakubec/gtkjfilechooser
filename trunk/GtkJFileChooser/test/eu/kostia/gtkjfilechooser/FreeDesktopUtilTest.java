package eu.kostia.gtkjfilechooser;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.kostia.gtkjfilechooser.FreeDesktopUtil;
import eu.kostia.gtkjfilechooser.RemovableDevice;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil.WellKnownDir;

public class FreeDesktopUtilTest {

	@Test
	// Test that no exception is thrown
	public void testGetWellKnownDirPath() {
		File desktop = FreeDesktopUtil.getWellKnownDirPath(WellKnownDir.DESKTOP);
		System.out.println(desktop+": "+ desktop.exists());
	}

	@Test
	// Test that no exception is thrown
	public void testExpandEnv() {
		String str = "Hello $USER, your \\\"home\\\" is $HOME \"while\" your host $HOSTNAME.";
		String actual = FreeDesktopUtil.expandEnv(str);

		String expected = "Hello " + System.getenv("USER") + ", your \"home\" is "
		+ System.getenv("HOME") + " while your host " + System.getenv("HOSTNAME")
		+ ".";
		Assert.assertEquals(expected, actual);
	}

	@Test
	// Test that no exception is thrown
	public void getRemovableDevices() {
		List<RemovableDevice> removablesDevices = FreeDesktopUtil.getRemovableDevices();
		for (RemovableDevice removableDevice : removablesDevices) {
			System.out.println(removableDevice);
		}

	}

}
