package calendar;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Amir
 * @author ebraminio (implementing isLeapYear)
 */

public class PersianDate extends AbstractDate implements Serializable {
    private int year;
    private int month;
    private int day;


    private static final String[] persianMonthName = {"", "فروردین",
            "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان",
            "آذر", "دی", "بهمن", "اسفند"};


    public PersianDate() {
        PersianDate p = DateConverter.civilToPersian(new CivilDate());
        setYear(p.getYear());
        // Initialize day, so that we get no exceptions when setting month
        this.day = 1;
        setMonth(p.getMonth());
        setDayOfMonth(p.getDayOfMonth());
    }

    public PersianDate(int year, int month, int day) {
        setYear(year);
        // Initialize day, so that we get no exceptions when setting month
        this.day = 1;
        setMonth(month);
        setDayOfMonth(day);
    }

    public void nextDay() {
        CivilDate date = DateConverter.persianToCivil(this);
        String dt = date.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        //dt = sdf.format(c.getTime());
        date = new CivilDate(c);
        PersianDate p = DateConverter.civilToPersian(date);
        year = p.getYear();
        month = p.getMonth();
        day = p.getDayOfMonth();
    }

    public void prevDay() {
        CivilDate date = DateConverter.persianToCivil(this);
        String dt = date.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -1);
        //dt = sdf.format(c.getTime());
        date = new CivilDate(c);
        PersianDate p = DateConverter.civilToPersian(date);
        year = p.getYear();
        month = p.getMonth();
        day = p.getDayOfMonth();
    }

    public void addDays(int days) {
        CivilDate date = DateConverter.persianToCivil(this);
        String dt = date.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, days);
        //dt = sdf.format(c.getTime());
        date = new CivilDate(c);
        PersianDate p = DateConverter.civilToPersian(date);
        year = p.getYear();
        month = p.getMonth();
        day = p.getDayOfMonth();
    }


    public String getMonthName() {
        return persianMonthName[month];
    }

    @Override
    public String toString() {
        Locale locale = new Locale("en");
        return String.format(locale, "%04d", year) + "/" + String.format(locale, "%02d", month) + "/" + String.format(locale, "%02d", day);
    }



    public String toStringInPersian() {
        Locale locale = new Locale("fa");
        return String.format(locale, "%04d", year) + "/" + String.format(locale, "%02d", month) + "/" + String.format(locale, "%02d", day);
    }

    public PersianDate clone() {
        return new PersianDate(getYear(), getMonth(), getDayOfMonth());
    }

    public int getDayOfMonth() {
        return day;
    }

    public void setDayOfMonth(int day) {
        if (day < 1)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if (month <= 6 && day > 31)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if (month > 6 && month <= 12 && day > 30)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if (isLeapYear() && month == 12 && day > 30)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if ((!isLeapYear()) && month == 12 && day > 29)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12)
            throw new MonthOutOfRangeException(
                    Constants.MONTH + " " + month + " " + Constants.IS_OUT_OF_RANGE);

        // Set the day again, so that exceptions are thrown if the
        // day is out of range
        setDayOfMonth(day);

        this.month = month;
    }

    public int getWeekOfYear() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year == 0)
            throw new YearOutOfRangeException(Constants.YEAR_0_IS_INVALID);

        this.year = year;
    }

    public void rollDay(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public void rollMonth(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public void rollYear(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public String getEvent() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public int getDayOfYear() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getWeekOfMonth() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public boolean isLeapYear() {
        int y;
        if (year > 0)
            y = year - 474;
        else
            y = 473;
        return (((((y % 2820) + 474) + 38) * 682) % 2816) < 682;
    }

    public boolean equals(PersianDate persianDate) {
        if (persianDate == null)
            return false;
        else
            return this.getDayOfMonth() == persianDate.getDayOfMonth()
                    && this.getMonth() == persianDate.getMonth()
                    && (this.getYear() == persianDate.getYear() || this.getYear() == -1);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PersianDate))
            return false;
        if (obj == this)
            return true;
        PersianDate persianDate = (PersianDate) obj;
        return this.getDayOfMonth() == persianDate.getDayOfMonth()
                && this.getMonth() == persianDate.getMonth()
                && (this.getYear() == persianDate.getYear() || this.getYear() == -1);
    }

    @Override
    public int hashCode() {
        String date = toString();
        return date.hashCode();
    }

    public static PersianDate valueOf(String date) {
        int mYear;
        int mMonth;
        int mDay;
        try {
            mYear = Integer.parseInt(date.substring(0, 4));
            mMonth = Integer.parseInt(date.substring(5, 7));
            mDay = Integer.parseInt(date.substring(8, 10));
        } catch (WrongDateFormatException e) {
            e.printStackTrace();
            return null;
        }

        return new PersianDate(mYear, mMonth, mDay);
    }


    public boolean after(PersianDate persianDate) {
        //if (this.getClass() != null)
        if (getYear() > persianDate.getYear())
            return true;
        else if (getYear() == persianDate.getYear() && getMonth() > persianDate.getMonth())
            return true;
        else if (getYear() == persianDate.getYear() && getMonth() == persianDate.getMonth() &&
                getDayOfMonth() > persianDate.getDayOfMonth())
            return true;

        return false;

    }

    public boolean before(PersianDate persianDate) {
        //if (this.getClass() != null)
        if (getYear() < persianDate.getYear())
            return true;
        else if (getYear() == persianDate.getYear() && getMonth() < persianDate.getMonth())
            return true;
        else if (getYear() == persianDate.getYear() && getMonth() == persianDate.getMonth() &&
                getDayOfMonth() < persianDate.getDayOfMonth())
            return true;
        return false;
    }

    public static long daysBetween(PersianDate firstPersianDate, PersianDate secondPersianDate) {

        CivilDate firstCivilDate = DateConverter.persianToCivil(firstPersianDate);
        CivilDate secondCivilDate = DateConverter.persianToCivil(secondPersianDate);

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        long millis = 0;
        try {

            Date firstDate = df.parse(firstCivilDate.toString());
            Date secondDate = df.parse(secondCivilDate.toString());

            millis = firstDate.getTime() - secondDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis / 1000 / 60 / 60 / 24;
    }
}
