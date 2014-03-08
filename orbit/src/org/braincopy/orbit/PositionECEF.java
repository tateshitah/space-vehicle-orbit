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
 * position object of ECEF (earth centered, earth fixed) coordinate system
 * 
 * @author Hiroaki Tateshita
 */
public class PositionECEF {

	/**
	 * x [m].
	 */
	public double x;

	/**
	 * y [m].
	 */
	public double y;

	/**
	 * z [m].
	 */
	public double z;

	/**
	 * 
	 */
	public PositionECEF() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * 
	 * @param x_
	 * @param y_
	 * @param z_
	 */
	public PositionECEF(final double x_, final double y_, final double z_) {
		this.x = x_;
		this.y = y_;
		this.z = z_;
	}

	/**
	 * This method is to convert from ECEF to LLH (latitude, longitude, height).
	 * 
	 * @return converted position from ECEF to LLH
	 */
	public final PositionLLH convertToLLH() {
		PositionLLH result = new PositionLLH(0, 0, -1
				* ConstantNumber.RADIUS_OF_EARTH);
		if (this.x == 0 && this.y == 0 && this.z == 0) {
			return result;
		}
		double longRadius = ConstantNumber.RADIUS_OF_EARTH;
		double shortRadius = longRadius
				* (1 - ConstantNumber.FlatteningOfEarth);
		double eccentricity = (longRadius * longRadius - shortRadius
				* shortRadius)
				/ (longRadius * longRadius);
		double eccentricityd = (longRadius * longRadius - shortRadius
				* shortRadius)
				/ (shortRadius * shortRadius);
		double t = Math.atan(z / Math.sqrt(x * x + y * y) * longRadius
				/ shortRadius);
		double latitude = Math.atan2(
				z + eccentricityd * shortRadius * Math.sin(t) * Math.sin(t)
						* Math.sin(t), Math.sqrt(x * x + y * y) - eccentricity
						* longRadius * Math.cos(t) * Math.cos(t) * Math.cos(t));
		result.latitude = latitude;
		double longitude = Math.atan2(y, x);
		result.longitude = longitude;
		double height = Math.sqrt(x * x + y * y)
				/ Math.cos(latitude)
				- longRadius
				/ Math.sqrt(1 - eccentricity * Math.sin(latitude)
						* Math.sin(latitude));
		result.height = height;
		return result;
	}

	@Override
	public final String toString() {
		String result = null;
		result = "(x[m], y[m], z[m]) = (" + x + ", " + y + ", " + z + ")";
		return result;
	}

	/**
	 * 
	 * @param args
	 *            arguments
	 */
	public static void main(final String[] args) {
		if (args.length == 3) {
			PositionECEF positionECEF = new PositionECEF(
					Double.parseDouble(args[0]), Double.parseDouble(args[1]),
					Double.parseDouble(args[2]));
			System.out.println(positionECEF.convertToLLH());
		} else {
			showUsage();
		}
	}

	/**
	 * 
	 */
	private static void showUsage() {
		System.out
				.println("usage: $ java -cp jar_file_name.jar PositionECEF x y z");
	}
}
