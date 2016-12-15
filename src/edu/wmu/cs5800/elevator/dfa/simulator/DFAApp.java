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
