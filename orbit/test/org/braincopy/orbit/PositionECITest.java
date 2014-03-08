package org.braincopy.orbit;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class PositionECITest {

	@Test
	public void testConvertToECEF() {
		/*
		 * julianDay = 2453937.82777778;
		 */
		GregorianCalendar dateAndTime = new GregorianCalendar(
				TimeZone.getTimeZone("UTC"));
		dateAndTime.set(2006, 6, 21, 7, 52, 0);
		PositionECI inputEci = new PositionECI(-2392.11241452386,
				-17078.1233608647, 19945.4195944851, dateAndTime);
		PositionECEF outputECEF = inputEci.convertToECEF();
		System.out
				.println(outputECEF
						+ " should be [-15615.6915464865 -7322.85945823539 19943.173494021]");
		/*
		 * rECI = [-2392.11241452386 -17078.1233608647 19945.4195944851];
		 * 
		 * t = 2453937.82777778;7:52:0.00 UT on July 21, 2006 .
		 * 
		 * Will yield an ECEF position and velocity of
		 * 
		 * rECEF = [-15615.6915464865 -7322.85945823539 19943.173494021]; }
		 */
	}

	@Test
	public void testCalcGST() {
		Calendar in = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		in.set(2012, 4, 31, 2, 57, 31);
		double gst = PositionECI.calcGST(in);
		System.out.println(gst / Math.PI * 86400 + " should be "
				+ (1107214.47173 % 86400));
	}
}
