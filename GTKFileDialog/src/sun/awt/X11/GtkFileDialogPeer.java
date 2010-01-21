/*
 * Copyright 1995-2006 Sun Microsystems, Inc.  All Rights Reserved.
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
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package sun.awt.X11;

import java.awt.FileDialog;
import java.awt.peer.FileDialogPeer;
import java.io.File;
import java.io.FilenameFilter;

/**
 * FileDialogPeer for the GtkFileChooser.
 * 
 * @author c.cerbo
 */
//TODO uncomment
class GtkFileDialogPeer /* extends XDialogPeer */ implements FileDialogPeer {

	private FileDialog fd;

	public GtkFileDialogPeer(FileDialog fd) {
		//super((Dialog) fd); //TODO uncomment
		this.fd = fd;
	}

	private native void run(String title, int mode, String dir, String file,
			FilenameFilter filter);

	private native void quit();

	void setFileInternal(String filename) {
		if (filename == null || filename.trim().isEmpty()) {
			fd.setFile(null);
			fd.setDirectory(null);
		} else {
			File filen = new File(filename);
			fd.setFile(filen.getName());
			fd.setDirectory(filen.getParent() + File.separator);
		}
	}

	boolean filenameFilterCallback(String fullname) {
		if (fd.getFilenameFilter() == null) {
			// no filter, accept all.
			return true;
		}

		File filen = new File(fullname);
		return fd.getFilenameFilter().accept(new File(filen.getParent()),
				filen.getName());
	}

	@Override
	public void setVisible(boolean b) {
		XToolkit.awtLock();
		try {
			if (b) {
				run(fd.getTitle(), fd.getMode(), fd.getDirectory(), fd
						.getFile(), fd.getFilenameFilter());
			} else {
				quit();
			}
		} finally {
			XToolkit.awtUnlock();
		}
	}
    
	//@Override //TODO uncomment
    public void dispose() {
    	quit();
    }
    
	@Override
	public void setDirectory(String dir) {
		// We do not implement this method because we
		// have delegated to FileDialog#setDirectory
	}

	@Override
	public void setFile(String file) {
		// We do not implement this method because we
		// have delegated to FileDialog#setFile
	}

	@Override
	public void setFilenameFilter(FilenameFilter filter) {
		// We do not implement this method because we
		// have delegated to FileDialog#setFilenameFilter
	}

}