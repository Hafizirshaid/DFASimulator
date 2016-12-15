package edu.wmu.cs5800.elevator.gui;

import javax.swing.SwingUtilities;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc this is the main class that will launch the elevator simulator.
 */
public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ElevatorSimulator elevatorSimulator = new ElevatorSimulator();
				elevatorSimulator.launch();
			}
		});
	}
}
