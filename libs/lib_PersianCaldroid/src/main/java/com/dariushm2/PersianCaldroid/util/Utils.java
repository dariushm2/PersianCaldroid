package com.dariushm2.PersianCaldroid.util;


import android.content.Context;
import android.support.annotation.RawRes;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.dariushm2.PersianCaldroid.R;
import com.dariushm2.PersianCaldroid.entity.DayEntity;
import com.dariushm2.PersianCaldroid.entity.EventEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;
import calendar.AbstractDate;
import calendar.CivilDate;
import calendar.DateConverter;
import calendar.DayOutOfRangeException;
import calendar.IslamicDate;
import calendar.PersianDate;
import static com.dariushm2.PersianCaldroid.Constants.DEFAULT_APP_LANGUAGE;
import static com.dariushm2.PersianCaldroid.Constants.PERSIAN_COMMA;



/**
 * Common utilities that needed for this calendar
 *
 * @author ebraminio
 */

public class Utils {

    private final String TAG = Utils.class.getName();
    private Context context;

    private List<EventEntity> events;


    private String[] persianMonths;
    private String[] islamicMonths;
    private String[] gregorianMonths;
    private String[] weekDays;

    private int theme = R.style.LightTheme;


    private Utils(Context context) {
        this.context = context;
    }

    private static WeakReference<Utils> myWeakInstance;

    public static Utils getInstance(Context context) {
        if (myWeakInstance == null || myWeakInstance.get() == null) {
            myWeakInstance = new WeakReference<>(new Utils(context.getApplicationContext()));
        }
        return myWeakInstance.get();
    }


    public boolean iranTime;



    public void setTheme(Context context, int theme) {
        this.theme = theme;
        context.setTheme(theme);
    }


    public String getAppLanguage() {
        return  DEFAULT_APP_LANGUAGE;
    }

    public int getTheme() {
        return theme;
    }


    public PersianDate getToday() {
        return DateConverter.civilToPersian(new CivilDate(makeCalendarFromDate(new Date())));
    }

    public Calendar makeCalendarFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (iranTime) {
            calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        }
        calendar.setTime(date);
        return calendar;
    }


    public String formatNumber(int number) {
        return formatNumber(Integer.toString(number));
    }

    public String formatNumber(String number) {

        char[] result = number.toCharArray();
        return String.valueOf(result);
    }

    public String dateToString(AbstractDate date) {
        return formatNumber(date.getDayOfMonth()) + ' ' + getMonthName(date) + ' ' +
                formatNumber(date.getYear());
    }

    public String dayTitleSummary(PersianDate persianDate) {
        return getWeekDayName(persianDate) + PERSIAN_COMMA + " " + dateToString(persianDate);
    }

    public String[] monthsNamesOfCalendar(AbstractDate date) {
        // the next step would be using them so lets check if they have initialized already
        if (persianMonths == null || gregorianMonths == null || islamicMonths == null)
            loadLanguageResource();

        if (date instanceof PersianDate)
            return persianMonths.clone();
        else if (date instanceof IslamicDate)
            return islamicMonths.clone();
        else
            return gregorianMonths.clone();
    }

    public String getMonthName(AbstractDate date) {
        return monthsNamesOfCalendar(date)[date.getMonth() - 1];
    }

    public String getWeekDayName(AbstractDate date) {
        if (date instanceof IslamicDate)
            date = DateConverter.islamicToCivil((IslamicDate) date);
        else if (date instanceof PersianDate)
            date = DateConverter.persianToCivil((PersianDate) date);

        if (weekDays == null)
            loadLanguageResource();

        return weekDays[date.getDayOfWeek() % 7];
    }


    public void longToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    private String readStream(InputStream is) {
        // http://stackoverflow.com/a/5445161
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public String readRawResource(@RawRes int res) {
        return readStream(context.getResources().openRawResource(res));
    }



    private void loadEvents() {
        List<EventEntity> events = new ArrayList<>();
        try {
            JSONArray days = new JSONObject(readRawResource(R.raw.events)).getJSONArray("events");

            int length = days.length();
            for (int i = 0; i < length; ++i) {
                JSONObject event = days.getJSONObject(i);

                int year = event.getInt("year");
                int month = event.getInt("month");
                int day = event.getInt("day");
                String title = event.getString("title");
                boolean holiday = event.getBoolean("holiday");

                events.add(new EventEntity(new PersianDate(year, month, day), title, holiday));
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        this.events = events;
    }

    private int maxSupportedYear = -1;
    private int minSupportedYear = -1;
    private boolean isYearWarnGivenOnce = false;

    public void checkYearAndWarnIfNeeded(int selectedYear) {
        // once is enough, see #clearYearWarnFlag() also
        if (isYearWarnGivenOnce)
            return;

        if (maxSupportedYear == -1 || minSupportedYear == -1)
            loadMinMaxSupportedYear();

        if (selectedYear < minSupportedYear) {
            longToast(context.getString(R.string.holidaysIncompletenessWarning));

            isYearWarnGivenOnce = true;
        }

        if (selectedYear > maxSupportedYear) {
            longToast(context.getString(getToday().getYear() > maxSupportedYear
                    ? R.string.shouldBeUpdated
                    : R.string.holidaysIncompletenessWarning));

            isYearWarnGivenOnce = true;
        }
    }

    // called from PersianCaldroidFragment to make it once per calendar view
    public void clearYearWarnFlag() {
        isYearWarnGivenOnce = false;
    }

    private void loadMinMaxSupportedYear() {
        if (events == null) {
            loadEvents();
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (EventEntity eventEntity : events) {
            int year = eventEntity.getDate().getYear();

            if (min > year && year != -1) {
                min = year;
            }

            if (max < year) {
                max = year;
            }
        }

        minSupportedYear = min;
        maxSupportedYear = max;
    }

    public List<EventEntity> getEvents(PersianDate day) {
        if (events == null) {
            loadEvents();
        }

        List<EventEntity> result = new ArrayList<>();
        for (EventEntity eventEntity : events) {
            if (eventEntity.getDate().equals(day)) {
                result.add(eventEntity);
            }
        }
        return result;
    }

    public String getEventsTitle(PersianDate day, boolean holiday) {
        String titles = "";
        boolean first = true;
        List<EventEntity> dayEvents = getEvents(day);

        for (EventEntity event : dayEvents) {
            if (event.isHoliday() == holiday) {
                if (first) {
                    first = false;

                } else {
                    titles = titles + "\n";
                }
                titles = titles + event.getTitle();
            }
        }
        return titles;
    }


    public void loadLanguageResource() {
        @RawRes int messagesFile;
        String lang = getAppLanguage();

        if (lang.equals("fa-AF"))
            messagesFile = R.raw.messages_fa_af;
        else if (lang.equals("ps"))
            messagesFile = R.raw.messages_ps;
        else
            messagesFile = R.raw.messages_fa;

        persianMonths = new String[12];
        islamicMonths = new String[12];
        gregorianMonths = new String[12];
        weekDays = new String[7];

        try {
            JSONObject messages = new JSONObject(readRawResource(messagesFile));

            JSONArray persianMonthsArray = messages.getJSONArray("PersianCalendarMonths");
            for (int i = 0; i < 12; ++i)
                persianMonths[i] = persianMonthsArray.getString(i);

            JSONArray islamicMonthsArray = messages.getJSONArray("IslamicCalendarMonths");
            for (int i = 0; i < 12; ++i)
                islamicMonths[i] = islamicMonthsArray.getString(i);

            JSONArray gregorianMonthsArray = messages.getJSONArray("GregorianCalendarMonths");
            for (int i = 0; i < 12; ++i)
                gregorianMonths[i] = gregorianMonthsArray.getString(i);

            JSONArray weekDaysArray = messages.getJSONArray("WeekDays");
            for (int i = 0; i < 7; ++i)
                weekDays[i] = weekDaysArray.getString(i);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }



    public List<DayEntity> getDays(int offset) {
        List<DayEntity> days = new ArrayList<>();
        PersianDate persianDate = getToday();
        int month = persianDate.getMonth() - offset;
        month -= 1;
        int year = persianDate.getYear();

        year = year + (month / 12);
        month = month % 12;
        if (month < 0) {
            year -= 1;
            month += 12;
        }
        month += 1;
        persianDate.setMonth(month);
        persianDate.setYear(year);
        persianDate.setDayOfMonth(1);

        int dayOfWeek = DateConverter.persianToCivil(persianDate).getDayOfWeek() % 7;

        try {
            PersianDate today = getToday();
            for (int i = 1; i <= 31; i++) {
                persianDate.setDayOfMonth(i);

                DayEntity dayEntity = new DayEntity();
                dayEntity.setNum(formatNumber(i));
                dayEntity.setDayOfWeek(dayOfWeek);

                if (dayOfWeek == 6 || !TextUtils.isEmpty(getEventsTitle(persianDate, true))) {
                    dayEntity.setHoliday(true);
                }

                if (getEvents(persianDate).size() > 0) {
                    dayEntity.setEvent(true);
                }

                dayEntity.setPersianDate(persianDate.clone());

                if (persianDate.equals(today)) {
                    dayEntity.setToday(true);
                }

                days.add(dayEntity);
                dayOfWeek++;
                if (dayOfWeek == 7) {
                    dayOfWeek = 0;
                }
            }
        } catch (DayOutOfRangeException e) {
            // okay, it was expected
        }

        return days;
    }



}
