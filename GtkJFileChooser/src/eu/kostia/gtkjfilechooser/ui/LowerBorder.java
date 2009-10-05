package eu.kostia.gtkjfilechooser.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.border.AbstractBorder;

public class LowerBorder extends AbstractBorder {

	private static final long serialVersionUID = 1L;

	protected int thickness;
	protected Color lineColor;

	/**
	 * Creates a line border with the specified color and thickness.
	 * 
	 * @param color
	 *            the color of the border
	 * @param thickness
	 *            the thickness of the border
	 */
	public LowerBorder(Color color, int thickness) {
		this.lineColor = color;
		this.thickness = thickness;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Color oldColor = g.getColor();
		g.setColor(lineColor);
		for (int i = 0; i < thickness; i++) {
			g.drawLine(x + i - width, 
					height - y + i, 
					width - i - i - 1, 
					height - i - i - 1);
			//g.drawRect(x + i, y + i, width - i - i - 1, height - i - i - 1);
			g.setColor(oldColor);
		}
	}

}