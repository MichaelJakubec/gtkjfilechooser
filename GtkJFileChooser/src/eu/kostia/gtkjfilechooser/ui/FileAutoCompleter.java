package eu.kostia.gtkjfilechooser.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.JTextComponent;

public class FileAutoCompleter extends AutoCompleter {
	private List<String> list;
	public FileAutoCompleter(JTextComponent comp) {
		super(comp);
		list = new ArrayList<String>();
		list.add("ALABAMA");
		list.add("ALASKA");
		list.add("AMERICAN");
		list.add("ARIZONA");
		list.add("ARKANSAS");
		list.add("CALIFORNIA");
		list.add("COLORADO");
		list.add("CONNECTICUT");
		list.add("DELAWARE");
		list.add("DISTRICT");
		list.add("FEDERATED");
		list.add("FLORIDA");
		list.add("GEORGIA");
		list.add("GUAM");
		list.add("HAWAII");
		list.add("IDAHO");
		list.add("ILLINOIS");
		list.add("INDIANA");
		list.add("IOWA");
		list.add("KANSAS");
		list.add("KENTUCKY");
		list.add("LOUISIANA");
		list.add("MAINE");
		list.add("MARSHALL");
		list.add("MARYLAND");
		list.add("MASSACHUSETTS");
		list.add("MICHIGAN");
		list.add("MINNESOTA");
		list.add("MISSISSIPPI");
		list.add("MISSOURI");
		list.add("MONTANA");
		list.add("NEBRASKA");
		list.add("NEVADA");
		list.add("NEW");
		list.add("NEW");
		list.add("NEW");
		list.add("NEW");
		list.add("NORTH");
		list.add("NORTH");
		list.add("NORTHERN");
		list.add("OHIO");
		list.add("OKLAHOMA");
		list.add("OREGON");
		list.add("PALAU");
		list.add("PENNSYLVANIA");
		list.add("PUERTO");
		list.add("RHODE");
		list.add("SOUTH");
		list.add("SOUTH");
		list.add("TENNESSEE");
		list.add("TEXAS");
		list.add("UTAH");
		list.add("VERMONT");
		list.add("VIRGIN");
		list.add("VIRGINIA");
		list.add("WASHINGTON");
		list.add("WEST");
		list.add("WISCONSIN");
		list.add("WYOMING");
	}




	@Override
	protected List<String> updateListData(String value) {
		List<String> suggestions = new ArrayList<String>();
		
		for (String entry : list) {
			if (entry.startsWith(value) && !entry.equals(value)){
				suggestions.add(entry);
			}
		}
		
		return suggestions;
	}
}
