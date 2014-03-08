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
 * @author 01028
 */
public class PositionLLHTest {

    public PositionLLHTest() {
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
     * Test of convertToECEF method, of class PositionLLH.
     */
    @Test
    public void testConvertToECEF() {
        System.out.println("convertToECEF");
        PositionLLH instance = new PositionLLH(38.13579616860843/180*Math.PI, 140.9158161673581/180*Math.PI, 41.93978297151625);
        PositionECEF result = instance.convertToECEF();
        System.out.println(result.toString());
        if (Math.abs(result.x+3899086.094) > 1) {
            fail("wrong calculation: x = " + result.x);
        }
        if (Math.abs(result.y - 3166914.545) > 1) {
            fail("wrong calculation: y = " + result.y);
        }
        if (Math.abs(result.z - 3917336.601) > 1) {
            fail("wrong calculation: z = " + result.z);
        }
    }

}