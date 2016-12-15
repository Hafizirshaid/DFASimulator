package edu.wmu.cs5800.elevator.gui.actionlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wmu.cs5800.elevator.dfa.simulator.DFAApp;
import edu.wmu.cs5800.elevator.dfa.simulator.DFASimulator;
import edu.wmu.cs5800.elevator.gui.DFAState;
import edu.wmu.cs5800.elevator.gui.ElevatorDFAPanel;
import edu.wmu.cs5800.elevator.gui.ElevatorKeypad;
import edu.wmu.cs5800.elevator.gui.ElevatorSimulator;
import edu.wmu.cs5800.elevator.gui.ElevatorSimulatorJFrame;
import edu.wmu.cs5800.elevator.gui.ElevatorStatusPanel;
import edu.wmu.cs5800.elevator.util.Constants;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc this class contains the action listener used when start simulation
 *       button is clicked, also this class contains the consumer thread that
 *       will consume the data in the elevator queue
 */
public class StartSimulationButtonActionListener implements ActionListener {

	/**
	 * 
	 */
	public static Thread elevatorServerThread;

	/**
	 * 
	 */
	public static boolean keepGoing = true;

	/**
	 * 
	 */
	public static Integer transitionSpeed = 1000;

	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		startSimulation(e);
	}

	/**
	 * 
	 * @param e
	 */
	public void startSimulation(ActionEvent e) {

		ElevatorKeypad.stopSimulation.setEnabled(true);
		ElevatorKeypad.startSimulationJButton.setEnabled(false);

		elevatorServerThread = new Thread(new Runnable() {

			/**
			 * 
			 */
			@Override
			public void run() {
				String[] dfaAppArgs = { "ElevatorDFA.txt", "," };

				DFAApp.main(dfaAppArgs);
				DFASimulator dfaSimulator = DFAApp.getDFASimulator();
				keepGoing = true;
				String initialState = dfaSimulator.startState;

				String currentState = initialState;

				ElevatorDFAPanel.setAllDFAStatesBackground(ElevatorSimulatorJFrame.backgroundColor);
				getStatePanelFromStateNameString(currentState).setCurrentState();
				sleep(transitionSpeed);

				while (keepGoing) {

					if (!ElevatorSimulator.floorsQueue.isEmpty()) {
						ElevatorStatusPanel.elevatorStatus.setText(Constants.DFA_STATUS_WORKING);
						String currentSymbol = ElevatorSimulator.floorsQueue.poll();
						String nextState = dfaSimulator.getNextStateFromTransitionTable(currentState, currentSymbol);
						ElevatorDFAPanel.setAllDFAStatesBackground(ElevatorSimulatorJFrame.backgroundColor);
						getStatePanelFromStateNameString(nextState).setCurrentState();
					} else {
						ElevatorStatusPanel.elevatorStatus.setText(Constants.DFA_STATUS_IDLE);
					}
					sleep(transitionSpeed);
				}
			}
		});
		elevatorServerThread.start();
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public DFAState getStatePanelFromStateNameString(String name) {

		DFAState state = null;

		if (name.equals("GF")) {
			state = ElevatorDFAPanel.getGroundFloor();
		} else if (name.equals("FF")) {
			state = ElevatorDFAPanel.getFirstFloor();
		} else if (name.equals("SF")) {
			state = ElevatorDFAPanel.getSecondFloor();
		} else if (name.equals("TF")) {
			state = ElevatorDFAPanel.getThirdFloor();
		}

		return state;
	}

	/**
	 * 
	 * @param sleepTime
	 */
	private void sleep(int sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
