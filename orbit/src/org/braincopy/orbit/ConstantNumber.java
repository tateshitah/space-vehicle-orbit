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
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

package org.braincopy.orbit;

/**
 * 
 * @author Hiroaki Tateshita
 * @version 0.5
 */
public final class ConstantNumber {

	/**
	 * not used.
	 */
	private ConstantNumber() {
		super();
	}

	/**
	 * radius of the earth. [m]
	 */
	public static final double RADIUS_OF_EARTH = 6378137.0;

	/**
     * 
     */
	public static final double FlatteningOfEarth = 1 / 298.257223563;

	/**
     * 
     */
	public static final int SECONDS_WEEK = (int) (3600L * 24L * 7L);

	/**
	 * geocentric gravitational constant.
	 */
	public static final double GEO_GRAVI_CONST = 3.986005e14;

	/**
	 * what?
	 */
	public static final double dOMEGAe = 7.2921151467e-5;

	public static final int SECONDS_DAY = 3600 * 24;

	/**
	 * light speed. [m/s]
	 */
	static double C = 2.99792458e8;

}
