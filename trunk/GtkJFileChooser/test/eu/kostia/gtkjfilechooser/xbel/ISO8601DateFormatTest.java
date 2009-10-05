package eu.kostia.gtkjfilechooser.xbel;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class ISO8601DateFormatTest {

	@Test
	public void testParse0() throws Exception {
		String source = "1994-11-05T08:15:30-05:00";
		Date date = new ISO8601DateFormat().parse(source);
		System.out.println(date.toString());
		assertEquals(new Date(784041330000L), date);
	}

	@Test
	public void testParse1() throws Exception {
		String source = "2009-09-06T16:59:39Z";
		Date date = new ISO8601DateFormat().parse(source);
		System.out.println(date.toString());
		assertEquals(new Date(1252256379000L), date);
	}



	@Test
	public void testFormat() throws Exception {
		Date date = new GregorianCalendar(2009, Calendar.OCTOBER, 5, 19, 30, 45).getTime();
		assertEquals("2009-10-05T19:30:45+02:00", new ISO8601DateFormat().format(date));
	}

}