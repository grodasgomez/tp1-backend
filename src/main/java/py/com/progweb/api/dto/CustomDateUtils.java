package py.com.progweb.api.dto;

import java.util.Calendar;
import java.util.Date;

public class CustomDateUtils {
    public static Date add4Hours(Date date) {
        new Date(date.getTime() + (1000 * 60 * 60 * 12));
        return date;
    }

	public static Date sumDaysToDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
