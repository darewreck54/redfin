import java.util.Calendar;

/**
 * Used to convert to Soda Mapped Values.  This is used in relation to the Soda API Client
 * and the expected query data types.  
 */
public class SodaDataConvertor {
	/**
	 * String representation of the Calendar value of AM/PM
	 * @param amPm - Calendar's value representing AM/PM
	 * @return String representation of AM or PM
	 */
	public static String convertAmPm(final int amPm) {
		return (amPm == Calendar.AM) ? "AM" : "PM";
	}

	/**
	 * Used to convert a Calendar Day Of the Week to the Socrata's Endpoint
	 * Day of the week.  
	 * @param dayOfWeek - Calendar value for the day of the week (Ex. Friday, Tuesday)
	 * @return an int value representing the Socrata's value of the day of the week
	 */
	public static int convertToDayOfWeekStr(final int dayOfWeek) {
		int dayOrder = -1;
		switch (dayOfWeek) {
		case Calendar.MONDAY: {
			dayOrder = 1;
			break;
		}
		case Calendar.TUESDAY: {
			dayOrder = 2;
			break;
		}
		case Calendar.WEDNESDAY: {
			dayOrder = 3;
			break;
		}
		case Calendar.THURSDAY: {
			dayOrder = 4;
			break;
		}
		case Calendar.FRIDAY: {
			dayOrder = 5;
			break;
		}
		case Calendar.SATURDAY: {
			dayOrder = 6;
			break;
		}
		case Calendar.SUNDAY: {
			dayOrder = 0;
			break;
		}
		}

		return dayOrder;
	}
}
