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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BasicActionDispatcher implements ActionDispatcher {
	protected List<ActionListener> actionListeners;
	
	public BasicActionDispatcher() {
		actionListeners = new ArrayList<ActionListener>();
	}
	
	@Override
	public void addActionListener(ActionListener l) {
		actionListeners.add(l);		
	}

	@Override
	public void removeActionListener(ActionListener l) {
		actionListeners.remove(l);
	}
	
	@Override
	public void removeAllActionListeners() {
		actionListeners.clear();
	}

	@Override
	public void fireActionEvent(ActionEvent e) {
		for (ActionListener l : actionListeners) {
			l.actionPerformed(e);
		}		
	}

}
