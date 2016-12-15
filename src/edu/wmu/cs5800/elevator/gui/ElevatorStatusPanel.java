package edu.wmu.cs5800.elevator.gui;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wmu.cs5800.elevator.util.Constants;
import edu.wmu.cs5800.elevator.util.Helper;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc This panel will show the status of the elevator, Idle, Stopped,
 *       Working.
 */
public class ElevatorStatusPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 29311484627712387L;

	public static JLabel elevatorStatus;
	public static JLabel elevatorInputString;

	public ElevatorStatusPanel() {
		setLayout(new FlowLayout());
		elevatorInputString = new JLabel();
		elevatorStatus = new JLabel(Constants.DFA_STATUS_STOP);
		elevatorStatus.setFont(Helper.getPreferedFont());
		elevatorInputString.setFont(Helper.getPreferedFont());

		JLabel elevatorStatusLabel = new JLabel("Elevator Status: ");
		elevatorStatusLabel.setFont(Helper.getPreferedFont());
		add(elevatorStatusLabel);
		add(elevatorStatus);
		add(elevatorInputString);
	}
}
