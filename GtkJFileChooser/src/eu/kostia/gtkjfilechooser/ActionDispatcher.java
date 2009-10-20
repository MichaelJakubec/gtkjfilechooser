package eu.kostia.gtkjfilechooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public interface ActionDispatcher {

	public void addActionListener(ActionListener l);

	public void removeActionListener(ActionListener l);

	public void fireActionEvent(ActionEvent e);

	void removeAllActionListeners();
}
