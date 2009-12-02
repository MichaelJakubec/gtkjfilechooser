/*
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
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 */
package eu.kostia.gtkjfilechooser.filewatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import eu.kostia.gtkjfilechooser.filewatcher.FileEvent.FileEventType;

/**
 * <p>
 * FileWatcher
 * </p>
 * 
 * 
 * 
 * @author $Author:$
 * @version $Revision:$
 */
public class FileWatcher {

	private long timeStamp;
	private File file;
	private List<FileListener> listeners;
	
	private TimerTask timerTask;
	private Timer timer;
		

	public FileWatcher(File aFile) throws FileNotFoundException {
		if (!aFile.exists()) {
			throw new FileNotFoundException(aFile.getAbsolutePath() + " not found!");
		}
		this.file = aFile;
		this.timeStamp = aFile.lastModified();
		this.listeners = new ArrayList<FileListener>();
		this.timerTask = new TimerTask() {			
			@Override
			public void run() {
				FileWatcher.this.watch();				
			}
		};
	}

	private void notifyEvent(FileEvent evt) {
		for (FileListener l : listeners) {
			l.fileChanged(evt);
		}
	}

	public void addFileListener(FileListener l) {
		listeners.add(l);
	}

	public void removeFileListener(FileListener l) {
		listeners.remove(l);
	}

	public List<FileListener> getAllFileListeners() {
		return listeners;
	}

	
	private void watch() {
		if (file.exists()) {
			long currentTimeStamp = file.lastModified();
			
			if (this.timeStamp != currentTimeStamp) {
				this.timeStamp = currentTimeStamp;
				FileEvent evt = new FileEvent(this, file, FileEventType.MODIFIED);				
				notifyEvent(evt);
			}
		} else {
			//File was deleted
			FileEvent evt = new FileEvent(this, file, FileEventType.DELETED);
			notifyEvent(evt);
			//after the notification stop the timer
			stop();
		}

	}

	public void start() {
		if (timer != null) {
			stop();
		}
		
		timer = new Timer();
		// repeat the check every second
		timer.schedule(timerTask, new Date(), 1000);
	}

	/**
	 * This method may be called repeatedly; the second and subsequent calls have no effect.
	 */
	public void stop() {
		if (timer != null) {
			timer.cancel();
		}
	}
}
