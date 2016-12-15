package edu.wmu.cs5800.elevator.dfa.simulator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc This class contains the main method for DFA application
 */
public class DFAApp {

	/**
	 * Main Class name
	 */
	private static final String APPLICATION_NAME = DFAApp.class.getName();

	/**
	 * argument number in command line for dfa file name.
	 */
	private static final Integer DFA_FILE_ARGS_INDEX = 0;

	/**
	 * delimiter symbol between dfa file symbols
	 */
	private static final Integer DELIMITER_SYMBOL_ARGS_INDEX = 1;

	/**
	 * 
	 */
	private static DFASimulator dfaSimulator;

	/**
	 * 
	 * @return DFA simulator command line usage
	 */
	private static String getDFASimulatorUsage() {

		StringBuilder dfaUsage = new StringBuilder();
		dfaUsage.append("Usage \n");
		dfaUsage.append(APPLICATION_NAME + " <file-name> <file-delimiter> \n");
		return dfaUsage.toString();
	}

	/**
	 * this method will validate the input arguments from the user.
	 * 
	 * @param args
	 */
	private static void validateInputArguments(String args[]) {

		if (args == null || args.length != 2) {
			throw new InvalidParameterException("invalid Parameters");
		}

		String dfaFileName = args[DFA_FILE_ARGS_INDEX];
		if (dfaFileName == null || dfaFileName.trim().isEmpty()) {
			throw new InvalidParameterException("Invalid file name");
		}

		String delimiter = args[DELIMITER_SYMBOL_ARGS_INDEX];
		if (delimiter == null || delimiter.trim().isEmpty()) {
			throw new InvalidParameterException("Invalid delimiter symbol");
		}
	}

	/**
	 * 
	 * @param dfaFileName
	 *            the name of dfa file in the file system
	 * @return dfa file in string line by line
	 * @throws IOException
	 *             if the file does not exist
	 */
	private static List<String> getDFAFileLineByLineList(String dfaFileName) throws IOException {

		List<String> dfaFileLineByLineList = Files.readAllLines(Paths.get(dfaFileName), StandardCharsets.UTF_8);
		Iterator<String> dfaFileLineByLineListIterator = dfaFileLineByLineList.iterator();
		List<String> fileLineByLine = new ArrayList<String>();

		while (dfaFileLineByLineListIterator.hasNext()) {
			String line = dfaFileLineByLineListIterator.next();
			fileLineByLine.add(line);
		}

		return fileLineByLine;
	}

	/**
	 * 
	 * @return
	 */
	public static DFASimulator getDFASimulator() {
		return dfaSimulator;
	}

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		try {
			validateInputArguments(args);
			String dfaFileName = args[DFA_FILE_ARGS_INDEX];
			String delimiterSymbol = args[DELIMITER_SYMBOL_ARGS_INDEX];
			dfaSimulator = new DFASimulator(getDFAFileLineByLineList(dfaFileName), delimiterSymbol);
		} catch (InvalidParameterException ex) {
			System.out.println(ex.getMessage());
			System.out.println(getDFASimulatorUsage());
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println("File Does not exist " + ex.getMessage());
		}
	}
	
}
package edu.wmu.cs5800.elevator.dfa.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @date October 22, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc This class contains methods that will simulate a Deterministic finite
 *       atomate machine based on an input file.
 */
public class DFASimulator {

	/**
	 * line number of list of all states in the given DFA
	 */
	public final Integer LIST_OF_STATES_LINE_NUMBER = 0;

	/**
	 * line number of list of symbols as an input to each state
	 */
	public final Integer LIST_OF_SYMBOLS_LINE_NUMBER = 1;

	/**
	 * Start state line number
	 */
	public final Integer START_STATE_LINE_NUMBER = 2;

	/**
	 * list of final states
	 */
	public final Integer LIST_OF_FINAL_STATES_LINE_NUMBER = 3;

	/**
	 * the line number of the input string that the DFA will evaluate line
	 */
	public final Integer INPUT_STRING_LINE_NUMBER = 4;

	/**
	 * the line number of first row in the transition table, the rest of the
	 * file will contain the transition table
	 */
	public final Integer TRANSITION_TABLE_FIRST_LINE_NUMBER = 5;

	/**
	 * Hash map that represents the transition table.
	 */
	private Map<String, HashMap<String, String>> transitionTable = new HashMap<String, HashMap<String, String>>();

	/**
	 * list of input file each row contains a single line in the file
	 */
	private List<String> dfaFileLineByLineList;

	/**
	 * start state symbol
	 */
	public String startState;

	/**
	 * list of all possible DFA states
	 */
	private List<String> listOfStates;

	/**
	 * list of all sybmols that the DFA should accept
	 */
	private List<String> listOfSymboles;

	/**
	 * list of all final states
	 */
	private List<String> listOfFinalStates;

	/**
	 * the input string that the DFA will evaluate.
	 */
	private String inputString;

	/**
	 * Delimiter symbol
	 */
	public String lineDelimiterSymbol;

	/**
	 * table Of State Inequivalences for minimizing the given DFA
	 */
	private boolean tableOfStateInequivalences[][];

	/**
	 * 
	 */
	private List<HashSet<String>> newMinimizedListOfStates;

	public String getInputString() {
		return inputString;
	}

	public void setInputString(String inputString) {
		this.inputString = inputString;
	}

	/**
	 * 
	 * @param dfaFileLineByLineList
	 *            list of DFA file line by line
	 */
	public DFASimulator(List<String> dfaFileLineByLineList, String lineDelimiterSymbol) {
		this.lineDelimiterSymbol = lineDelimiterSymbol;
		this.dfaFileLineByLineList = dfaFileLineByLineList;
		populateTransitionTable();
	}

	/**
	 * this method will parse the DFA from the file and it will populate the
	 * transitionTable
	 */
	protected void populateTransitionTable() {

		List<String> fileLineByLine = new ArrayList<String>();
		Iterator<String> dfaFileLineByLineListIterator = dfaFileLineByLineList.iterator();

		while (dfaFileLineByLineListIterator.hasNext()) {
			String line = dfaFileLineByLineListIterator.next();
			fileLineByLine.add(line);
		}

		String states = fileLineByLine.get(LIST_OF_STATES_LINE_NUMBER);
		listOfStates = Arrays.asList(states.split(lineDelimiterSymbol));
		listOfSymboles = Arrays.asList(fileLineByLine.get(LIST_OF_SYMBOLS_LINE_NUMBER).split(lineDelimiterSymbol));
		startState = fileLineByLine.get(START_STATE_LINE_NUMBER);

		if (!listOfStates.contains(startState)) {
			throw new IllegalArgumentException("Invalid start state " + startState);
		}

		listOfFinalStates = Arrays
				.asList(fileLineByLine.get(LIST_OF_FINAL_STATES_LINE_NUMBER).split(lineDelimiterSymbol));

		Iterator<String> listOfFinalStatesIterator = listOfFinalStates.iterator();

		// Make sure all final states are in listOfStates.
		while (listOfFinalStatesIterator.hasNext()) {
			String finalState = listOfFinalStatesIterator.next();
			if (!listOfStates.contains(finalState)) {
				throw new IllegalArgumentException("Invalid final state " + finalState);
			}
		}

		inputString = fileLineByLine.get(INPUT_STRING_LINE_NUMBER);
		char[] inputStringCharArray = inputString.toCharArray();

		// Make sure that all symbols exists in the symbol's list
		for (int inputStringCharIndex = 0; inputStringCharIndex < inputStringCharArray.length; inputStringCharIndex++) {
			if (!listOfSymboles.contains(String.valueOf(inputStringCharArray[inputStringCharIndex]))) {
				throw new IllegalArgumentException("Invalid symbol " + inputStringCharArray[inputStringCharIndex]);
			}
		}

		for (int stateIndex = 0; stateIndex < listOfStates.size(); stateIndex++) {

			String line = fileLineByLine.get(stateIndex + TRANSITION_TABLE_FIRST_LINE_NUMBER);
			List<String> listOfTableStates = Arrays.asList(line.split(lineDelimiterSymbol));
			HashMap<String, String> transitionTableRowMap = new HashMap<String, String>();

			for (int symbolsIndex = 0; symbolsIndex < listOfSymboles.size(); symbolsIndex++) {

				String stateToAddToTransitionTable = null;
				if (listOfTableStates.size() > symbolsIndex && listOfTableStates.get(symbolsIndex) != null
						&& !listOfTableStates.get(symbolsIndex).trim().isEmpty()) {

					stateToAddToTransitionTable = listOfTableStates.get(symbolsIndex);

					if (!listOfStates.contains(stateToAddToTransitionTable)) {
						throw new IllegalArgumentException(
								"Invalid state in transition table " + stateToAddToTransitionTable);
					}
				}

				transitionTableRowMap.put(listOfSymboles.get(symbolsIndex), stateToAddToTransitionTable);
			}
			transitionTable.put(listOfStates.get(stateIndex), transitionTableRowMap);
		}
	}

	/**
	 * this method will return the next state for the given currentState and
	 * symbol.
	 * 
	 * @param currentState
	 *            current state
	 * @param symbol
	 *            current symbol
	 * @return next state
	 */
	public String getNextStateFromTransitionTable(String currentState, String symbol) {
		return transitionTable.get(currentState).get(symbol);
	}

	/**
	 * this method will trigger the DFA to start working on the input string.
	 */
	public void simlateDFA() {

		String currentState = new String(startState);
		for (int inputStringIndex = 0; inputStringIndex < inputString.length(); inputStringIndex++) {

			String currentSymbol = String.valueOf(inputString.charAt(inputStringIndex));
			String nextState = getNextStateFromTransitionTable(currentState, currentSymbol);

			if (nextState == null || nextState.trim().isEmpty()) {
				break;
			}

			System.out.println("|- [" + currentState + ", " + inputString.substring(inputStringIndex) + "]");
			currentState = nextState;
		}

		if (listOfFinalStates.contains(currentState)) {
			System.out.println("|- [" + currentState + ", lambda]");
			System.out.println("Accepted");
		} else {
			System.out.println("Rejected");
		}
	}

	/**
	 * This method will determine if the given state is final state or not
	 * 
	 * @param state
	 * @return
	 */
	public boolean isFinalState(String state) {
		return listOfFinalStates.contains(state);
	}

	/**
	 * This method will determine if s1 and s2 are Distinguishable
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public boolean areDistinguishable(String s1, String s2) {

		boolean areDistinguishable = false;
		if (s1.equals(s2)) {
			areDistinguishable = false;
		} else if ((isFinalState(s1) && !isFinalState(s2)) || (!isFinalState(s1) && isFinalState(s2))) {
			areDistinguishable = true;
		}
		return areDistinguishable;
	}

	/**
	 * this method will determine if s1 and s2 are Distinguishable for the next
	 * 'depth' states
	 * 
	 * @param s1
	 * @param s2
	 * @param depth
	 * @return
	 */
	public boolean areDistinguishable(String s1, String s2, int depth) {

		boolean areDistinguishable = false;
		Iterator<String> listOfSymbolesIterator = listOfSymboles.iterator();

		while (listOfSymbolesIterator.hasNext()) {

			String symbol = listOfSymbolesIterator.next();
			String s1NthNextState = this.getNthNextState(s1, symbol, depth);
			String s2NthNextState = this.getNthNextState(s2, symbol, depth);

			if (areDistinguishable(s1NthNextState, s2NthNextState)) {
				areDistinguishable = true;
				break;
			}
		}

		return areDistinguishable;
	}

	/**
	 * this method will determine the Nth next state by applying the given
	 * symbol
	 * 
	 * @param currentState
	 * @param symbol
	 * @param depth
	 * @return
	 */
	public String getNthNextState(String currentState, String symbol, int depth) {
		String nextState = currentState;

		if (depth > 0) {
			nextState = getNextStateFromTransitionTable(currentState, symbol);
			for (int i = 1; i < depth; i++) {
				nextState = this.getNextStateFromTransitionTable(nextState, symbol);
			}
		}
		return nextState;
	}

	/**
	 * this method will populate table of state Inequivalences
	 */
	public void constructTableOfStateInequivalences() {

		tableOfStateInequivalences = new boolean[listOfStates.size() - 1][listOfStates.size() - 1];

		for (int round = 0; round < listOfStates.size(); round++) {
			for (int i = 0; i < tableOfStateInequivalences.length; i++) {
				for (int j = i; j < tableOfStateInequivalences.length; j++) {

					if (!tableOfStateInequivalences[i][j]) {
						String horesantalState = listOfStates.get(i);
						String verticalState = listOfStates.get(j + 1);
						tableOfStateInequivalences[i][j] = areDistinguishable(horesantalState, verticalState, round);
					}
				}
			}
		}
	}

	/**
	 * This method will print Table Of State Inequivalences
	 */
	public void printTableOfStateInequivalences() {
		System.out.println("--------------");
		System.out.println("table Of State Inequivalences:");
		for (int i = 0; i < tableOfStateInequivalences.length; i++) {
			for (int j = i; j < tableOfStateInequivalences.length; j++) {
				String stringToPrint = "0";
				if (tableOfStateInequivalences[i][j]) {
					stringToPrint = "1";
				}
				System.out.print(stringToPrint + " ");
			}
			System.out.println();
		}
		System.out.println("--------------");
	}

	/**
	 * This method will compute the list of new states after minimization.
	 */
	public void determineNewMinimizedListOfStates() {

		this.newMinimizedListOfStates = new ArrayList<HashSet<String>>();
		int counter = 0;

		for (int i = 0; i < tableOfStateInequivalences.length + 1; i++) {

			String horisontalState = listOfStates.get(i);

			if (checkIfStringIsInsideListOfSets(newMinimizedListOfStates, horisontalState)) {
				continue;
			}

			HashSet<String> hashSet = new HashSet<>();
			hashSet.add(horisontalState);
			boolean breakOuterLoop = false;

			for (int j = i; j < tableOfStateInequivalences[0].length; j++) {
				if (!tableOfStateInequivalences[i][j]) {

					String verticalState = listOfStates.get(j + 1);
					HashSet<String> set = new HashSet<String>();
					set.add(horisontalState);
					set.add(verticalState);
					hashSet.addAll(set);
					if (counter >= listOfStates.size()) {
						breakOuterLoop = true;
						break;
					}
				}
			}

			counter += hashSet.size();
			newMinimizedListOfStates.add(hashSet);

			if (counter >= listOfStates.size() || breakOuterLoop) {
				break;
			}
		}
	}

	/**
	 * this method will get Equevenlent state In New Transition Table State From
	 * Old Transiton Table
	 * 
	 * @param oldState
	 * @return
	 */
	public String getEquevenlentInNewTransitionTableStateFromOldTransitonTable(String oldState) {

		String newState = null;
		Iterator<HashSet<String>> newMinimizedListOfStatesIterator = this.newMinimizedListOfStates.iterator();

		// loop thought all states and check if it contains
		while (newMinimizedListOfStatesIterator.hasNext()) {
			HashSet<String> current = newMinimizedListOfStatesIterator.next();
			if (current.contains(oldState)) {
				newState = current.toString();
				break;
			}
		}

		return newState;
	}

	/**
	 * getNewStateHashSetFromOldState
	 * 
	 * @param oldState
	 * @return
	 */
	public HashSet<String> getNewStateHashSetFromOldState(String oldState) {
		HashSet<String> newStateSet = null;
		Iterator<HashSet<String>> newMinimizedListOfStatesIterator = this.newMinimizedListOfStates.iterator();
		while (newMinimizedListOfStatesIterator.hasNext()) {
			HashSet<String> current = newMinimizedListOfStatesIterator.next();
			if (current.contains(oldState)) {
				newStateSet = current;
				break;
			}
		}
		return newStateSet;
	}

	/**
	 * this method will construct the new tranisition table.
	 */
	public void constructNewTransitionTable() {

		Map<String, HashMap<String, String>> newTransitionTable = new HashMap<String, HashMap<String, String>>();
		Iterator<HashSet<String>> it = newMinimizedListOfStates.iterator();

		while (it.hasNext()) {
			HashSet<String> current = it.next();

			String firstState = current.iterator().next();

			HashMap<String, String> currentTransition = new HashMap<String, String>();
			for (int i = 0; i < listOfSymboles.size(); i++) {
				currentTransition
						.put(listOfSymboles.get(i),
								getNewStateHashSetFromOldState(
										this.getNextStateFromTransitionTable(firstState, listOfSymboles.get(i)))
												.toString());
			}

			newTransitionTable.put(current.toString(), currentTransition);
		}

		Iterator<String> newTransitionTableIterator = newTransitionTable.keySet().iterator();
		System.out.println("--------------------\nNew Transition Table after minimization");
		while (newTransitionTableIterator.hasNext()) {
			String key = newTransitionTableIterator.next();
			System.out.println(key + " " + newTransitionTable.get(key));
		}

		this.transitionTable = newTransitionTable;

		// Construct final states
		List<String> listOfNewFinalStates = new ArrayList<String>();
		Iterator<String> listOfFinalStatesIterator = listOfFinalStates.iterator();
		while (listOfFinalStatesIterator.hasNext()) {
			String current = listOfFinalStatesIterator.next();

			HashSet<String> c = this.getNewStateHashSetFromOldState(current);
			if (!listOfNewFinalStates.contains(c.toString())) {
				listOfNewFinalStates.add(c.toString());
			}
		}

		this.listOfFinalStates = listOfNewFinalStates;
		System.out.println("List of Final States after minimization");
		System.out.println(listOfFinalStates);

		this.startState = this.getNewStateHashSetFromOldState(this.startState).toString();
		System.out.println("start State after minimization");
		System.out.println(startState);
		System.out.println("DFA computation after minimization");
		this.simlateDFA();
	}

	/**
	 * this method will minimize the DFA
	 */
	public void minimizeDFA() {

		constructTableOfStateInequivalences();
		printTableOfStateInequivalences();
		determineNewMinimizedListOfStates();
		constructNewTransitionTable();
	}

	/**
	 * this method will check If String Is Inside List Of Sets
	 * 
	 * @param sets
	 * @param elem
	 * @return
	 */
	public boolean checkIfStringIsInsideListOfSets(List<HashSet<String>> sets, String elem) {
		boolean inside = false;
		Iterator<HashSet<String>> i = sets.iterator();
		while (i.hasNext()) {
			HashSet<String> currentElm = i.next();
			if (currentElm.contains(elem)) {
				inside = true;
				break;
			}
		}
		return inside;
	}
}package edu.wmu.cs5800.elevator.gui.actionlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wmu.cs5800.elevator.dfa.simulator.DFAApp;
import edu.wmu.cs5800.elevator.dfa.simulator.DFASimulator;
import edu.wmu.cs5800.elevator.gui.DFAState;
import edu.wmu.cs5800.elevator.gui.ElevatorDFAPanel;
import edu.wmu.cs5800.elevator.gui.ElevatorKeybad;
import edu.wmu.cs5800.elevator.gui.ElevatorSimulator;
import edu.wmu.cs5800.elevator.gui.ElevatorSimulatorJFrame;
import edu.wmu.cs5800.elevator.gui.ElevatorStatusPanel;
import edu.wmu.cs5800.elevator.util.Constants;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc
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

		ElevatorKeybad.stopSimulation.setEnabled(true);
		ElevatorKeybad.startSimulationJButton.setEnabled(false);

		elevatorServerThread = new Thread(new Runnable() {

			/**
			 * 
			 */
			@Override
			public void run() {
				keepGoing = true;
				String initialState = Constants.GROUND_FLOOR_STATE;
				String[] dfaAppArgs = { "ElevatorDFA.txt", "," };
				DFAApp.main(dfaAppArgs);
				DFASimulator dfaSimulator = DFAApp.getDFASimulator();
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
	private void sleep(int sleepTime){
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
package edu.wmu.cs5800.elevator.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc 
 */
public class DFAState extends JPanel {

	private static final long serialVersionUID = -1399821507998481617L;

	/**
	 * 
	 */
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;
	private String label;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param label
	 */
	public DFAState(int x, int y, int width, int height, Color color, String label) {
		this.x = x;
		this.y = y;
		this.label = label;
		this.width = width;
		this.height = height;
		this.color = color;
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setBounds(x, y, width, height);
	}

	/**
	 * 
	 */
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setPaint(this.color);
		graphics2d.fillOval(0, 0, width, height);
		graphics2d.setPaint(Color.WHITE);
		graphics2d.setColor(Color.WHITE);
		graphics2d.drawString(this.label, this.getWidth() / 2, this.getHeight() / 2);
	}

	/**
	 * 
	 */
	public void setCurrentState() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		Color randomColor = new Color(r, g, b);
		this.setBackground(randomColor);
	}

	public void setNotCurrentState() {
		JButton j = new JButton();
		this.setBackground(j.getBackground());
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
package edu.wmu.cs5800.elevator.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.wmu.cs5800.elevator.util.Constants;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc 
 */
public class ElevatorDFAPanel extends JPanel {

	static DFAState groundFloor;
	static DFAState firstFloor;
	static DFAState secondFloor;
	static DFAState thirdFloor;

	private static final long serialVersionUID = -157415759141981757L;

	/**
	 * 
	 */
	public ElevatorDFAPanel() {
		int offset = 150;
		int endLeft = 600;
		int endDown = 600;
		int stateWidth = 100;
		int stateHeight = 100;
		groundFloor = new DFAState(offset, offset, stateWidth, stateHeight, Color.GREEN, Constants.GROUND_FLOOR_STATE);
		firstFloor = new DFAState(endLeft - offset, offset, stateWidth, stateHeight, Color.BLACK,
				Constants.FIRST_FLOOR_STATE);
		secondFloor = new DFAState(offset, endDown - offset, stateWidth, stateHeight, Color.RED,
				Constants.SECOND_FLOOR_STATE);
		thirdFloor = new DFAState(endLeft - offset, endDown - offset, stateWidth, stateHeight, Color.BLUE,
				Constants.THIRD_FLOOR_STATE);
		setLayout(null);
		setBorder(BorderFactory.createLineBorder(Color.black));
		add(groundFloor);
		add(firstFloor);
		add(secondFloor);
		add(thirdFloor);
		setSize(Constants.DFA_PANEL_WIDTH, Constants.DFA_PANEL_HEIGHT);
	}

	/**
	 * 
	 * @return
	 */
	public static DFAState getGroundFloor() {
		return groundFloor;
	}

	/**
	 * 
	 * @param groundFloor
	 */
	public static void setGroundFloor(DFAState groundFloor) {
		ElevatorDFAPanel.groundFloor = groundFloor;
	}

	public static DFAState getFirstFloor() {
		return firstFloor;
	}

	public static void setFirstFloor(DFAState firstFloor) {
		ElevatorDFAPanel.firstFloor = firstFloor;
	}

	public static DFAState getSecondFloor() {
		return secondFloor;
	}

	public static void setSecondFloor(DFAState secondFloor) {
		ElevatorDFAPanel.secondFloor = secondFloor;
	}

	public static DFAState getThirdFloor() {
		return thirdFloor;
	}

	public static void setThirdFloor(DFAState thirdFloor) {
		ElevatorDFAPanel.thirdFloor = thirdFloor;
	}

	public static void setAllDFAStatesBackground(Color color) {
		groundFloor.setBackground(color);
		firstFloor.setBackground(color);
		secondFloor.setBackground(color);
		thirdFloor.setBackground(color);
	}

	/**
	 * 
	 * @param g
	 * @param stateOne
	 * @param stateTwo
	 */
	protected void drawLineBetweenTwoStates(Graphics g, DFAState stateOne, DFAState stateTwo) {
		g.drawLine(stateOne.getX() + stateOne.getWidth() / 2, stateOne.getY() + stateOne.getHeight() / 2,
				stateTwo.getX() + stateTwo.getWidth() / 2, stateTwo.getY() + stateTwo.getHeight() / 2);
	}

	/**
	 * 
	 */
	public void paintComponent(Graphics g) {
		drawLineBetweenTwoStates(g, groundFloor, firstFloor);
		drawLineBetweenTwoStates(g, groundFloor, secondFloor);
		drawLineBetweenTwoStates(g, groundFloor, thirdFloor);
		drawLineBetweenTwoStates(g, firstFloor, secondFloor);
		drawLineBetweenTwoStates(g, firstFloor, thirdFloor);
		drawLineBetweenTwoStates(g, secondFloor, thirdFloor);
	}

}
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
 * @desc
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
 * @desc 
 */
public class ElevatorKeybad extends JPanel {

	private static final long serialVersionUID = 700456467295580710L;

	public static ElevatorKey groundFloorJButton;
	public static ElevatorKey firstFloorJButton;
	public static ElevatorKey secondFloorJButton;
	public static ElevatorKey thirdFloorJButton;
	public static JButton startSimulationJButton;
	public static JButton stopSimulation;

	public ElevatorKeybad() {

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
				ElevatorKeybad.stopSimulation.setEnabled(false);
				ElevatorKeybad.startSimulationJButton.setEnabled(true);
				ElevatorStatusPanel.elevatorStatus.setText(Constants.DFA_STATUS_STOP);
			}
		});
		stopSimulation.setEnabled(false);
		add(stopSimulation);
	}

}package edu.wmu.cs5800.elevator.gui;

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
 * @desc
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
package edu.wmu.cs5800.elevator.gui;

import java.awt.BorderLayout;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc
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
		ElevatorKeybad elevatorKeybad = new ElevatorKeybad();
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
 * @desc 
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

}package edu.wmu.cs5800.elevator.gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wmu.cs5800.elevator.gui.actionlistener.StartSimulationButtonActionListener;

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
package edu.wmu.cs5800.elevator.gui;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wmu.cs5800.elevator.util.Constants;
import edu.wmu.cs5800.elevator.util.Helper;

/**
 * 
 * @author hafiz
 *
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
package edu.wmu.cs5800.elevator.gui;

import javax.swing.SwingUtilities;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc
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
package edu.wmu.cs5800.elevator.util;

import java.awt.Color;

/**
 * @date Dec 01, 2016
 * @class CS5800 Theory of Computation
 * @author Hafez Irshaid
 * @version 1.0
 * @desc
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
package edu.wmu.cs5800.elevator.util;

import java.awt.Font;

public class Helper {

	public static Font getPreferedFont() {
		return new Font("", Font.PLAIN, 20);
	}
}
