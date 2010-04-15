/*******************************************************************************
 * Copyright (c) 2010 Costantino Cerbo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Costantino Cerbo - initial API and implementation
 ******************************************************************************/
package eu.kostia.gtkjfilechooser;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.kostia.gtkjfilechooser.FreeDesktopUtil.WellKnownDir;

public class FreeDesktopUtilTest {

	@Test
	// Test that no exception is thrown
	public void testGetWellKnownDirPath() {
		File desktop = FreeDesktopUtil.getWellKnownDirPath(WellKnownDir.DESKTOP);
		System.out.println(desktop);
		Assert.assertTrue(desktop.exists());
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
