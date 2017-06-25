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
public class DOPCalculator {

	/**
	 * 
	 * output is KML file
	 * 
	 * @throws SatElsetException
	 * @throws ObjectDecayed
	 * @throws FileNotFoundException
	 * @throws CannotInverseException
	 */
	public void outputCalcResult(ArrayList<TLEString> tleList)
			throws ObjectDecayed, SatElsetException, FileNotFoundException, CannotInverseException {

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
		}
		Kml kml = new Kml();
		KMLCreator kmlCreator = new KMLCreator(kml);

		double mesh = 3.0;// [degree]]
		double targetHDOP = 3.0;
		PositionLLH currentPosllh = null;
		double percentageUnderCertainHDOP = 0;
		for (int i = 0; i < 180 / mesh; i++) {
			for (int j = 0; j < 180 / mesh; j++) {
				currentPosllh = new PositionLLH((j * mesh - 90.0) / 180.0 * Math.PI, (i * mesh) / 180.0 * Math.PI, 0.0);
				percentageUnderCertainHDOP = calcpercentageUnderCertainHDOP(currentPosllh, resultsList, targetHDOP,
						15.0, startDay, startYear, step);
				if (percentageUnderCertainHDOP == 100.0) {
					percentageUnderCertainHDOP = 99.0;
				}
				System.out.println((i * mesh) + ", " + (j * mesh - 90.0) + " :" + percentageUnderCertainHDOP);
				kmlCreator.createUnitPlacemark(new Coordinate(i * mesh, j * mesh - 90, 30), (int) mesh,
						"polystyle" + ((int) percentageUnderCertainHDOP * 16 / 100),
						Double.toString(percentageUnderCertainHDOP));
			}
		}
		String kmlFileName = "testdata/hdop.kml";
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
	 * @throws CannotInverseException
	 */
	private double calcpercentageUnderCertainHDOP(PositionLLH currentPosllh, ArrayList<Vector<Sgp4Data>> resultsList,
			double targetHDOP, double elevationMask, double startDay, int startYear, int step)
			throws CannotInverseException {
		double result = 0.0;
		double[] hdopArray = new double[24 * 60 / step + 1];
		for (int i = 0; i < hdopArray.length; i++) {
			hdopArray[i] = 0.0;
		}

		Vector<Sgp4Data> satellitesListInTheIndex;

		for (int i = 0; i < hdopArray.length; i++) {
			satellitesListInTheIndex = getSatellitesListInTheIndex(resultsList, i);
			hdopArray[i] = calcHDOP(currentPosllh, satellitesListInTheIndex, startDay, startYear, step, i,
					elevationMask);

		}

		int count = 0;
		for (int i = 0; i < hdopArray.length; i++) {
			if (hdopArray[i] < targetHDOP) {
				count++;
			}
		}
		result = (double) count / (double) hdopArray.length * 100;
		return result;
	}

	private double calcHDOP(PositionLLH currentPosllh, Vector<Sgp4Data> satellitesListInTheIndex, double startDay,
			int startYear, int step, int index, double elevationMask) throws CannotInverseException {
		double result = Double.MAX_VALUE;
		Iterator<Sgp4Data> resultIte = satellitesListInTheIndex.iterator();
		Sgp4Data data = null;
		double days = 0.0;

		GregorianCalendar dateAndTime = null;
		PositionECI posEci = null;
		PositionENU currentPositionENU = null;
		double currentElevation = 0;
		double currentAzimuth = 0;
		double designMatrix[][] = new double[satellitesListInTheIndex.size()][4];

		int i = 0;
		int availableSatNum = 0;
		double[][] GTG = new double[4][4];

		while (resultIte.hasNext()) {
			data = resultIte.next();
			days = startDay + index * (double) step * 60 / (double) ConstantNumber.SECONDS_DAY;
			dateAndTime = Main.getCalendarFmYearAndDays(startYear, days);
			posEci = new PositionECI(data.getX() * ConstantNumber.RADIUS_OF_EARTH,
					data.getY() * ConstantNumber.RADIUS_OF_EARTH, data.getZ() * ConstantNumber.RADIUS_OF_EARTH,
					dateAndTime);
			currentPositionENU = PositionENU.convertToENU(posEci.convertToECEF(), currentPosllh.convertToECEF());
			currentElevation = currentPositionENU.getElevation();

			if (currentElevation < elevationMask / 180 * Math.PI) {
				continue;
			}

			currentAzimuth = currentPositionENU.getAzimuth();

			designMatrix[i][0] = -Math.cos(currentElevation) * Math.sin(currentAzimuth);
			designMatrix[i][1] = -Math.cos(currentElevation) * Math.cos(currentAzimuth);
			designMatrix[i][2] = -Math.sin(currentElevation);
			designMatrix[i][3] = 1.0;

			i++;

		}
		availableSatNum = i;
		// for GtG calculation
		if (availableSatNum >= 4) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					for (int l = 0; l < availableSatNum; l++) {
						GTG[j][k] += designMatrix[l][j] * designMatrix[l][k];
					}
				}
			}

			double[][] cov = inverse_matrix(GTG, 4);
			result = Math.sqrt(cov[0][0] + cov[1][1]);
		}
		return result;
	}

	/**
	 * 
	 * @param resultsList
	 * @param index
	 * @return
	 */
	private Vector<Sgp4Data> getSatellitesListInTheIndex(ArrayList<Vector<Sgp4Data>> resultsList, int index) {
		Vector<Sgp4Data> result = new Vector<Sgp4Data>();
		Iterator<Vector<Sgp4Data>> ite = resultsList.iterator();
		while (ite.hasNext()) {
			result.add((Sgp4Data) ((Vector<Sgp4Data>) ite.next()).get(index));
		}
		return result;
	}

	/**
	 * 
	 * @param matrix
	 *            input matrix
	 * @param dim
	 *            dimention
	 * @throws CannotInverseException
	 */
	protected double[][] inverse_matrix(double[][] matrix, int dim) throws CannotInverseException {

		int i, j, k;
		double[][] b = new double[dim][2 * dim];

		/**/
		for (i = 0; i < dim; i++) {
			for (j = 0; j < dim; j++) {
				b[i][j] = matrix[i][j];
				if (i == j)
					b[i][j + dim] = 1.0;
				else
					b[i][j + dim] = 0.0;
			}
		}

		/**/
		for (i = 0; i < dim; i++) {
			/**/
			if (Math.abs(b[i][i]) <= 1E-16) {// fabs()
				throw new CannotInverseException("value is " + b[i][i]);
				// System.exit(2);
			}
			for (j = dim + dim - 1; j >= i; j--) {
				b[i][j] /= b[i][i];
			}

			/**/
			for (k = 0; k < dim; k++)
				if (k != i) {
					for (j = dim + dim - 1; j >= i; j--) {
						b[k][j] -= b[k][i] * b[i][j];
					}
				}
		}

		/**/
		for (i = 0; i < dim; i++) {
			for (j = 0; j < dim; j++) {
				matrix[i][j] = b[i][j + dim];
			}
		}
		return matrix;
	}

	public static void main(String[] args) {
		DOPCalculator calculator = new DOPCalculator();
		ArrayList<TLEString> tleList = new ArrayList<TLEString>();
		TLEString qzss1TLEString = new TLEString();
		qzss1TLEString.setLine1("1 37158U 10045A   17136.38279348 -.00000102  00000-0  00000-0 0  9990");
		qzss1TLEString.setLine2("2 37158  40.8296 159.7841 0751802 270.0854  78.4139  1.00288962 24430");
		tleList.add(qzss1TLEString);

		TLEString qzss2TLEString = new TLEString();
		qzss2TLEString.setLine1("1 42738U 17028A   17175.62920076 -.00000254 +00000-0 +00000-0 0  9997");
		qzss2TLEString.setLine2("2 42738 044.7389 289.4881 0753048 269.8262 076.5640 01.00256368000260");
		tleList.add(qzss2TLEString);

		TLEString qzss3TLEString = new TLEString();
		qzss3TLEString.setLine1("1 28937U 06004A   17150.86877781 -.00000262  00000-0  00000-0 0  9996");
		qzss3TLEString.setLine2("2 28937   0.0213  88.6063 0003697 329.9697 269.7870  1.00271007 41347");
		tleList.add(qzss3TLEString);

		TLEString qzss4TLEString = new TLEString();
		qzss4TLEString.setLine1("1 37158U 10045A   17136.38279348 -.00000102  00000-0  00000-0 0  9990");
		qzss4TLEString.setLine2("2 37158  40.8296  29.7841 0751802 270.0854 208.4139  1.00288962 24431");
		tleList.add(qzss4TLEString);

		TLEString qzss5TLEString = new TLEString();
		qzss5TLEString.setLine1("1 28937U 06004A   17150.86877781 -.00000262  00000-0  00000-0 0  9996");
		qzss5TLEString.setLine2("2 28937   0.0213  58.6063 0003697 329.9697 269.7870  1.00271007 41344");
		tleList.add(qzss5TLEString);

		TLEString qzss6TLEString = new TLEString();
		qzss6TLEString.setLine1("1 28937U 06004A   17150.86877781 -.00000262  00000-0  00000-0 0  9996");
		qzss6TLEString.setLine2("2 28937   0.0213 118.6063 0003697 329.9697 269.7870  1.00271007 41341");
		tleList.add(qzss6TLEString);

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
		} catch (CannotInverseException e) {
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
