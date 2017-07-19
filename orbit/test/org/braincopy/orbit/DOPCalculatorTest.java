package org.braincopy.orbit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Vector;

import org.junit.Test;

import sgp4v.ObjectDecayed;
import sgp4v.SatElsetException;

public class DOPCalculatorTest {

	@Test
	public void testCalcHDOP() {
		DOPCalculator calc = new DOPCalculator();

		PositionLLH currentPosllh = new PositionLLH(0, 0, 0);

		Vector<PositionECEF> satellitesPosList = new Vector<PositionECEF>();

		PositionENU posENU_Zenith = new PositionENU(0, 0, 35000000);
		PositionECEF posECEF_Zenith = posENU_Zenith.convertToECEF(currentPosllh.convertToECEF());
		satellitesPosList.add(posECEF_Zenith);

		PositionENU posENU_North = new PositionENU(0, 35000000 * Math.cos(30.0 * Math.PI / 180.0),
				35000000 * Math.sin(30.0 * Math.PI / 180.0));
		PositionECEF posECEF_North = posENU_North.convertToECEF(currentPosllh.convertToECEF());
		satellitesPosList.add(posECEF_North);

		PositionENU posENU_East = new PositionENU(35000000 * Math.cos(30.0 * Math.PI / 180.0), 0,
				35000000 * Math.sin(30.0 * Math.PI / 180.0));
		PositionECEF posECEF_East = posENU_East.convertToECEF(currentPosllh.convertToECEF());
		satellitesPosList.add(posECEF_East);

		PositionENU posENU_West = new PositionENU(-35000000 * Math.cos(30.0 * Math.PI / 180.0), 0,
				35000000 * Math.sin(30.0 * Math.PI / 180.0));
		PositionECEF posECEF_West = posENU_West.convertToECEF(currentPosllh.convertToECEF());
		satellitesPosList.add(posECEF_West);

		try {
			double hdop = calc.calcHDOP(currentPosllh, satellitesPosList, 15.0);
			System.out.println("hdop = " + hdop);
			assertEquals(1.6329, hdop, 0.1);
		} catch (CannotInverseException e) {
			fail("something happens. " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (CannotCalculateException e) {
			fail("something happens. " + e.getLocalizedMessage());
			e.printStackTrace();
		}

		satellitesPosList.clear();
		currentPosllh = new PositionLLH(35.683333 / 180 * Math.PI, 139.683333 / 180 * Math.PI, 5.0);// Tokyo

		double azimuth1 = 110.0 / 180.0 * Math.PI;// [rad]
		double elevation1 = 85.0 / 180.0 * Math.PI;// [rad]
		PositionENU posENU1 = new PositionENU(35000000 * Math.cos(azimuth1) * Math.cos(elevation1),
				35000000 * Math.sin(azimuth1) * Math.cos(elevation1), 35000000 * Math.sin(elevation1));
		PositionECEF posECEF1 = posENU1.convertToECEF(currentPosllh.convertToECEF());
		satellitesPosList.add(posECEF1);

		double azimuth2 = 255.0 / 180.0 * Math.PI;// [rad]
		double elevation2 = 25.0 / 180.0 * Math.PI;// [rad]
		PositionENU posENU2 = new PositionENU(35000000 * Math.cos(azimuth2) * Math.cos(elevation2),
				35000000 * Math.sin(azimuth2) * Math.cos(elevation2), 35000000 * Math.sin(elevation2));
		PositionECEF posECEF2 = posENU2.convertToECEF(currentPosllh.convertToECEF());
		satellitesPosList.add(posECEF2);

		double azimuth3 = 285.0 / 180.0 * Math.PI;// [rad]
		double elevation3 = 40.0 / 180.0 * Math.PI;// [rad]
		PositionENU posENU3 = new PositionENU(35000000 * Math.cos(azimuth3) * Math.cos(elevation3),
				35000000 * Math.sin(azimuth3) * Math.cos(elevation3), 35000000 * Math.sin(elevation3));
		PositionECEF posECEF3 = posENU3.convertToECEF(currentPosllh.convertToECEF());
		satellitesPosList.add(posECEF3);

		double azimuth4 = 250.0 / 180.0 * Math.PI;// [rad]
		double elevation4 = 45.0 / 180.0 * Math.PI;// [rad]
		PositionENU posENU4 = new PositionENU(35000000 * Math.cos(azimuth4) * Math.cos(elevation4),
				35000000 * Math.sin(azimuth4) * Math.cos(elevation4), 35000000 * Math.sin(elevation4));
		PositionECEF posECEF4 = posENU4.convertToECEF(currentPosllh.convertToECEF());

		try {
			calc.calcHDOP(currentPosllh, satellitesPosList, 15.0);
		} catch (CannotInverseException e) {
			fail("something happens. " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (CannotCalculateException e) {
			System.out.println("CannotCalculateException detected normally. " + e.getMessage());
		}

		satellitesPosList.add(posECEF4);

		try {
			double hdop = calc.calcHDOP(currentPosllh, satellitesPosList, 15.0);
			System.out.println("hdop = " + hdop);
			assertEquals(5.1283, hdop, 0.1);
		} catch (CannotInverseException e) {
			fail("something happens. " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (CannotCalculateException e) {
			fail("something happens. " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testOutputCalcResult() {
		DOPCalculator calc = new DOPCalculator();
		try {
			ArrayList<TLEString> tleList = new ArrayList<TLEString>();
			TLEString qzss1TLEString = new TLEString();
			qzss1TLEString.setLine1("1 37158U 10045A   17136.38279348 -.00000102  00000-0  00000-0 0  9990");
			qzss1TLEString.setLine2("2 37158  40.8296 159.7841 0751802 270.0854  78.4139  1.00288962 24430");
			tleList.add(qzss1TLEString);
			calc.outputCalcResult(tleList);
		} catch (ObjectDecayed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SatElsetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotInverseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCalculateException e) {
			System.out.println("normally detected: " + e.getMessage());
		}
	}

	@Test
	public void testInverse_matrix() {
		double[][] a = { { 1, 0, 1, 2 }, { -1, 1, 1, 1 }, { 1, -1, 0, 1 }, { 1, 1, -1, 2 } };

		DOPCalculator calc = new DOPCalculator();
		int m = 4;
		for (int i = 0; i < 4; i++) {
			System.out.println(a[i][0] + " " + a[i][1] + " " + a[i][2] + " " + a[i][3]);
		}
		try {
			calc.inverse_matrix(a, m);
		} catch (CannotInverseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 4; i++) {
			System.out.println(a[i][0] + " " + a[i][1] + " " + a[i][2] + " " + a[i][3]);
		}
		assertEquals(1.0, a[0][0], 0.0001);
		assertEquals(-1.0, a[0][2], 0.0001);
		double[][] b = { { 3, 2, 1 }, { 1, 3, 1 }, { 2, 2, 1 } };
		for (int i = 0; i < 3; i++) {
			System.out.println(b[i][0] + " " + b[i][1] + " " + b[i][2]);
		}
		m = 3;
		try {
			calc.inverse_matrix(b, m);
		} catch (CannotInverseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 3; i++) {
			System.out.println(b[i][0] + " " + b[i][1] + " " + b[i][2]);
		}
		assertEquals(1.0, b[0][0], 0.0001);
		assertEquals(-1.0, b[0][2], 0.0001);

		double[][] c = { { 1, 2 }, { 3, 4 } };
		m = 2;
		try {
			calc.inverse_matrix(c, m);
		} catch (CannotInverseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(-2.0, c[0][0], 0.00001);
	}

}
