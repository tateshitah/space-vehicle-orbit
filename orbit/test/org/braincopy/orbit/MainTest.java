/*
Copyright (c) 2021 Hiroaki Tateshita

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

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class MainTest {
   @Test
   public void testGetCalendarFmYearAndDays(){
       int year = 2021;
       double days = 1;
       SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH:mm:ss.ss");
       sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

       Calendar calendar = Main.getCalendarFmYearAndDays(year, days);
       assertEquals("20210101_00:00:00.00",sdf.format(calendar.getTime()) );
       System.out.println(sdf.format(calendar.getTime()));
       days = 1.5;
       calendar = Main.getCalendarFmYearAndDays(year, days);
       assertEquals("20210101_12:00:00.00",sdf.format(calendar.getTime()) );
       System.out.println(sdf.format(calendar.getTime()));
       days = 131.4795;
       calendar = Main.getCalendarFmYearAndDays(year, days);
       assertEquals("20210511_11:30:28.28",sdf.format(calendar.getTime()) );
       System.out.println(sdf.format(calendar.getTime()));
       days = 131.10417;
       calendar = Main.getCalendarFmYearAndDays(year, days);
       System.out.println(sdf.format(calendar.getTime()));
       assertEquals("20210511_02:30:00.00",sdf.format(calendar.getTime()) );
       //2021 Jan 31 01:14:17 UTC should be 31.25992506
       days = 31.25992506;
       calendar = Main.getCalendarFmYearAndDays(year, days);
       assertEquals("20210131_06:14:17.17",sdf.format(calendar.getTime()) );
       System.out.println(sdf.format(calendar.getTime()));
       
   } 
}
