/*******************************************************************************
 * Copyright 2009 Costantino Cerbo.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 *******************************************************************************/
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
