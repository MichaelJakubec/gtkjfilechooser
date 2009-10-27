package eu.kostia.gtkjfilechooser;

import static eu.kostia.gtkjfilechooser.I18N._;
import eu.kostia.gtkjfilechooser.FreeDesktopUtil.WellKnownDir;

/**
 * Basic location in a file system: home dir, desktop and root.
 * 
 * @author c.cerbo
 *
 */
public class BasicPath implements Path {

	static public final BasicPath HOME = new BasicPath(System.getProperty("user.name"), System.getProperty("user.home"), "places/user-home");
	static public final BasicPath DESKTOP = new BasicPath(FreeDesktopUtil.getWellKnownDirPath(WellKnownDir.DESKTOP).getName(), FreeDesktopUtil.getWellKnownDirPath(WellKnownDir.DESKTOP).getAbsolutePath(), "places/user-desktop");

	static public final BasicPath ROOT = new BasicPath(_("File System"), "/", "gtk-harddisk"); 

	private static final long serialVersionUID = 1L;

	protected String location;
	protected String name;
	protected String iconName;

	public BasicPath() {
		super();
	}

	public BasicPath(String name, String location, String iconName) {
		this();
		this.name = name;
		this.location = location;
		this.iconName = iconName;
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getIconName() {
		return iconName;
	}

}
