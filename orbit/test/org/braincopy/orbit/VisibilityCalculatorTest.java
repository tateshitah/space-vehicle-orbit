package org.braincopy.orbit;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import sgp4v.ObjectDecayed;
import sgp4v.SatElsetException;

public class VisibilityCalculatorTest {

	@Test
	public void test() {
		VisibilityCalculator calculator = new VisibilityCalculator();
		ArrayList<TLEString> tleList = new ArrayList<TLEString>();
		TLEString qzss1TLEString = new TLEString();
		qzss1TLEString.setLine1("1 38337U 12025A   14101.92104084  .00000852  00000-0  19923-3 0  7438");
		qzss1TLEString.setLine2("2 38337 098.2062 042.9966 0000801 073.7798 286.3479 14.57092085101113");
		tleList.add(qzss1TLEString);

		TLEString qzss2TLEString = new TLEString();
		qzss2TLEString.setLine1("1 98337U 12025A   14101.92104084  .00000852  00000-0  19923-3 0  7434");
		qzss2TLEString.setLine2("2 98337 098.2062 042.9966 0000801 073.7798 286.3479 14.57092085101119");
		tleList.add(qzss2TLEString);

		TLEString qzss3TLEString = new TLEString();
		qzss3TLEString.setLine1("1 38337U 12025A   14101.92104084  .00000852  00000-0  19923-3 0  7438");
		qzss3TLEString.setLine2("2 38337 098.2062 042.9966 0000801 073.7798 286.3479 14.57092085101113");
		tleList.add(qzss3TLEString);

		TLEString qzss4TLEString = new TLEString();
		qzss4TLEString.setLine1("1 98337U 12025A   14101.92104084  .00000852  00000-0  19923-3 0  7434");
		qzss4TLEString.setLine2("2 98337 098.2062 042.9966 0000801 073.7798 286.3479 14.57092085101119");
		tleList.add(qzss4TLEString);

		try {
			calculator.outputCalcResult(tleList);
		} catch (ObjectDecayed e) {
			System.err.println("something happens: " + e.getLocalizedMessage());
			fail();
			e.printStackTrace();
		} catch (SatElsetException e) {
			System.err.println("something happens: " + e.getLocalizedMessage());
			fail();
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("something happens: " + e.getLocalizedMessage());
			fail();
			e.printStackTrace();
		}

	}

}
