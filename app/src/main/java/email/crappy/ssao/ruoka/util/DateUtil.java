package email.crappy.ssao.ruoka.util;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Santeri 'iffa'
 */
public class DateUtil {
    public static boolean isDataExpired(String expiration) {
        GregorianCalendar current = new GregorianCalendar();
        GregorianCalendar expire = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        try {
            expire.setTime(sdf.parse(expiration));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Logger.d("expire: " + expire.get(GregorianCalendar.DAY_OF_MONTH) + " " + expire.get(GregorianCalendar.MONTH) + " " + expire.get(GregorianCalendar.YEAR));
        Logger.d("current: " + current.get(GregorianCalendar.DAY_OF_MONTH) + " " + current.get(GregorianCalendar.MONTH) + " " + current.get(GregorianCalendar.YEAR));

        return current.after(expire);
    }

    public static boolean isToday(String date) {
        int day = Integer.parseInt(date.split("\\.")[0]);
        int month = Integer.parseInt(date.split("\\.")[1]);

        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.DAY_OF_MONTH) == day && calendar.get(Calendar.MONTH) == (month - 1);

    }

    public static boolean isCurrentWeek(String weekString) {
        GregorianCalendar current = new GregorianCalendar();
        int week = Integer.parseInt(weekString);

        return current.get(Calendar.WEEK_OF_YEAR) == week;
    }

    public static boolean isDateThisWeek(String date) {
        GregorianCalendar current = new GregorianCalendar();
        GregorianCalendar item = new GregorianCalendar();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        try {
            item.setTime(sdf.parse(date + "." + current.get(Calendar.YEAR)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return item.get(Calendar.WEEK_OF_YEAR) == current.get(Calendar.WEEK_OF_YEAR);
    }

    public static boolean isValentines() {
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.DAY_OF_MONTH) == 14 && calendar.get(Calendar.MONTH) == 1;
    }

}
