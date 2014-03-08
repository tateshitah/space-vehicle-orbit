/*
Copyright (c) 2014 Hiroaki Tateshita

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package org.braincopy.orbit;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;

import sgp4v.ObjectDecayed;
import sgp4v.SatElsetException;
import sgp4v.Sgp4Data;
import sgp4v.Sgp4Unit;

/**
 * @author Hiroaki Tateshita
 * 
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("--- demo ---");
		Sgp4Unit sgp4 = new Sgp4Unit();
		int startYear, stopYear, step;
		double startDay, stopDay;

		startYear = 2014;
		startDay = 61.5;// March 2nd 12 am
		stopYear = 2014;
		stopDay = 62.5;
		step = 60;// 60 [minutes] = 1 hour

		/*
		 * following two line element is retrieved from space-track.org
		 */
		String line1 = "1 37158U 10045A   14058.66994286 -.00000212  00000-0  00000+0 0  7489";
		String line2 = "2 37158 040.6013 176.9014 0749309 270.0563 089.2168 01.00264238 12691";

		Vector<Sgp4Data> results;
		try {
			results = sgp4.runSgp4(line1, line2, startYear, startDay, stopYear,
					stopDay, step);// step's unit should be minute.
			PositionECI posEci = null;
			PositionLLH posLlh = null;
			GregorianCalendar dateAndTime = null;
			double days = 0;
			Sgp4Data data = null;
			for (int i = 0; i < results.size(); i++) {
				data = (Sgp4Data) results.elementAt(i);
				days = startDay + i * (double) step * 60
						/ (double) ConstantNumber.SECONDS_DAY;
				dateAndTime = Main.getCalendarFmYearAndDays(startYear, days);
				posEci = new PositionECI(data.getX()
						* ConstantNumber.RADIUS_OF_EARTH, data.getY()
						* ConstantNumber.RADIUS_OF_EARTH, data.getZ()
						* ConstantNumber.RADIUS_OF_EARTH, dateAndTime);
				posLlh = posEci.convertToECEF().convertToLLH();
				System.out.println(posLlh.getLat() * 180 / Math.PI + "\t"
						+ posLlh.getLon() * 180 / Math.PI + "\t"
						+ posLlh.getHeight());
			}
		} catch (ObjectDecayed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SatElsetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param year
	 * @param days
	 * @return
	 */
	public static GregorianCalendar getCalendarFmYearAndDays(int year,
			double days) {
		GregorianCalendar result = new GregorianCalendar(
				TimeZone.getTimeZone("UTC"));
		result.set(Calendar.YEAR, year);
		int daysOfYear = (int) days;
		result.set(Calendar.DAY_OF_YEAR, daysOfYear);
		double hours = (days - (double) daysOfYear) * 24;
		int hourOfDay = (int) hours;
		result.set(Calendar.HOUR_OF_DAY, hourOfDay);
		double minutes = (hours - (double) hourOfDay) * 60;
		int minuteOfHour = (int) minutes;
		result.set(Calendar.MINUTE, minuteOfHour);
		result.set(Calendar.SECOND,
				(int) ((minutes - (double) minuteOfHour) * 60));
		return result;
	}
}
