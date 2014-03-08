package org.braincopy.orbit;
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


import org.braincopy.orbit.PositionECEF;
import org.braincopy.orbit.PositionLLH;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hiroaki Tateshita
 */
public class PositionECEFTest {

    public PositionECEFTest() {
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
     * Test of convertToLLH method, of class PositionECEF.
     */
    @Test
    public void testConvertToLLH() {
        System.out.println("convertToLLH");
        PositionECEF positionOfGSI_ECEF = new PositionECEF(-3957233.4647, 3310367.7082, 3737528.0249);
        System.out.println("0627 GST: "+positionOfGSI_ECEF.convertToLLH());
        PositionECEF positionOfGSI2_ECEF = new PositionECEF( -3957160.4048,3310202.1487,3737751.0078);
        System.out.println("2110 GST: "+positionOfGSI2_ECEF.convertToLLH());



        PositionECEF positionOfENRI_ECEF = new PositionECEF(-3947763.354, 3364399.155, 3699430.076);
        PositionLLH result = positionOfENRI_ECEF.convertToLLH();
        System.out.println(result.toString());

        if (Math.abs(result.latitude - 35.68 / 180 * Math.PI) > 1) {
            fail("wrong calculation: latitude = " + result.latitude);
        }
        if (Math.abs(result.longitude - 139.56 / 180 * Math.PI) > 1) {
            fail("wrong calculation: longitude = " + result.longitude);
        }

        positionOfENRI_ECEF = new PositionECEF(-3899086.094, 3166914.545, 3917336.601);
        result = positionOfENRI_ECEF.convertToLLH();
        System.out.println(result.toString());
        if (Math.abs(result.latitude - 38.13579617 / 180 * Math.PI) > 1) {
            fail("wrong calculation: latitude = " + result.latitude);
        }
        if (Math.abs(result.longitude - 140.91581617 / 180 * Math.PI) > 1) {
            fail("wrong calculation: longitude = " + result.longitude);
        }
        if (Math.abs(result.height - 41.940) > 1) {
            fail("wrong calculation: height = " + result.height);
        }
    }
}
