package edu.wmu.cs5800.elevator.gui;

import java.awt.BorderLayout;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc this is the wrapper class for ElevatorSimulatorJFrame used to launch
 *       the window
 */
public class ElevatorSimulator {

	/**
	 * 
	 */
	private ElevatorSimulatorJFrame elevatorSimulatorJFrame;

	/**
	 * 
	 */
	public static Queue<String> floorsQueue = new ConcurrentLinkedQueue<String>();

	public void initElevatorSimulatorJFrame() {

		elevatorSimulatorJFrame = new ElevatorSimulatorJFrame();
		elevatorSimulatorJFrame.setLayout(new BorderLayout());
		ElevatorDFAPanel panel = new ElevatorDFAPanel();
		elevatorSimulatorJFrame.add(panel, BorderLayout.CENTER);
		ElevatorKeypad elevatorKeybad = new ElevatorKeypad();
		elevatorSimulatorJFrame.add(elevatorKeybad, BorderLayout.EAST);
		elevatorSimulatorJFrame.setVisible(true);
		ElevatorStatusPanel elevatorStatusPanel = new ElevatorStatusPanel();
		elevatorSimulatorJFrame.add(elevatorStatusPanel, BorderLayout.NORTH);
		elevatorSimulatorJFrame.add(new ElevatorSpeedJSlider(), BorderLayout.WEST);
		elevatorSimulatorJFrame.add(new ElevatorScreenPanel(), BorderLayout.SOUTH);
	}

	public void launch() {
		initElevatorSimulatorJFrame();
	}
}
