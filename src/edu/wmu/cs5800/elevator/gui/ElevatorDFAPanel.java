package edu.wmu.cs5800.elevator.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.wmu.cs5800.elevator.util.Constants;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc this class contains 4 floors states for elevator DFA. this class also
 *       is able to draw lines between states which represents the transitions.
 */
public class ElevatorDFAPanel extends JPanel {

	static DFAState groundFloor;
	static DFAState firstFloor;
	static DFAState secondFloor;
	static DFAState thirdFloor;

	private static final long serialVersionUID = -157415759141981757L;

	/**
	 * 
	 */
	public ElevatorDFAPanel() {
		int offset = 150;
		int endLeft = 600;
		int endDown = 600;
		int stateWidth = 100;
		int stateHeight = 100;
		groundFloor = new DFAState(offset, offset, stateWidth, stateHeight, Color.GREEN, Constants.GROUND_FLOOR_STATE);
		firstFloor = new DFAState(endLeft - offset, offset, stateWidth, stateHeight, Color.BLACK,
				Constants.FIRST_FLOOR_STATE);
		secondFloor = new DFAState(offset, endDown - offset, stateWidth, stateHeight, Color.RED,
				Constants.SECOND_FLOOR_STATE);
		thirdFloor = new DFAState(endLeft - offset, endDown - offset, stateWidth, stateHeight, Color.BLUE,
				Constants.THIRD_FLOOR_STATE);
		setLayout(null);
		setBorder(BorderFactory.createLineBorder(Color.black));
		add(groundFloor);
		add(firstFloor);
		add(secondFloor);
		add(thirdFloor);
		setSize(Constants.DFA_PANEL_WIDTH, Constants.DFA_PANEL_HEIGHT);
	}

	/**
	 * 
	 * @return
	 */
	public static DFAState getGroundFloor() {
		return groundFloor;
	}

	/**
	 * 
	 * @param groundFloor
	 */
	public static void setGroundFloor(DFAState groundFloor) {
		ElevatorDFAPanel.groundFloor = groundFloor;
	}

	public static DFAState getFirstFloor() {
		return firstFloor;
	}

	public static void setFirstFloor(DFAState firstFloor) {
		ElevatorDFAPanel.firstFloor = firstFloor;
	}

	public static DFAState getSecondFloor() {
		return secondFloor;
	}

	public static void setSecondFloor(DFAState secondFloor) {
		ElevatorDFAPanel.secondFloor = secondFloor;
	}

	public static DFAState getThirdFloor() {
		return thirdFloor;
	}

	public static void setThirdFloor(DFAState thirdFloor) {
		ElevatorDFAPanel.thirdFloor = thirdFloor;
	}

	public static void setAllDFAStatesBackground(Color color) {
		groundFloor.setBackground(color);
		firstFloor.setBackground(color);
		secondFloor.setBackground(color);
		thirdFloor.setBackground(color);
	}

	/**
	 * 
	 * @param g
	 * @param stateOne
	 * @param stateTwo
	 */
	protected void drawLineBetweenTwoStates(Graphics g, DFAState stateOne, DFAState stateTwo) {
		g.drawLine(stateOne.getX() + stateOne.getWidth() / 2, stateOne.getY() + stateOne.getHeight() / 2,
				stateTwo.getX() + stateTwo.getWidth() / 2, stateTwo.getY() + stateTwo.getHeight() / 2);
	}

	/**
	 * 
	 */
	public void paintComponent(Graphics g) {
		drawLineBetweenTwoStates(g, groundFloor, firstFloor);
		drawLineBetweenTwoStates(g, groundFloor, secondFloor);
		drawLineBetweenTwoStates(g, groundFloor, thirdFloor);
		drawLineBetweenTwoStates(g, firstFloor, secondFloor);
		drawLineBetweenTwoStates(g, firstFloor, thirdFloor);
		drawLineBetweenTwoStates(g, secondFloor, thirdFloor);
	}

}
