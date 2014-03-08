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

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION 
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

package org.braincopy.orbit;

/**
 * This class express the position information with latitude, longitude and
 * height.
 * 
 * @author Hiroaki Tateshita
 */
public class PositionLLH {

	/**
	 * [rad].
	 */
	double latitude;

	/**
	 * [rad].
	 */
	double longitude;

	/**
	 * [m].
	 */
	double height;

	/**
	 * 
	 * @param latitude_
	 *            [rad]
	 * @param longitude_
	 *            [rad]
	 * @param height_
	 *            [m]
	 */
	public PositionLLH(double latitude_, double longitude_, double height_) {
		this.latitude = latitude_;
		this.longitude = longitude_;
		this.height = height_;
	}

	/**
	 * 
	 * @param radian
	 * @return
	 */
	public static double showDegree(double radian) {
		double result = -1000000;
		result = radian / Math.PI * 180;
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public PositionECEF convertToECEF() {
		PositionECEF result = null;
		double longRadius = ConstantNumber.RADIUS_OF_EARTH;
		double shortRadius = longRadius
				* (1 - ConstantNumber.FlatteningOfEarth);
		double eccentricity = (longRadius * longRadius - shortRadius
				* shortRadius)
				/ (longRadius * longRadius);

		double n = longRadius
				/ Math.sqrt(1 - eccentricity * Math.sin(this.latitude)
						* Math.sin(this.latitude));
		double x = (n + this.height) * Math.cos(this.latitude)
				* Math.cos(this.longitude);
		double y = (n + this.height) * Math.cos(this.latitude)
				* Math.sin(this.longitude);
		double z = (n * (1.0 - eccentricity) + this.height)
				* Math.sin(this.latitude);

		result = new PositionECEF(x, y, z);
		return result;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		String result = null;
		result = "(latitude, longtitude, height) = ("
				+ PositionLLH.transferDtoDMS(latitude) + ", "
				+ PositionLLH.transferDtoDMS(longitude) + ", " + height + ")";
		return result;
	}

	/**
	 * transfer from radian to Degree, minutes, seconds
	 * 
	 * @param radian
	 * @return Degree, minutes, seconds
	 */
	public static String transferDtoDMS(double radian) {
		String result = null;
		double degree = radian / Math.PI * 180;
		int intD = (int) degree;
		double decimals = degree - intD;
		int intMinute = (int) (decimals * 60);
		double decimalsMinute = decimals * 60 - intMinute;
		double second = decimalsMinute * 60;
		result = intD + "d" + intMinute + "'" + second + "\"";
		return result;
	}

	/**
	 * get Latitude [rad]
	 * 
	 * @return Latitude [rad]
	 */
	public double getLat() {
		return latitude;
	}

	/**
	 * get Longitude [rad]
	 * 
	 * @return Longitude [rad]
	 */
	public double getLon() {
		return longitude;
	}

	/**
	 * get Height [m]
	 * 
	 * @return Height [m]
	 */
	public double getHeight() {
		return height;
	}

}
