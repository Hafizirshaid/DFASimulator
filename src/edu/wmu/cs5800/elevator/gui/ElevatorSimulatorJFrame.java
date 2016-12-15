package edu.wmu.cs5800.elevator.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import edu.wmu.cs5800.elevator.util.Constants;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc this JFrame will contain all UI components for Elevator Simulator DFA
 *       project.
 */
public class ElevatorSimulatorJFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static Color backgroundColor;

	public ElevatorSimulatorJFrame() {
		backgroundColor = getBackground();
		setTitle("DFA Elevator Simulator");
		setSize(Constants.JFRAME_WIDTH, Constants.JFRAME_HEIGHT);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

}
