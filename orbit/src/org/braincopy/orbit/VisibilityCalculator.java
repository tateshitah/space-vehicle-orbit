/**
 
Copyright (c) 2017 Hiroaki Tateshita

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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import org.braincopy.kml.KMLCreator;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
//import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import sgp4v.ObjectDecayed;
import sgp4v.SatElsetException;
import sgp4v.Sgp4Data;
import sgp4v.Sgp4Unit;

/**
 * 
 * @author Hiroaki Tateshita
 *
 */
public class VisibilityCalculator {

	/**
	 * 
	 * output is KML file
	 * 
	 * @throws SatElsetException
	 * @throws ObjectDecayed
	 * @throws FileNotFoundException
	 */
	public void outputCalcResult(ArrayList<TLEString> tleList)
			throws ObjectDecayed, SatElsetException, FileNotFoundException {

		Sgp4Unit sgp4 = new Sgp4Unit();
		int startYear, stopYear, step;
		double startDay, stopDay;

		Calendar startDate = new GregorianCalendar(2017, 5 - 1, 15, 9, 0, 0);
		startYear = startDate.get(Calendar.YEAR);
		startDay = startDate.get(GregorianCalendar.DAY_OF_YEAR);
		Calendar stopDate = new GregorianCalendar(2017, 5 - 1, 16, 9, 0, 0);
		stopYear = stopDate.get(Calendar.YEAR);
		stopDay = stopDate.get(GregorianCalendar.DAY_OF_YEAR);
		step = 60;// 60 [minutes] = 1 hour

		Vector<Sgp4Data> results;
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");

		Iterator<TLEString> ite = tleList.iterator();
		ArrayList<Vector<Sgp4Data>> resultsList = new ArrayList<Vector<Sgp4Data>>(tleList.size());
		while (ite.hasNext()) {
			TLEString tle = (TLEString) ite.next();
			results = sgp4.runSgp4(tle.getLine1(), tle.getLine2(), startYear, startDay, stopYear, stopDay, step);// step's
			resultsList.add(results);
			/*
			 * PositionECI posEci = null; PositionLLH posLlh = null;
			 * GregorianCalendar dateAndTime = null; double days = 0; Sgp4Data
			 * data = null; for (int i = 0; i < results.size(); i++) { data =
			 * (Sgp4Data) results.elementAt(i); days = startDay + i * (double)
			 * step * 60 / (double) ConstantNumber.SECONDS_DAY; dateAndTime =
			 * Main.getCalendarFmYearAndDays(startYear, days); // posEci = new
			 * PositionECI(data.getX() * // ConstantNumber.RADIUS_OF_EARTH, //
			 * data.getY() * ConstantNumber.RADIUS_OF_EARTH, data.getZ() * //
			 * ConstantNumber.RADIUS_OF_EARTH, // dateAndTime); // posLlh =
			 * posEci.convertToECEF().convertToLLH(); //
			 * System.out.println(sdf.format(dateAndTime.getTime()) + "\t" + //
			 * posLlh.getLat() * 180 / Math.PI + "\t" // + posLlh.getLon() * 180
			 * / Math.PI + "\t" + // posLlh.getHeight()); }
			 */
		}
		Kml kml = new Kml();
		KMLCreator kmlCreator = new KMLCreator(kml);

		double mesh = 1.0;// [degree]]
		PositionLLH currentPosllh = null;
		double percentageOverCertainEl = 0;
		for (int i = 0; i < 360 / mesh; i++) {
			for (int j = 0; j < 180 / mesh; j++) {
				currentPosllh = new PositionLLH((j * mesh - 90.0) / 180.0 * Math.PI,
						(i * mesh - 180.0) / 180.0 * Math.PI, 0.0);
				percentageOverCertainEl = calcpercentageOverCertainEl(currentPosllh, resultsList, 15.0, startDay,
						startYear, step);
				if (percentageOverCertainEl == 100.0) {
					percentageOverCertainEl = 99.0;
				}
				System.out.println((i * mesh - 180.0) + ", " + (j * mesh - 90.0) + " :" + percentageOverCertainEl);
				kmlCreator.createUnitPlacemark(new Coordinate(i * mesh - 180, j * mesh - 90, 30), (int) mesh,
						"polystyle" + ((int) percentageOverCertainEl * 16 / 100));
			}
		}
		String kmlFileName = "testdata/visibility.kml";
		kml.marshal(new File(kmlFileName));
		System.out.println("completed: " + kmlFileName);
	}

	/**
	 * 
	 * @param currentPosllh
	 * @param resultsList
	 * @param elevationMask
	 * @param startDay
	 * @param startYear
	 * @param step
	 * @return
	 */
	private double calcpercentageOverCertainEl(PositionLLH currentPosllh, ArrayList<Vector<Sgp4Data>> resultsList,
			double elevationMask, double startDay, int startYear, int step) {
		double result = 0.0;
		double[] minimumElevationArray = new double[24 * 60 / step + 1];
		for (int i = 0; i < minimumElevationArray.length; i++) {
			minimumElevationArray[i] = 90.0;
		}
		Iterator<Vector<Sgp4Data>> resultIte = resultsList.iterator();
		Sgp4Data data = null;
		double days = 0.0;

		GregorianCalendar dateAndTime = null;
		PositionECI posEci = null;
		while (resultIte.hasNext()) {
			Vector<Sgp4Data> resultEachTle = resultIte.next();
			Iterator<Sgp4Data> sgp4resultIte = resultEachTle.iterator();
			int i = 0;
			double currentElevation = 0;
			while (sgp4resultIte.hasNext()) {
				data = sgp4resultIte.next();
				days = startDay + i * (double) step * 60 / (double) ConstantNumber.SECONDS_DAY;
				dateAndTime = Main.getCalendarFmYearAndDays(startYear, days);
				posEci = new PositionECI(data.getX() * ConstantNumber.RADIUS_OF_EARTH,
						data.getY() * ConstantNumber.RADIUS_OF_EARTH, data.getZ() * ConstantNumber.RADIUS_OF_EARTH,
						dateAndTime);
				currentElevation = PositionENU.convertToENU(posEci.convertToECEF(), currentPosllh.convertToECEF())
						.getElevation();
				// System.out.println("Test only: current ele=" +
				// currentElevation);
				if (minimumElevationArray[i] > currentElevation) {
					minimumElevationArray[i] = currentElevation;
				}
				i++;
			}
		}

		int count = 0;
		for (int i = 0; i < minimumElevationArray.length; i++) {
			if (minimumElevationArray[i] > elevationMask / 180.0 * Math.PI) {
				count++;
			}
		}
		result = (double) count / (double) minimumElevationArray.length * 100;
		return result;
	}

	public static void main(String[] args) {
		VisibilityCalculator calculator = new VisibilityCalculator();
		ArrayList<TLEString> tleList = new ArrayList<TLEString>();
		TLEString qzss1TLEString = new TLEString();
		qzss1TLEString.setLine1("1 37158U 10045A   17136.38279348 -.00000102  00000-0  00000-0 0  9990");
		qzss1TLEString.setLine2("2 37158  40.8296 159.7841 0751802 270.0854  78.4139  1.00288962 24430");
		tleList.add(qzss1TLEString);

		TLEString qzss2TLEString = new TLEString();
		qzss2TLEString.setLine1("1 28937U 06004A   17134.86515212 -.00000246  00000-0  00000-0 0  9998");
		qzss2TLEString.setLine2("2 28937   0.0182 273.9213 0003329 139.8575 275.4434  1.00268614 41051");
		tleList.add(qzss2TLEString);

		TLEString qzss3TLEString = new TLEString();
		qzss3TLEString.setLine1("1 26863U 01029A   17134.59080891 -.00000370  00000-0  00000-0 0  9994");
		qzss3TLEString.setLine2("2 26863  12.6979  33.4115 0003004  52.2354 122.3690  1.00265126 59107");
		tleList.add(qzss3TLEString);

		TLEString qzss4TLEString = new TLEString();
		qzss4TLEString.setLine1("1 39727U 14023A   17134.84979927 -.00000273  00000-0  00000-0 0  9992");
		qzss4TLEString.setLine2("2 39727   2.7709 299.0786 0002553 118.1671 216.2258  1.00272084529226");
		tleList.add(qzss4TLEString);

		try {
			calculator.outputCalcResult(tleList);
		} catch (ObjectDecayed e) {
			System.err.println("something happens: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (SatElsetException e) {
			System.err.println("something happens: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("something happens: " + e.getLocalizedMessage());
			e.printStackTrace();
		}

	}

	/**
	 * return time percentage of visible input satellite (supposed one
	 * satellite) for 24 hours from the current position.
	 * 
	 * @param currentPosllh
	 * @param rinexNav
	 * @return 0-100 [%]
	 */
	/*
	 * public double calcTimePercentageOfVisibleOver30deg( PositionLLH
	 * currentPosllh, RINEXNavigationFileReader rinexNav) { double result = 0.0;
	 * int[] prnList = rinexNav.getPrn_list(); Wtime cursorTime = null;
	 * Ephemeris ephemeris = null; int visibleCount = 0; int calcCount = 0;
	 * OrbitCalculator orbitCalculator = new OrbitCalculator(); PositionECEF
	 * satellitePosition = null; for (int i = 0; i < prnList.length; i++) { //
	 * use just first ephemeris ephemeris =
	 * rinexNav.getEphemerisArray(prnList[i])[0]; // get date and time from nav
	 * file cursorTime = new Wtime(ephemeris.getWeek(),
	 * ephemeris.getData(Ephemeris.FEILD_EPHM_TOC)); for (int j = 0; j < 24;
	 * j++) { satellitePosition = orbitCalculator.calcSatPosition( ephemeris,
	 * cursorTime, 0); if (PositionENU.convertToENU(satellitePosition,
	 * currentPosllh.convertToECEF()).getElevation() > 30/180*Math.PI) {
	 * visibleCount++; } calcCount++; cursorTime.goForward(3600);// go forward 1
	 * hour } } result = (double) visibleCount / (double) calcCount*100; return
	 * result; }
	 */
}
