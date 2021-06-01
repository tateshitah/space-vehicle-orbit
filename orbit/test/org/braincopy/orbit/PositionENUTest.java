package org.braincopy.orbit;
/*
Copyright (c) 2014-2021 Hiroaki Tateshita

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Hiroaki Tateshita
 */
public class PositionENUTest {

	public PositionENUTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of convertToENU method, of class PositionENU.
	 */
	@Test
	public void testConvertToENU() {
		System.out.println("convertToENU");

		PositionECEF position = new PositionLLH(38.14227288 / 180 * Math.PI, 140.93265738 / 180 * Math.PI, 45.664)
				.convertToECEF();
		PositionECEF base = new PositionLLH(38.13877338 / 180 * Math.PI, 140.89872429 / 180 * Math.PI, 44.512)
				.convertToECEF();
		PositionENU result = PositionENU.convertToENU(position, base);
		System.out.println(result);

		if (Math.abs(result.east - 2974.681) > 1) {
			fail("wrong result: east = " + result.east);
		}
		if (Math.abs(result.north - 388.988) > 1) {
			fail("wrong result: north = " + result.north);
		}
		if (Math.abs(result.up - 0.447) > 1) {
			fail("wrong result: north = " + result.north);
		}

	}

	@Test
	public void testGetAzimuth() {
		System.out.println("getAzimuth");
		PositionENU position = new PositionENU(1, 1, 1);
		assertEquals(Math.PI / 4, position.getAzimuth(), 0.001);
		position = new PositionENU(-1, 1, 1);
		assertEquals(3 * Math.PI / 4, position.getAzimuth(), 0.001);
		position = new PositionENU(1, -1, 1);
		assertEquals(-Math.PI / 4, position.getAzimuth(), 0.001);
		position = new PositionENU(1,0,0);//just east
		assertEquals(0, position.getAzimuth(),0.001);
		position = new PositionENU(-1,0,0);//just east
		assertEquals(Math.PI, position.getAzimuth(),0.001);

	}
}
