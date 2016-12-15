package edu.wmu.cs5800.elevator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.wmu.cs5800.elevator.util.Helper;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc this class is elevator key which contains floor number to be clicked in
 *       the elevator keypad.
 */
public class ElevatorKey extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;

	private String keyId;
	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ElevatorKey(String keyId, String label) {
		addActionListener(this);
		setKeyId(keyId);
		setLabel(label);
		setText(label);
		setFont(Helper.getPreferedFont());
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ElevatorKey clickedKey = (ElevatorKey) e.getSource();
		ElevatorSimulator.floorsQueue.offer(clickedKey.getKeyId());
		ElevatorScreenPanel.elevatorScreenTextField
				.setText(ElevatorScreenPanel.elevatorScreenTextField.getText() + clickedKey.getKeyId());
	}

}
