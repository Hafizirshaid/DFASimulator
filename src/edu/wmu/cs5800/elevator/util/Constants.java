package edu.wmu.cs5800.elevator.util;

import java.awt.Color;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc this class contains general constants in DFA simulator.
 */
public class Constants {

	/**
	 * 
	 */
	public static final String FIRST_FLOOR = "1";

	/**
	 * 
	 */
	public static final String SECOND_FLOOR = "2";

	/**
	 * 
	 */
	public static final String THIRD_FLOOR = "3";
	public static final String GROUND_FLOOR = "0";
	public static final String FIRST_FLOOR_STATE = "FF";
	public static final String SECOND_FLOOR_STATE = "SF";
	public static final String THIRD_FLOOR_STATE = "TF";
	public static final String GROUND_FLOOR_STATE = "GF";
	public static final Color CURRENT_STATE_COLOR = Color.YELLOW;
	public static final Integer JFRAME_WIDTH = 1000;
	public static final Integer JFRAME_HEIGHT = 750;
	public static final Integer DFA_PANEL_WIDTH = 600;
	public static final Integer DFA_PANEL_HEIGHT = 350;

	public static final String DFA_STATUS_STOP = "Stopped";
	public static final String DFA_STATUS_WORKING = "Working";
	public static final String DFA_STATUS_IDLE = "Idle";
}
