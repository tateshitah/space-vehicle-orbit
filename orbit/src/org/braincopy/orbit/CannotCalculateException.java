package org.braincopy.orbit;

public class CannotCalculateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CannotCalculateException(String string) {
		super(string);
	}

	public CannotCalculateException(int satelliteNumber) {
		super("more than 4 satellites are necessary but you have only: " + satelliteNumber);
	}

}
