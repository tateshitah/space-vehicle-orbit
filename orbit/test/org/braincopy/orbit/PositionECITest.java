package org.braincopy.orbit;

import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;

import org.junit.Test;

import sgp4v.ObjectDecayed;
import sgp4v.SatElsetException;
import sgp4v.Sgp4Data;
import sgp4v.Sgp4Unit;

public class PositionECITest {

	@Test
	public void test() {
		// at first same TLE same date
		TLEString inTLE = new TLEString();
		inTLE.setLine1("1 25544U 98067A   21130.82534197  .00000540  00000-0  17959-4 0  9990");
		inTLE.setLine2("2 25544  51.6449 167.5222 0002900 345.0941 184.7772 15.48989745282809");

		Vector<Sgp4Data> results;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Sgp4Unit sgp4 = new Sgp4Unit();
		int startYear, stopYear, step;
		double startDay, stopDay;

		startYear = 2021;
		startDay = 131.10417;// May 11th 230 am UTC really?
		//startDay = 131.4795;
		stopYear = 2021;
		stopDay = 131.11;
		//stopDay = 130.84;
		step = 5;// 60 [minutes] = 1 hour

		try {
			results = sgp4.runSgp4(inTLE.getLine1(), inTLE.getLine2(), startYear, startDay, stopYear, stopDay, step);
			PositionECI posEci = null;
			PositionLLH posLlh = null;
			GregorianCalendar dateAndTime = null;
			double days = 0;
			Sgp4Data data = null;
			for (int i = 0; i < results.size(); i++) {
				data = (Sgp4Data) results.elementAt(i);
				days = startDay + i * (double) step * 60 / (double) ConstantNumber.SECONDS_DAY;
				dateAndTime = Main.getCalendarFmYearAndDays(startYear, days);
				posEci = new PositionECI(data.getX() * ConstantNumber.RADIUS_OF_EARTH,
						data.getY() * ConstantNumber.RADIUS_OF_EARTH, data.getZ() * ConstantNumber.RADIUS_OF_EARTH,
						dateAndTime);
				posLlh = posEci.convertToECEF().convertToLLH();
	//			System.out.println(sdf.format(dateAndTime.getTime()) + "\t"
	//					+ data.getX() * ConstantNumber.RADIUS_OF_EARTH / 1000 + ", "
	//					+ data.getY() * ConstantNumber.RADIUS_OF_EARTH / 1000 + ", "
	//					+ data.getZ() * ConstantNumber.RADIUS_OF_EARTH / 1000 + ":" + posLlh.getLat() * 180 / Math.PI
	//					+ "\t" + posLlh.getLon() * 180 / Math.PI + "\t" + posLlh.getHeight());
				System.out.println(sdf.format(dateAndTime.getTime()) + "\t"
						+ data.getX()* ConstantNumber.RADIUS_OF_EARTH  + ", "
						+ data.getY()* ConstantNumber.RADIUS_OF_EARTH  + ", "
						+ data.getZ()* ConstantNumber.RADIUS_OF_EARTH  + ":" + posLlh.getLat() * 180 / Math.PI
						+ "\t" + posLlh.getLon() * 180 / Math.PI + "\t" + posLlh.getHeight());
			}
		} catch (ObjectDecayed e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (SatElsetException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} // step's unit should be minute.

	}

	@Test
	public void testConvertToECEF() {
		/*
		 * julianDay = 2453937.82777778;
		 */
		GregorianCalendar dateAndTime = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		dateAndTime.set(2006, 6, 21, 7, 52, 0);
		PositionECI inputEci = new PositionECI(-2392.11241452386, -17078.1233608647, 19945.4195944851, dateAndTime);
		PositionECEF outputECEF = inputEci.convertToECEF();
		System.out.println(outputECEF + " should be [-15615.6915464865 -7322.85945823539 19943.173494021]");
		/*
		 * rECI = [-2392.11241452386 -17078.1233608647 19945.4195944851];
		 * 
		 * t = 2453937.82777778;7:52:0.00 UT on July 21, 2006 .
		 * 
		 * Will yield an ECEF position and velocity of
		 * 
		 * rECEF = [-15615.6915464865 -7322.85945823539 19943.173494021]; }
		 */
	}

	@Test
	public void testCalcGST() {
		Calendar in = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		in.set(2012, 4, 31, 2, 57, 31);
		double gst = PositionECI.calcGST(in);
		System.out.println(gst / Math.PI * 86400 + " should be " + (1107214.47173 % 86400));
		in.set(2021, 4, 11, 2, 30, 00);
		gst = PositionECI.calcGST(in);
		System.out.println(in.get(Calendar.HOUR_OF_DAY) + "<-HourOfDay : " + gst + " should be 17.77344842708558?");
	}
}
