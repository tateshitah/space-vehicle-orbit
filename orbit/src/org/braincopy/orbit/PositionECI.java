/**

Copyright (c) 2014 Hiroaki Tateshita

Permission is hereby granted, free of charge, to any person obtaining a copy 
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
copies of the Software, and to permit persons to whom the Software is furnished
to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all 
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT 
OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */
package org.braincopy.orbit;

import java.util.Calendar;

/**
 * Position information of ECI (Earth-Centered Inertial) coodinate system.
 * 
 * @author Hiroaki Tateshita
 * 
 */
public class PositionECI {

	/**
	 * x [m].
	 */
	private double x; // [m]

	/**
	 * y [m].
	 */
	private double y; // [m]

	/**
	 * z [m].
	 */
	private double z; // [m]

	/**
	 * date and time of the epoch.
	 */
	private Calendar dateAndTime;

	/**
	 * FROM THE J2000 (noon Jan. 1st, 2000) EPOCH TO 1970 JAN 1 0H UT1 is
	 * -10957.5 days
	 */
	private static final double DAYS_BW_1970_J2000 = 10957.5;

	/**
	 * days of 100 years in a Julian century.
	 */
	private static final double DAYS_JULIAN_CENTURY = 36525;

	/**
	 * constant rate between solar time and sidereal time.
	 */
	private static final double RATE_BW_SOLAR_SIDEREAL_TIME = ConstantNumber.dOMEGAe
			* ConstantNumber.SECONDS_DAY / 2 / Math.PI;

	/**
	 * Constructor.
	 * 
	 * @param xIn
	 *            .
	 * @param yIn
	 *            .
	 * @param zIn
	 *            .
	 * @param dateAndTimeIn
	 *            .
	 */
	public PositionECI(final double xIn, final double yIn, final double zIn,
			final Calendar dateAndTimeIn) {
		this.x = xIn;
		this.y = yIn;
		this.z = zIn;
		this.dateAndTime = dateAndTimeIn;
	}

	/**
	 * 
	 * @return Earth Center Earth Fix ...
	 */
	public final PositionECEF convertToECEF() {

		PositionECEF result = null;

		/*
		 * GST is Greenwich Sidereal time (rad)
		 */
		double gst;

		gst = calcGST(this.dateAndTime);

		double xEcef = this.x * Math.cos(gst) + this.y * Math.sin(gst);
		double yEcef = -this.x * Math.sin(gst) + this.y * Math.cos(gst);

		result = new PositionECEF(xEcef, yEcef, this.z);
		return result;
	}

	/**
	 * Greenwich Sidereal Time.
	 * 
	 * @param dateAndTime
	 *            current time
	 * @return Greenwich Sidereal Time [rad]
	 */
	public static final double calcGST(final Calendar dateAndTime) {
		double result = -Double.MAX_VALUE;

		double daysFm1970;
		double secOfDay;
		double tu;
		double daysFmJ2000;
		double gst0, gst;

		/* INPUT IS TIME "secondsSince1970" IN SECONDS AND "TDAY" */
		/* WHICH IS WHOLE DAYS FROM 1970 JAN 1 0H */

		/* FOR COMPATABILITY */

		double secondsFm1970 = dateAndTime.getTimeInMillis() / 1000;
		daysFm1970 = (double) ((int) (secondsFm1970 / ConstantNumber.SECONDS_DAY));
		secOfDay = secondsFm1970 - daysFm1970 * ConstantNumber.SECONDS_DAY;

		/* THE NUMBER OF DAYS FROM THE J2000 EPOCH */
		/* TO 1970 JAN 1 0H UT1 IS -10957.5 */

		daysFmJ2000 = daysFm1970 - DAYS_BW_1970_J2000;
		/*
		 * tu is time interval in Julian Century (100 years).
		 */
		tu = daysFmJ2000 / DAYS_JULIAN_CENTURY;

		gst0 = 24110.54841 + 8640184.812866 * tu + 0.093104 * tu * tu
				- (6.2e-6 * tu * tu * tu);//
		gst = gst0 + RATE_BW_SOLAR_SIDEREAL_TIME * secOfDay;

		gst = gst % ConstantNumber.SECONDS_DAY;
		if (gst < 0) {
			gst += ConstantNumber.SECONDS_DAY;
		}

		result = gst / ConstantNumber.SECONDS_DAY * 2 * Math.PI;

		return result;
	}
}
