package edu.wmu.cs5800.elevator.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wmu.cs5800.elevator.gui.actionlistener.StartSimulationButtonActionListener;
import edu.wmu.cs5800.elevator.util.Constants;
import edu.wmu.cs5800.elevator.util.Helper;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc this class represents all keys in elevator keypad, floors as well as
 *       start and stop simulation buttons
 */
public class ElevatorKeypad extends JPanel {

	private static final long serialVersionUID = 700456467295580710L;

	public static ElevatorKey groundFloorJButton;
	public static ElevatorKey firstFloorJButton;
	public static ElevatorKey secondFloorJButton;
	public static ElevatorKey thirdFloorJButton;
	public static JButton startSimulationJButton;
	public static JButton stopSimulation;

	public ElevatorKeypad() {

		GridLayout experimentLayout = new GridLayout(0, 1);
		groundFloorJButton = new ElevatorKey(Constants.GROUND_FLOOR, "Ground Floor");
		firstFloorJButton = new ElevatorKey(Constants.FIRST_FLOOR, "First Floor");
		secondFloorJButton = new ElevatorKey(Constants.SECOND_FLOOR, "Second Floor");
		thirdFloorJButton = new ElevatorKey(Constants.THIRD_FLOOR, "Third Floor");
		setLayout(experimentLayout);
		add(groundFloorJButton);
		add(firstFloorJButton);
		add(secondFloorJButton);
		add(thirdFloorJButton);
		startSimulationJButton = new JButton("Start simulation");
		startSimulationJButton.addActionListener(new StartSimulationButtonActionListener());
		startSimulationJButton.setFont(Helper.getPreferedFont());
		add(startSimulationJButton);
		stopSimulation = new JButton("Stop simulation");
		stopSimulation.setFont(Helper.getPreferedFont());

		stopSimulation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StartSimulationButtonActionListener.keepGoing = false;
				ElevatorKeypad.stopSimulation.setEnabled(false);
				ElevatorKeypad.startSimulationJButton.setEnabled(true);
				ElevatorStatusPanel.elevatorStatus.setText(Constants.DFA_STATUS_STOP);
			}
		});
		stopSimulation.setEnabled(false);
		add(stopSimulation);
	}

}