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
