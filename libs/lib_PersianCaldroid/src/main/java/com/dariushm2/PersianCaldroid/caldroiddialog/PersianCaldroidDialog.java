package com.dariushm2.PersianCaldroid.caldroiddialog;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dariushm2.PersianCaldroid.Constants;
import com.dariushm2.PersianCaldroid.R;
import com.dariushm2.PersianCaldroid.util.Utils;

import java.util.Calendar;
import java.util.Locale;

import calendar.CivilDate;
import calendar.DateConverter;
import calendar.PersianDate;

public class PersianCaldroidDialog extends DialogFragment
        implements ViewPager.OnPageChangeListener {
    private ViewPager monthViewPager;
    private Utils utils;

    private PersianDate selectedPersianDate;

    private TextView txtDay;
    private TextView txtMonth;
    private TextView txtYear;
    private Typeface typeface;

    private Pair<PersianDate, Integer> backgroundResourceForDate;

    private int viewPagerPosition;

    CalendarDialogAdapter adapter;

    private OnDateSetListener mCallback;

    private PersianDate selectedDate;

    public void setSelectedDate(PersianDate date){
        this.selectedDate = date;
    }

    public PersianDate getSelectedDate() {
        return selectedDate;
    }
    public void clearSelectedDate(){
        selectedDate = null;

    }

    protected CalendarDialogAdapter getAdapter(){
        return adapter;
    }
    protected ViewPager getMonthViewPager() {
        return monthViewPager;
    }

    public interface OnDateSetListener {
        void onDateSet(PersianCaldroidDialog dialog, PersianDate persianDate);
    }
    public PersianCaldroidDialog setOnDateSetListener(OnDateSetListener callback) {
        mCallback = callback;
        return this;
    }


    public void setTypeface(Typeface typeface){
        this.typeface = typeface;
    }
    public Typeface getTypeface() {
        return this.typeface;
    }

    public void setBackgroundResourceForDate(PersianDate date, int backgroundRes) {
        backgroundResourceForDate = new Pair<>(date, backgroundRes);
    }
    public Pair<PersianDate, Integer> getBackgroundResourceForDate() {
        return backgroundResourceForDate;
    }



    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.dialog_calendar, container, false);
        utils = Utils.getInstance(getContext());
        utils.clearYearWarnFlag();
        viewPagerPosition = 0;

        txtDay = (TextView) view.findViewById(R.id.date_picker_day);
        txtMonth = (TextView) view.findViewById(R.id.date_picker_month);
        txtYear = (TextView) view.findViewById(R.id.date_picker_year);

        Button btnDone = (Button) view.findViewById(R.id.done_button);
        Button btnCancel = (Button) view.findViewById(R.id.cancel_button);

        txtDay.setTypeface(typeface);
        txtMonth.setTypeface(typeface);
        txtYear.setTypeface(typeface);
        btnDone.setTypeface(typeface);
        btnCancel.setTypeface(typeface);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onDateSet(PersianCaldroidDialog.this, selectedPersianDate);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        monthViewPager = (ViewPager) view.findViewById(R.id.calendar_pager);

        adapter = new CalendarDialogAdapter(getChildFragmentManager());
        monthViewPager.setAdapter(adapter);
        monthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);

        monthViewPager.addOnPageChangeListener(this);



        // This will immediately be replaced by the same functionality on fragment but is here to
        // make sure enough space is dedicated to actionbar's title and subtitle, kinda hack anyway
        PersianDate today = utils.getToday();

        selectedPersianDate = today;


        if (selectedDate != null) {
            bringDate(selectedDate);
            setMonthYearTitle(utils.getWeekDayName(selectedDate), selectedDate.getDayOfMonth(),
                    utils.getMonthName(selectedDate), selectedDate.getYear());
        }
        else
            setMonthYearTitle(utils.getWeekDayName(today), today.getDayOfMonth(), utils.getMonthName(today), today.getYear());

        return view;
    }

    public void setMonthYearTitle(String dayOfWeek, int day, String monthName, int year) {
        txtDay.setText(String.format(new Locale("fa"), "%2d", day));
        txtMonth.setText(monthName);
        txtYear.setText(String.format(new Locale("fa"), "%4d", year));
    }

    public void changeMonth(int position) {
        monthViewPager.setCurrentItem(monthViewPager.getCurrentItem() + position, true);
    }

    public void selectDay(PersianDate persianDate) {

        selectedPersianDate = persianDate;

        txtDay.setText(String.format(new Locale("fa"), "%d", persianDate.getDayOfMonth()));
//        txtMonth.setText(utils.getMonthName(persianDate));
//        txtYear.setText(String.format(new Locale("fa"), "%d", persianDate.getYear()));
        CivilDate civilDate = DateConverter.persianToCivil(persianDate);

        if (utils.getToday().equals(persianDate)) {

            if (utils.iranTime) {

            }
        } else {

        }

        showEvent(persianDate);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void addEventOnCalendar(PersianDate persianDate) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);

        CivilDate civil = DateConverter.persianToCivil(persianDate);

        intent.putExtra(CalendarContract.Events.DESCRIPTION,
                utils.dayTitleSummary(persianDate));

        Calendar time = Calendar.getInstance();
        time.set(civil.getYear(), civil.getMonth() - 1, civil.getDayOfMonth());

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                time.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                time.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        startActivity(intent);
    }

    private void showEvent(PersianDate persianDate) {
        String holidays = utils.getEventsTitle(persianDate, true);
        String events = utils.getEventsTitle(persianDate, false);


        if (!TextUtils.isEmpty(holidays)) {

        }

        if (!TextUtils.isEmpty(events)) {

        }
    }



    private void bringTodayYearMonth() {
        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_DIALOG);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_DIALOG,
                Constants.BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, -1);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        if (monthViewPager.getCurrentItem() != Constants.MONTHS_LIMIT / 2) {
            monthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);
        }

        selectDay(utils.getToday());
    }

    private void bringDate(PersianDate date) {
        PersianDate today = new PersianDate();
        viewPagerPosition =
                (today.getYear() - date.getYear()) * 12 + today.getMonth() - date.getMonth();

        monthViewPager.setCurrentItem(viewPagerPosition + Constants.MONTHS_LIMIT / 2);

        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_DIALOG);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_DIALOG, viewPagerPosition);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, date.getDayOfMonth());

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        //selectDay(date);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        viewPagerPosition = position - Constants.MONTHS_LIMIT / 2;


        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_DIALOG);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_DIALOG, viewPagerPosition);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, -1);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    //SelectDayDialog dialog = new SelectDayDialog();
    //dialog.show(getChildFragmentManager(), SelectDayDialog.class.getName());

    public int getViewPagerPosition() {
        return viewPagerPosition;
    }
}
