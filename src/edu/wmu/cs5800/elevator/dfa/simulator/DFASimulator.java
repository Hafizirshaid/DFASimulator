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
 *       automata machine based on an input file.
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

}
