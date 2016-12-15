package edu.wmu.cs5800.elevator.gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wmu.cs5800.elevator.gui.actionlistener.StartSimulationButtonActionListener;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc this JSlider will be used to control sleep time for Elevator simulator
 *       thread.
 */
public class ElevatorSpeedJSlider extends JSlider implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ElevatorSpeedJSlider() {
		super(JSlider.VERTICAL, 0, 3000, 1000);
		setPaintTicks(true);
		setLabelTable(createStandardLabels(1));
		addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		ElevatorSpeedJSlider elevatorSpeedJSlider = (ElevatorSpeedJSlider) e.getSource();
		StartSimulationButtonActionListener.transitionSpeed = elevatorSpeedJSlider.getValue();
	}

}
