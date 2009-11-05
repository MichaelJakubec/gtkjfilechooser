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

public class RemovableDevice extends BasicPath{

	private static final long serialVersionUID = 1L;

	public enum RemovableDeviceType {
		DRIVE_HARDDISK_USB, DRIVE_REMOVABLE_MEDIA_USB, MEDIA_OPTICAL, MEDIA_FLASH, DRIVE_REMOVABLE_MEDIA;

		public String toIconName() {
			return toString().toLowerCase().replace('_', '-');
		}

		/**
		 * Returns the RemovableDeviceType.
		 * 
		 * <pre>
		 * sdb : drive-harddisk-usb.svg (esempio: harddisk esterno usb)
		 * sdc : drive-removable-media-usb.svg (esempio: stick usb)
		 * sr : media-optical.svg  (esempio: CD o DVD)
		 * mmc : media-flash-sd.svg (media-flash.svg)
		 * other : drive-removable-media.svg
		 * </pre>
		 * 
		 * @param dev
		 * @return
		 */
		public static RemovableDeviceType getType(String dev) {
			if (dev.startsWith("/dev/sdb")) {
				return DRIVE_HARDDISK_USB;
			} else if (dev.startsWith("/dev/sdc")) {
				return DRIVE_REMOVABLE_MEDIA_USB;
			} else if (dev.startsWith("/dev/sr")) {
				return MEDIA_OPTICAL;
			} else if (dev.startsWith("/dev/mmc")) {
				return MEDIA_FLASH;
			}

			return DRIVE_REMOVABLE_MEDIA;
		}
	};


	private RemovableDeviceType type;

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public RemovableDeviceType getType() {
		return type;
	}

	public void setType(RemovableDeviceType type) {
		this.type = type;
	}

	@Override
	public String getIconName() {
		return "devices/" + type.toIconName();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RemovableDevice [name=");
		builder.append(name);
		builder.append(", location=");
		builder.append(location);
		builder.append(", type=");
		builder.append(type);
		builder.append(", iconName=");
		builder.append(getIconName());
		builder.append("]");
		return builder.toString();
	}

}
