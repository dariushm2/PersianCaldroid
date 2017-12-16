package com.dariushm2.PersianCaldroid.caldroidfragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dariushm2.PersianCaldroid.Constants;
import com.dariushm2.PersianCaldroid.R;
import com.dariushm2.PersianCaldroid.util.Utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import calendar.CivilDate;
import calendar.DateConverter;
import calendar.PersianDate;

public class PersianCaldroidFragment extends Fragment
        implements ViewPager.OnPageChangeListener {
    private ViewPager monthViewPager;
    private CalendarFragmentAdapter calendarFragmentAdapter;
    private Utils utils;

    private AppCompatTextView txtMonthYear;

    private int viewPagerPosition;
    private int currentMonthFragmentPosition;

    private Typeface typeface;


    private HashMap<PersianDate, Integer> backgroundForDatesMap = new HashMap<>();


    private OnDateClickListener mCallback;


    public interface OnDateClickListener {
        void onDateClick(PersianDate persianDate);
    }
    public PersianCaldroidFragment setOnDateClickListener(OnDateClickListener callback) {
        mCallback = callback;
        return PersianCaldroidFragment.this;
    }

    private OnChangeMonthListener changeMonthListener;


    public interface OnChangeMonthListener {
        void onChangeMonth();
    }
    public PersianCaldroidFragment setOnChangeMonthListener(OnChangeMonthListener callback) {
        changeMonthListener = callback;
        return PersianCaldroidFragment.this;
    }

    public void setTypeface(Typeface typeface){
        this.typeface = typeface;
    }
    public Typeface getTypeface() {
        return this.typeface;
    }

    /**
     * Set backgroundForDateMap
     */
    public void setBackgroundResourceForDates(HashMap<PersianDate, Integer> backgroundForDatesMap) {
        // Clear first
        this.backgroundForDatesMap.clear();

//        if (backgroundForDatesMap == null || backgroundForDatesMap.size() == 0) {
//            Log.e("Res", "empty");
//            return;
//        }
        this.backgroundForDatesMap = backgroundForDatesMap;
        //Log.e("SETBACK...",Integer.toString(this.backgroundForDatesMap.size()));

    }

    public HashMap<PersianDate, Integer> getBackgroundForDatesMap(){
        return backgroundForDatesMap;
    }

    public void refresh() {
        //String tag = Constants.makeFragmentName(R.id.calendar_pager, viewPagerPosition);
        //MonthFragment fragment = (MonthFragment) calendarFragmentAdapter.getCurrentFragment();
        MonthFragment fragment = (MonthFragment) calendarFragmentAdapter.getRegisteredFragment(currentMonthFragmentPosition);
        fragment.refresh();
        fragment = (MonthFragment) calendarFragmentAdapter.getRegisteredFragment(currentMonthFragmentPosition - 1);
        if (fragment != null)
            fragment.refresh();
        fragment = (MonthFragment) calendarFragmentAdapter.getRegisteredFragment(currentMonthFragmentPosition + 1);
        if (fragment != null)
            fragment.refresh();
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        utils = Utils.getInstance(getContext());
        utils.clearYearWarnFlag();
        viewPagerPosition = 0;

        currentMonthFragmentPosition = 0;
        currentMonthFragmentPosition = Constants.MONTHS_LIMIT / 2;


        txtMonthYear = (AppCompatTextView) view.findViewById(R.id.pc_month_year_textview);

        txtMonthYear.setTypeface(typeface);

        monthViewPager = (ViewPager) view.findViewById(R.id.calendar_pager);

        calendarFragmentAdapter = new CalendarFragmentAdapter(getChildFragmentManager());
        monthViewPager.setAdapter(calendarFragmentAdapter);
        monthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);

        monthViewPager.addOnPageChangeListener(this);

        AppCompatImageView prev = (AppCompatImageView) view.findViewById(R.id.prev);
        AppCompatImageView next = (AppCompatImageView) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMonth(1);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMonth(-1);
            }
        });



        // This will immediately be replaced by the same functionality on fragment but is here to
        // make sure enough space is dedicated to actionbar's title and subtitle, kinda hack anyway
        PersianDate today = utils.getToday();
        //utils.setActivityTitleAndSubtitle(getActivity(), utils.getMonthName(today), utils.formatNumber(today.getYear()));
        setMonthYearTitle(utils.getMonthName(today), today.getYear());
        return view;
    }

    public void setMonthYearTitle(String monthName, int year) {
        txtMonthYear.setText(monthName + " " + String.format(new Locale("fa"), "%4d", year));
    }

    public void changeMonth(int position) {
        monthViewPager.setCurrentItem(monthViewPager.getCurrentItem() + position, true);
    }

    public void selectDay(PersianDate persianDate) {
        mCallback.onDateClick(persianDate);
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
        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT,
                Constants.BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, -1);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        if (monthViewPager.getCurrentItem() != Constants.MONTHS_LIMIT / 2) {
            monthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);
        }

        selectDay(utils.getToday());
    }

    public void bringDate(PersianDate date) {
        PersianDate today = utils.getToday();
        viewPagerPosition =
                (today.getYear() - date.getYear()) * 12 + today.getMonth() - date.getMonth();

        monthViewPager.setCurrentItem(viewPagerPosition + Constants.MONTHS_LIMIT / 2);

        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT, viewPagerPosition);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, date.getDayOfMonth());

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        selectDay(date);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

        //refresh();
        changeMonthListener.onChangeMonth();

        viewPagerPosition = position - Constants.MONTHS_LIMIT / 2;

        currentMonthFragmentPosition = position;

        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT, viewPagerPosition);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, -1);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public int getViewPagerPosition() {
        return viewPagerPosition;
    }
}
