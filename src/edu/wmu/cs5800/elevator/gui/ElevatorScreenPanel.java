package edu.wmu.cs5800.elevator.gui;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.wmu.cs5800.elevator.util.Helper;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc This Panel will contain the floor numbers that are being served by the
 *       elevator.
 */
public class ElevatorScreenPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public static JLabel elevatorScreen = new JLabel();

	/**
	 * 
	 */
	public static JTextField elevatorScreenTextField = new JTextField();

	public ElevatorScreenPanel() {
		elevatorScreen.setText("Floors:");
		elevatorScreen.setFont(Helper.getPreferedFont());
		add(elevatorScreen);
		elevatorScreenTextField.setEditable(false);
		elevatorScreenTextField.setColumns(10);
		elevatorScreenTextField.setFont(Helper.getPreferedFont());

		add(elevatorScreenTextField);
		setLayout(new FlowLayout());
	}
}
