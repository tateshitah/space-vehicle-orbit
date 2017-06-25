/**
 
Copyright (c) 2012-2017 braincopy.org

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

package org.braincopy.kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.braincopy.orbit.PositionECEF;
import org.braincopy.orbit.PositionLLH;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;

public class KMLCreator {
	Document doc;
	String transparency = "7f";// "00" means no color, "ff" means no
								// transparency
	String[] colorStrs = { transparency + "000099", transparency + "0000ff", transparency + "0011ff",
			transparency + "0033ff", transparency + "0066ff", transparency + "0099ff", transparency + "00bbff",
			transparency + "00ffff", transparency + "22cccc", transparency + "44aaaa", transparency + "668888",
			transparency + "886666", transparency + "aa4444", transparency + "cc2222", transparency + "ff0000",
			transparency + "990000" };
	// new String[16];

	public KMLCreator(Kml kml) {
		this.doc = kml.createAndSetDocument().withOpen(true);
		Style style1 = doc.createAndAddStyle().withId("polystyle0");
		style1.createAndSetPolyStyle().withColor(colorStrs[0]);
		style1.createAndSetLineStyle().withWidth(0);
		Style style2 = doc.createAndAddStyle().withId("polystyle1");
		style2.createAndSetPolyStyle().withColor(colorStrs[1]);
		style2.createAndSetLineStyle().withWidth(0);
		Style style3 = doc.createAndAddStyle().withId("polystyle2");
		style3.createAndSetPolyStyle().withColor(colorStrs[2]);
		style3.createAndSetLineStyle().withWidth(0);
		Style style4 = doc.createAndAddStyle().withId("polystyle3");
		style4.createAndSetPolyStyle().withColor(colorStrs[3]);
		style4.createAndSetLineStyle().withWidth(0);
		Style style5 = doc.createAndAddStyle().withId("polystyle4");
		style5.createAndSetPolyStyle().withColor(colorStrs[4]);
		style5.createAndSetLineStyle().withWidth(0);
		Style style6 = doc.createAndAddStyle().withId("polystyle5");
		style6.createAndSetPolyStyle().withColor(colorStrs[5]);
		style6.createAndSetLineStyle().withWidth(0);
		Style style7 = doc.createAndAddStyle().withId("polystyle6");
		style7.createAndSetPolyStyle().withColor(colorStrs[6]);
		style7.createAndSetLineStyle().withWidth(0);
		Style style8 = doc.createAndAddStyle().withId("polystyle7");
		style8.createAndSetPolyStyle().withColor(colorStrs[7]);
		style8.createAndSetLineStyle().withWidth(0);
		Style style9 = doc.createAndAddStyle().withId("polystyle8");
		style9.createAndSetPolyStyle().withColor(colorStrs[8]);
		style9.createAndSetLineStyle().withWidth(0);
		Style style10 = doc.createAndAddStyle().withId("polystyle9");
		style10.createAndSetPolyStyle().withColor(colorStrs[9]);
		style10.createAndSetLineStyle().withWidth(0);
		Style style11 = doc.createAndAddStyle().withId("polystyle10");
		style11.createAndSetPolyStyle().withColor(colorStrs[10]);
		style11.createAndSetLineStyle().withWidth(0);
		Style style12 = doc.createAndAddStyle().withId("polystyle11");
		style12.createAndSetPolyStyle().withColor(colorStrs[11]);
		style12.createAndSetLineStyle().withWidth(0);
		Style style13 = doc.createAndAddStyle().withId("polystyle12");
		style13.createAndSetPolyStyle().withColor(colorStrs[12]);
		style13.createAndSetLineStyle().withWidth(0);
		Style style14 = doc.createAndAddStyle().withId("polystyle13");
		style14.createAndSetPolyStyle().withColor(colorStrs[13]);
		style14.createAndSetLineStyle().withWidth(0);
		Style style15 = doc.createAndAddStyle().withId("polystyle14");
		style15.createAndSetPolyStyle().withColor(colorStrs[14]);
		style15.createAndSetLineStyle().withWidth(0);
		Style style16 = doc.createAndAddStyle().withId("polystyle15");
		style16.createAndSetPolyStyle().withColor(colorStrs[15]);
		style16.createAndSetLineStyle().withWidth(0);

		Style lineStyle1 = doc.createAndAddStyle().withId("lineStyle1");
		lineStyle1.createAndSetLineStyle().withColor("ffFF1493").withWidth(4.0);

	}

	/**
	 * In this method, new Placemark will be added to KML object of Document
	 * object which KMLCreator object has.
	 * 
	 * @param center
	 * @param mesh
	 * @param styleUrl
	 * @return
	 */
	public Placemark createUnitPlacemark(Coordinate center, int mesh, String styleUrl, String description) {
		Placemark result = null;
		result = doc.createAndAddPlacemark().withStyleUrl(styleUrl);
		result.createAndSetPolygon().withExtrude(true).createAndSetOuterBoundaryIs().createAndSetLinearRing()
				.addToCoordinates(center.getLongitude() - mesh * 0.5, center.getLatitude() + mesh * 0.5,
						center.getAltitude())
				.addToCoordinates(center.getLongitude() + mesh * 0.5, center.getLatitude() + mesh * 0.5,
						center.getAltitude())
				.addToCoordinates(center.getLongitude() + mesh * 0.5, center.getLatitude() - mesh * 0.5,
						center.getAltitude())
				.addToCoordinates(center.getLongitude() - mesh * 0.5, center.getLatitude() - mesh * 0.5,
						center.getAltitude());

		// add in May 31, 2017 by Hiroaki Tateshita
		result.setName(center.getLongitude() + ", " + center.getLatitude());
		result.setDescription(description);

		return result;
	}

	public static void main(String[] args) {
		final Kml kml = new Kml();
		KMLCreator creator = new KMLCreator(kml);
		int mesh = 3;// [degree]]
		for (int i = 0; i < 360 / mesh; i++) {
			for (int j = 1; j < 180 / mesh; j++) {
				creator.createUnitPlacemark(new Coordinate(i * mesh - 180, j * mesh - 90, 30), mesh,
						"polystyle" + (i + j) % 16, "test" + i + j);
			}
		}
		kml.marshal();
		try {
			kml.marshal(new File("HelloKML.kml"));
		} catch (FileNotFoundException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public void addOrbitOfASatellites(PositionECEF[][] posEcefMatrix, int[] prnList) {

		Placemark[] gtLines = new Placemark[posEcefMatrix.length];
		for (int i = 0; i < gtLines.length; i++) {
			gtLines[i] = doc.createAndAddPlacemark().withName("QZSS").withStyleUrl("#lineStyle1");
		}
		ArrayList<Coordinate> coordinates = null;
		PositionLLH tempLLH = null;

		for (int j = 0; j < posEcefMatrix.length; j++) {
			coordinates = new ArrayList<Coordinate>();
			for (int i = 0; i < posEcefMatrix[j].length; i++) {
				tempLLH = posEcefMatrix[j][i].convertToLLH();
				coordinates.add(new Coordinate(tempLLH.getLon() * 180 / Math.PI, tempLLH.getLat() * 180 / Math.PI,
						tempLLH.getHeight()));
			}
			gtLines[j].createAndSetLineString().withCoordinates(coordinates);
			// .withAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);
		}

	}
}
