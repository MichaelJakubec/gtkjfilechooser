/*******************************************************************************
 * Copyright 2010 Costantino Cerbo.  All Rights Reserved.
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
package eu.kostia.gtkjfilechooser.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

public class TestAutoCompleterTextField extends Autocompleter {
	private List<String> list;
	public TestAutoCompleterTextField(JTextField comp) {
		super(comp);
		list = new ArrayList<String>();
		list.add("alabama");
		list.add("alaska");
		list.add("american");
		list.add("arizona");
		list.add("arkansas");
		list.add("california");
		list.add("colorado");
		list.add("connecticut");
		list.add("delaware");
		list.add("district");
		list.add("federated");
		list.add("florida");
		list.add("georgia");
		list.add("guam");
		list.add("hawaii");
		list.add("idaho");
		list.add("illinois");
		list.add("indiana");
		list.add("iowa");
		list.add("kansas");
		list.add("kentucky");
		list.add("louisiana");
		list.add("maine");
		list.add("marshall");
		list.add("maryland");
		list.add("massachusetts");
		list.add("michigan");
		list.add("minnesota");
		list.add("mississippi");
		list.add("missouri");
		list.add("montana");
		list.add("nebraska");
		list.add("nevada");
		list.add("northern");
		list.add("ohio");
		list.add("oklahoma");
		list.add("oregon");
		list.add("palau");
		list.add("pennsylvania");
		list.add("puerto");
		list.add("rhode");
		list.add("south");
		list.add("south");
		list.add("tennessee");
		list.add("texas");
		list.add("utah");
		list.add("vermont");
		list.add("virgin");
		list.add("virginia");
		list.add("washington");
		list.add("west");
		list.add("wisconsin");
		list.add("wyoming");
	}




	@Override
	protected List<String> updateSuggestions(String value) {
		List<String> suggestions = new ArrayList<String>();

		for (String entry : list) {
			if (entry.startsWith(value) && !entry.equals(value)){
				suggestions.add(entry);
			}
		}

		return suggestions;
	}
}
