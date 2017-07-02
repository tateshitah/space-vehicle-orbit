/**
 
Copyright (c) 2014-2017 Hiroaki Tateshita

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
 * This class express relative position from base position to target position.
 * You can use this class when you want azimuth and elevation of target things
 * like satellite from your position.
 * 
 * @author Hiroaki Tateshita
 */
public class PositionENU {

	public double east;// [m]
	public double north;// [m]
	public double up;// [m]

	/**
	 * 
	 * @param east
	 *            [m]
	 * @param north
	 *            [m]
	 * @param up
	 *            [m]
	 */
	public PositionENU(double east, double north, double up) {
		this.east = east;
		this.north = north;
		this.up = up;
	}

	/**
	 * 
	 * @param position
	 * @param base
	 * @return
	 */
	public static PositionENU convertToENU(PositionECEF position, PositionECEF base) {
		PositionENU result = null;
		PositionECEF relativePosition = new PositionECEF(position.x - base.x, position.y - base.y, position.z - base.z);
		PositionLLH baseLLH = base.convertToLLH();
		double east = -relativePosition.x * Math.sin(baseLLH.longitude)
				+ relativePosition.y * Math.cos(baseLLH.longitude);

		double north = -relativePosition.x * Math.cos(baseLLH.longitude) * Math.sin(baseLLH.latitude)
				- relativePosition.y * Math.sin(baseLLH.longitude) * Math.sin(baseLLH.latitude)
				+ relativePosition.z * Math.cos(baseLLH.latitude);

		double up = relativePosition.x * Math.cos(baseLLH.longitude) * Math.cos(baseLLH.latitude)
				+ relativePosition.y * Math.sin(baseLLH.longitude) * Math.cos(baseLLH.latitude)
				+ relativePosition.z * Math.sin(baseLLH.latitude);
		result = new PositionENU(east, north, up);
		return result;
	}

	/**
	 * 
	 * @return [rad]
	 */
	public double getElevation() {
		return Math.atan(up / Math.sqrt(north * north + east * east));
	}

	/**
	 * 
	 * @return azimuth [rad]
	 */
	public double getAzimuth() {
		return Math.atan2(north, east);
	}

	@Override
	public String toString() {
		String result = null;
		result = "(east[m], north[m], up[m]) = (" + this.east + ", " + this.north + ", " + this.up + ")";
		return result;
	}

	/**
	 * 
	 * @param base
	 * @return
	 */
	public PositionECEF convertToECEF(PositionECEF base) {

		double s1, c1, s2, c2;
		PositionLLH blh;
		PositionECEF pos = new PositionECEF();

		/* */
		blh = base.convertToLLH();// xyz_to_blh(base);
		s1 = Math.sin(blh.longitude);// lon);
		c1 = Math.cos(blh.longitude);// lon);
		s2 = Math.sin(blh.latitude);// lat);
		c2 = Math.cos(blh.latitude);// lat);

		/* ENU */
		pos.x = -this.east * s1 - this.north * c1 * s2 + this.up * c1 * c2;
		pos.y = this.east * c1 - this.north * s1 * s2 + this.up * s1 * c2;
		pos.z = this.north * c2 + this.up * s2;

		/**/
		pos.x += base.x;
		pos.y += base.y;
		pos.z += base.z;

		return pos;
	}
}
