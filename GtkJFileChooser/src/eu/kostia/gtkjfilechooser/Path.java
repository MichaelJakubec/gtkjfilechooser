package eu.kostia.gtkjfilechooser;

import java.io.Serializable;

public interface Path extends Serializable {

	public String getName();

	public String getLocation();

	public String getIconName();
}
