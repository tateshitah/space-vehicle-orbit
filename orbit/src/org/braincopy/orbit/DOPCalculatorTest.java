package org.braincopy.orbit;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import sgp4v.ObjectDecayed;
import sgp4v.SatElsetException;

public class DOPCalculatorTest {

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
