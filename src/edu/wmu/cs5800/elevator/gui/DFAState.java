package edu.wmu.cs5800.elevator.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc This class represents DFA state circle, which represents floor number.
 *       This class will draw circle in the Elevator DFA Panel.
 */
public class DFAState extends JPanel {

	private static final long serialVersionUID = -1399821507998481617L;

	/**
	 * 
	 */
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;
	private String label;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param label
	 */
	public DFAState(int x, int y, int width, int height, Color color, String label) {
		this.x = x;
		this.y = y;
		this.label = label;
		this.width = width;
		this.height = height;
		this.color = color;
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setBounds(x, y, width, height);
	}

	/**
	 * 
	 */
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setPaint(this.color);
		graphics2d.fillOval(0, 0, width, height);
		graphics2d.setPaint(Color.WHITE);
		graphics2d.setColor(Color.WHITE);
		graphics2d.drawString(this.label, this.getWidth() / 2, this.getHeight() / 2);
	}

	/**
	 * 
	 */
	public void setCurrentState() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		Color randomColor = new Color(r, g, b);
		this.setBackground(randomColor);
	}

	public void setNotCurrentState() {
		JButton j = new JButton();
		this.setBackground(j.getBackground());
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
