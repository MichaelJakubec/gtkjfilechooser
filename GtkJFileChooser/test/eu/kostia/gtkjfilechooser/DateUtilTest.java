package eu.kostia.gtkjfilechooser;

import static java.util.Calendar.JANUARY;
import static java.util.Calendar.OCTOBER;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DateUtilTest {
	static private final Locale SYSTEM_DEFAULT = Locale.getDefault();
	static private final Date TODAY = new GregorianCalendar(2009, OCTOBER, 20, 00, 01, 30)
	.getTime();

	@BeforeClass
	static public void beforeClass() {
		Locale.setDefault(Locale.ENGLISH);
	}

	@AfterClass
	static public void afterClass() {
		Locale.setDefault(SYSTEM_DEFAULT);
	}

	@Test
	public void testToJulianDayNumber0() throws Exception {
		Date date = new GregorianCalendar(2009, OCTOBER, 20).getTime();
		assertEquals(2455125L, DateUtil.toJulianDayNumber(date));
	}

	@Test
	public void testToJulianDayNumber1() throws Exception {
		Date date = new GregorianCalendar(2000, JANUARY, 1, 12, 00).getTime();
		assertEquals(2451545L, DateUtil.toJulianDayNumber(date));
	}

	@Test
	public void testToPrettyFormatToday() throws Exception {
		assertEquals("00:01", DateUtil.toPrettyFormat(TODAY, TODAY));
	}

	@Test
	public void testToPrettyFormatYesterday() throws Exception {
		Date yesterday = new GregorianCalendar(2009, OCTOBER, 19, 23, 59, 59).getTime();
		assertEquals("Yesterday at 23:59", DateUtil.toPrettyFormat(yesterday, TODAY));
	}

	@Test
	public void testToPrettyFormatSixDaysAgo() throws Exception {
		Date sixDaysAgo = new GregorianCalendar(2009, OCTOBER, 14, 23, 30, 55).getTime();
		assertEquals("Wednesday", DateUtil.toPrettyFormat(sixDaysAgo, TODAY));

	}

	@Test
	public void testToPrettyFormatSevenDaysAgo() throws Exception {
		Date sevenDaysAgo = new GregorianCalendar(2009, OCTOBER, 13, 23, 30).getTime();
		assertEquals("10/13/09", DateUtil.toPrettyFormat(sevenDaysAgo, TODAY));
	}

	@Test
	public void testStringFormat() throws Exception {
		String fmtEn = "Yesterday at %H:%M\n";
		System.out.printf(fmtEn, new Date());

		String fmtIt = "Ieri alle %k.%M\n";
		System.out.printf(fmtIt, new Date());


	}

}
